package com.xceptance.neodymium.testclasses.multibrowser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
public class BrowserWithoutAvailableEnvironment
{

    @Test(expected = IllegalArgumentException.class)
    @Browser("Galaxy_Note3_Emulation")
    public void testWithUnavailableEnvironment()
    {
        Assert.assertEquals("Galaxy_Note3_Emulation", Neodymium.getBrowserProfileName());
    }
}
