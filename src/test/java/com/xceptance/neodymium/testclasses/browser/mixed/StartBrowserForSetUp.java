package com.xceptance.neodymium.testclasses.browser.mixed;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("chrome")
public class StartBrowserForSetUp
{
    private static WebDriver webdriverTest;

    private static WebDriver webdriverBefore;

    private static WebDriver webdriverBefore1;

    @Before
    public void before()
    {
        Assert.assertNotNull("Browser should be started for the setup", Neodymium.getDriver());
        webdriverBefore = Neodymium.getDriver();
        Assert.assertNotEquals(webdriverBefore, webdriverBefore1);
    }

    @Before
    public void before1()
    {
        Assert.assertNotNull("Browser should be started for the setup", Neodymium.getDriver());
        webdriverBefore1 = Neodymium.getDriver();
        Assert.assertNotEquals(webdriverBefore, webdriverBefore1);
    }

    @Test
    public void first() throws Exception
    {
        Assert.assertNotNull("Browser should be started for the setup", Neodymium.getDriver());
        webdriverTest = Neodymium.getDriver();
        Assert.assertNotEquals(webdriverTest, webdriverBefore);
        Assert.assertNotEquals(webdriverTest, webdriverBefore1);
    }

    @AfterClass
    public static void afterClass()
    {
        NeodymiumWebDriverTest.assertWebDriverClosed(webdriverTest);
        NeodymiumWebDriverTest.assertWebDriverClosed(webdriverBefore);
        NeodymiumWebDriverTest.assertWebDriverClosed(webdriverBefore1);
    }
}
