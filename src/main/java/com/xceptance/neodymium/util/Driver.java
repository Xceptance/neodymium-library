package com.xceptance.neodymium.util;

import java.util.List;
import java.util.function.Supplier;

import com.xceptance.neodymium.module.statement.browser.BrowserStatement;

import cucumber.api.Scenario;

public class Driver
{
    private static ThreadLocal<BrowserStatement> browserStatement = ThreadLocal.withInitial(new Supplier<BrowserStatement>()
    {
        @Override
        public BrowserStatement get()
        {
            return new BrowserStatement();
        }

    });

    public static void setUp(final String browserProfileName)
    {

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
     *                Driver.tearDown(scenario);
     *            }
     *            </pre>
     **/
    public static void tearDown(Scenario scenario)
    {
        browserStatement.get().teardown(scenario.isFailed());
    }

    public static List<String> getBrowserTags()
    {
        return BrowserStatement.getBrowserTags();
    }
}
