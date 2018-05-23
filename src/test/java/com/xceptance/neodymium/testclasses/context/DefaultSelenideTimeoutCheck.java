package com.xceptance.neodymium.testclasses.context;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Configuration;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("headless_chrome")
public class DefaultSelenideTimeoutCheck
{
    @Test
    public void first() throws Exception
    {
        Assert.assertEquals(3000, Configuration.timeout);
        Assert.assertEquals(6000, Configuration.collectionsTimeout);
    }
}
