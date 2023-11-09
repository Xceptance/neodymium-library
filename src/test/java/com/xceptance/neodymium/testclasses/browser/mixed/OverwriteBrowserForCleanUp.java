package com.xceptance.neodymium.testclasses.browser.mixed;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("chrome")
public class OverwriteBrowserForCleanUp
{
    @Test
    public void first() throws Exception
    {
        Assert.assertEquals("chrome", Neodymium.getBrowserProfileName());
    }

    @After
    @Browser("Chrome_headless")
    public void after()
    {
        Assert.assertEquals("Chrome_headless", Neodymium.getBrowserProfileName());
    }
}
