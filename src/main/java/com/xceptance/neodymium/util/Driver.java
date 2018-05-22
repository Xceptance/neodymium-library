package com.xceptance.neodymium.util;

import java.util.function.Supplier;

import com.xceptance.neodymium.module.statement.browser.BrowserStatement;

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

    public static void tearDown(boolean testFailed)
    {
        browserStatement.get().teardown(testFailed);
    }
}
