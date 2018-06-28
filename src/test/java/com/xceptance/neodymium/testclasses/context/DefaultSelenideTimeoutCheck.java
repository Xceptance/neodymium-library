package com.xceptance.neodymium.testclasses.context;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.codeborne.selenide.Configuration;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_headless")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultSelenideTimeoutCheck
{
    @Test
    public void test1() throws Exception
    {
        Configuration.timeout = 1234;
        Configuration.collectionsTimeout = 1234;
    }

    @Test
    public void test2() throws Exception
    {
        Assert.assertEquals(3000, Configuration.timeout);
        Assert.assertEquals(6000, Configuration.collectionsTimeout);
    }
}
