package com.xceptance.neodymium.junit5.browser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.common.browser.SuppressBrowsers;
import com.xceptance.neodymium.junit5.Data;
import com.xceptance.neodymium.util.Neodymium;

public class BrowserData extends Data
{
    private List<String> classBrowsers;

    private List<String> browserTags = new LinkedList<>();

    private static final String SYSTEM_PROPERTY_BROWSERDEFINITION = "browserdefinition";

    public BrowserData(Class<?> testClass)
    {
        this();
        if (getAnnotations(testClass, SuppressBrowsers.class).isEmpty())
        {
            classBrowsers = getAnnotations(testClass, Browser.class).stream().map(annotation -> annotation.value()).distinct().collect(Collectors.toList());
        }
        else
        {
            classBrowsers = new LinkedList<>();
        }
    }

    public BrowserData()
    {
        populateBrowserDataWithGlobalInformation();
    }

    private void populateBrowserDataWithGlobalInformation()
    {
        final String ieDriverPath = Neodymium.configuration().getIeDriverPath();
        final String chromeDriverPath = Neodymium.configuration().getChromeDriverPath();
        final String geckoDriverPath = Neodymium.configuration().getFirefoxDriverPath();

        // shall we run old school firefox?
        final boolean firefoxLegacy = Neodymium.configuration().useFirefoxLegacy();
        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, Boolean.toString(!firefoxLegacy));

        if (!StringUtils.isEmpty(ieDriverPath))
        {
            System.setProperty("webdriver.ie.driver", ieDriverPath);
        }
        if (!StringUtils.isEmpty(chromeDriverPath))
        {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        }
        if (!StringUtils.isEmpty(geckoDriverPath))
        {
            System.setProperty("webdriver.gecko.driver", geckoDriverPath);
        }

        // TODO: do we need a possibility to define browser tags globaly via system var? Is this opportunity documented?

        // get test specific browser definitions (aka browser tag see browser.properties)
        // could be one value or comma separated list of values
        String browserDefinitionsProperty = System.getProperty(SYSTEM_PROPERTY_BROWSERDEFINITION, "");
        browserDefinitionsProperty = browserDefinitionsProperty.replaceAll("\\s", "");

        // parse test specific browser definitions
        if (!StringUtils.isEmpty(browserDefinitionsProperty))
        {
            browserTags.addAll(Arrays.asList(browserDefinitionsProperty.split(",")));
        }
    }

    public List<String> getBrowserTags()
    {
        return browserTags;
    }

    public List<String> createIterationData(Method testMethod)
    {
        if (getAnnotations(testMethod, SuppressBrowsers.class).isEmpty())
        {
            List<String> methodBrowsers = getAnnotations(testMethod, Browser.class).stream().map(annotation -> annotation.value()).distinct().collect(Collectors.toList());
            if (!methodBrowsers.isEmpty())
            {
                return methodBrowsers;
            }
            return classBrowsers;
        }
        return new LinkedList<>();
    }
}
