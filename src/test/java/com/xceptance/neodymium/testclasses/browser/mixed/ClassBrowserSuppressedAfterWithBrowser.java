package com.xceptance.neodymium.testclasses.browser.mixed;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("chrome")
@SuppressBrowsers
public class ClassBrowserSuppressedAfterWithBrowser
{
    @Test
    public void first() throws Exception
    {
    }

    @After
    @Browser("chrome")
    public void after()
    {
        Assert.assertNotNull("Browser should be started for cleanup", Neodymium.getDriver());
    }
}
