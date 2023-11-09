package com.xceptance.neodymium.testclasses.browser.methodonly;

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
public class ClassBrowserSuppressedWithAfter
{
    @Test
    public void first() throws Exception
    {
    }

    @After
    public void after()
    {
        Assert.assertNull("Browser should not be started for cleanup", Neodymium.getDriver());
    }
}
