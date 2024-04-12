package com.xceptance.neodymium.common.browser;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.xceptance.neodymium.common.Data;
import com.xceptance.neodymium.util.Neodymium;

public class BrowserData extends Data
{
    private List<String> classBrowsers;

    private List<String> systemBrowserFilter;

    private List<BrowserMethodData> browserMethodDatas = new LinkedList<>();

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
            systemBrowserFilter.addAll(Arrays.asList(browserDefinitionsProperty.split(",")));
        }
    }

    public List<BrowserMethodData> getBrowserTags()
    {
        return browserMethodDatas;
    }

    public List<BrowserMethodData> createIterationData(Method testMethod)
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
                browsers = computeRandomBrowsers(testMethod, methodRandomBrowsersAnnotations, browsers);
            }
            else if (!classRandomBrowsersAnnotation.isEmpty() && methodBrowsers.isEmpty())
            {
                // evaluate the class level (including inheritance)
                // Note: if browsers are annotated on method level they prohibit the evaluation of the random
                // browser annotation on class level
                browsers = computeRandomBrowsers(testMethod, classRandomBrowsersAnnotation, browsers);
            }
        }
        if (systemBrowserFilter != null && !systemBrowserFilter.isEmpty())
        {
            return browsers.stream()
                           .filter(browserTag -> systemBrowserFilter.contains(browserTag))
                           .map(browserTag -> addKeepBrowserOpenInformation(browserTag, testMethod))
                           .collect(Collectors.toList());
        }
        return browsers.stream()
                       .map(browserTag -> addKeepBrowserOpenInformation(browserTag, testMethod))
                       .collect(Collectors.toList());
    }

    private BrowserMethodData addKeepBrowserOpenInformation(String browserTag, Method method)
    {
        List<KeepBrowserOpen> methodKeepBrowserOpenAnnotations = getAnnotations(method, KeepBrowserOpen.class);
        List<KeepBrowserOpen> classKeepBrowserOpenAnnotations = getAnnotations(method.getDeclaringClass(), KeepBrowserOpen.class);

        boolean keepOpen = Neodymium.configuration().keepBrowserOpen();
        boolean keepOpenOnFailure = Neodymium.configuration().keepBrowserOpenOnFailure();

        if (!classKeepBrowserOpenAnnotations.isEmpty())
        {
            KeepBrowserOpen keepBrowserOpen = classKeepBrowserOpenAnnotations.get(0);
            if (keepBrowserOpen.onlyOnFailure())
            {
                keepOpen = false;
                keepOpenOnFailure = true;
            }
            else
            {
                keepOpen = true;
                keepOpenOnFailure = false;
            }
        }

        if (!methodKeepBrowserOpenAnnotations.isEmpty())
        {
            KeepBrowserOpen keepBrowserOpen = methodKeepBrowserOpenAnnotations.get(0);
            if (keepBrowserOpen.onlyOnFailure())
            {
                keepOpen = false;
                keepOpenOnFailure = true;
            }
            else
            {
                keepOpen = true;
                keepOpenOnFailure = false;
            }
        }
        return new BrowserMethodData(browserTag, keepOpen, keepOpenOnFailure);
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
