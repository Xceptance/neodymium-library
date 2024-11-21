package com.xceptance.neodymium.junit5.browser;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import com.xceptance.neodymium.common.browser.BrowserMethodData;
import com.xceptance.neodymium.common.browser.BrowserRunner;

public class BrowserExecutionCallback implements BeforeEachCallback, TestWatcher
{
    private BrowserRunner browserRunner;

    public BrowserExecutionCallback(BrowserMethodData browserTag, String testName)
    {
        browserRunner = new BrowserRunner(browserTag, testName);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception
    {
        browserRunner.setUpTest();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause)
    {
        browserRunner.teardown(true);
    }

    @Override
    public void testSuccessful(ExtensionContext context)
    {
        browserRunner.teardown(false);
    }
}
