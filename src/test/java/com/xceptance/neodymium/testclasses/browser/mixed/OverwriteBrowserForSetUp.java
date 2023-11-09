package com.xceptance.neodymium.testclasses.browser.mixed;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("chrome")
public class OverwriteBrowserForSetUp
{
    @Before
    @Browser("Chrome_headless")
    public void before()
    {
        Assert.assertEquals("Chrome_headless", Neodymium.getBrowserProfileName());
    }

    @Test
    public void first() throws Exception
    {
        Assert.assertEquals("chrome", Neodymium.getBrowserProfileName());
    }
}
