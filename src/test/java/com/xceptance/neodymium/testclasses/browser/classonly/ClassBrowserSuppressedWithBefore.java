package com.xceptance.neodymium.testclasses.browser.classonly;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("chrome")
@SuppressBrowsers
public class ClassBrowserSuppressedWithBefore
{
    @Before
    public void before()
    {
        Assert.assertNull("Browser should not be started for cleanup", Neodymium.getDriver());
    }

    @Test
    public void first() throws Exception
    {
    }
}
