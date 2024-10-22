package com.xceptance.neodymium.junit4.testclasses.browser.classonly;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.common.browser.SuppressBrowsers;
import com.xceptance.neodymium.junit4.NeodymiumRunner;
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
