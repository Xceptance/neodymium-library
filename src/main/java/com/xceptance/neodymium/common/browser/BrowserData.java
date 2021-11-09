package com.xceptance.neodymium.common.browser;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.xceptance.neodymium.common.Data;
import com.xceptance.neodymium.util.Neodymium;

public class BrowserData extends Data
{
    private List<String> classBrowsers;

    private List<String> browserTags = new LinkedList<>();

    private List<RandomBrowsers> classRandomBrowsersAnnotation;

    private static final String SYSTEM_PROPERTY_BROWSERDEFINITION = "browserdefinition";

    public BrowserData(Class<?> testClass)
    {
        this();
        initClassAnnotationsFor(testClass);
    }

    public void initClassAnnotationsFor(Class<?> testClass)
    {
        classRandomBrowsersAnnotation = getAnnotations(testClass, RandomBrowsers.class);

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
        List<String> browsers = new LinkedList<>();
        List<String> methodBrowsers = new LinkedList<>();
        if (getAnnotations(testMethod, SuppressBrowsers.class).isEmpty())
        {
            methodBrowsers = getAnnotations(testMethod, Browser.class).stream().map(annotation -> annotation.value()).distinct()
                                                                      .collect(Collectors.toList());
            if (!methodBrowsers.isEmpty())
            {
                browsers = methodBrowsers;
            }
            else
            {
                browsers = classBrowsers;
            }
            List<RandomBrowsers> methodRandomBrowsersAnnotations = getAnnotations(testMethod, RandomBrowsers.class);

            // choose a random set from the available browser annotations
            if (!methodRandomBrowsersAnnotations.isEmpty())
            {
                // evaluate the method level (top priority)
                return computeRandomBrowsers(testMethod, methodRandomBrowsersAnnotations, browsers);
            }
            else if (!classRandomBrowsersAnnotation.isEmpty() && methodBrowsers.isEmpty())
            {
                // evaluate the class level (including inheritance)
                // Note: if browsers are annotated on method level they prohibit the evaluation of the random
                // browser annotation on class level
                return computeRandomBrowsers(testMethod, classRandomBrowsersAnnotation, browsers);
            }
        }

        return browsers;
    }

    private List<String> computeRandomBrowsers(final Method method, final List<RandomBrowsers> randomBrowsersAnnotation,
                                               final List<String> browsers)
    {
        if (randomBrowsersAnnotation.get(0).value() > browsers.size())
        {
            String msg = MessageFormat.format("Method ''{0}'' is marked to be run with {1} random browsers, but there are only {2} available",
                                              method.getName(), randomBrowsersAnnotation.get(0).value(), browsers.size());
            throw new IllegalArgumentException(msg);
        }
        if (randomBrowsersAnnotation.get(0).value() > 0)
        {
            Collections.shuffle(browsers, Neodymium.getRandom());
            return browsers.subList(0, randomBrowsersAnnotation.get(0).value());
        }
        return browsers;
    }
}
