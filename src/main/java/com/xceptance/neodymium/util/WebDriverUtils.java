package com.xceptance.neodymium.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import com.xceptance.neodymium.module.statement.browser.BrowserStatement;

import cucumber.api.Scenario;

public class WebDriverUtils
{
    private static ThreadLocal<BrowserStatement> browserStatement = ThreadLocal.withInitial(new Supplier<BrowserStatement>()
    {
        @Override
        public BrowserStatement get()
        {
            return new BrowserStatement();
        }
    });

    /**
     * @param browserProfileName
     *            the browser profile name that is configured in the browser.properties file
     * 
     *            <pre>
     *            public void setup(String browserProfileName)
     *            {
     *                WebDriverUtils.setUp(browserProfileName);
     *            }
     *            </pre>
     **/
    public static void setUp(final String browserProfileName)
    {
        if (browserStatement.get().getBrowserTags().contains(browserProfileName))
        {
            browserStatement.get().setUpTest(browserProfileName);
        }
        else
        {
            throw new RuntimeException("Could not find any tag that matches a browser tag");
        }
    }

    /**
     * @param scenario
     *            Scenario is a Cucumber API class that can be gathered in hooks via dependency injection
     * 
     *            <pre>
     *            public void setup(Scenario scenario)
     *            {
     *                WebDriverUtils.setUpWithBrowserTag(scenario);
     *            }
     *            </pre>
     **/
    public static void setUpWithBrowserTag(Scenario scenario)
    {
        String browserProfileName = getFirstMatchingBrowserTag(scenario);
        browserStatement.get().setUpTest(browserProfileName);
    }

    /**
     * @param scenario
     *            Scenario is a Cucumber API class that can be gathered in hooks via dependency injection
     * 
     *            <pre>
     *            &#64;cucumber.api.java.After(order = 100)
     *            public void tearDown(Scenario scenario)
     *            {
     *                WebDriverUtils.tearDown(scenario);
     *            }
     *            </pre>
     **/
    public static void tearDown(Scenario scenario)
    {
        tearDown(scenario.isFailed());
    }

    /**
     * @param isFailed
     *            boolean to pass if the test failed in order to use Neodymium's keepBrowserOpenOnFailure feature
     **/
    public static void tearDown(boolean isFailed)
    {
        browserStatement.get().teardown(isFailed);
    }

    static String getFirstMatchingBrowserTag(Scenario scenario)
    {
        Collection<String> scenarioTags = scenario.getSourceTagNames();
        List<String> browserTags = browserStatement.get().getBrowserTags();

        String firstBrowserTagFound = "";
        for (String tag : scenarioTags)
        {
            String compareString = tag.substring(1);// substring to cut off leading '@'
            if (browserTags.contains(compareString))
            {
                if (StringUtils.isEmpty(firstBrowserTagFound))
                {
                    firstBrowserTagFound = compareString;
                }
                else
                {
                    throw new RuntimeException("Found more than one matching browser tag");
                }
            }
        }

        if (StringUtils.isEmpty(firstBrowserTagFound))
        {
            throw new RuntimeException("Could not find any tag that matches a browser tag");
        }
        else
        {
            return firstBrowserTagFound;
        }
    }
}
