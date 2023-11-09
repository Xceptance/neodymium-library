package com.xceptance.neodymium.testclasses.browser.mixed;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("chrome")
public class StartBrowserForCleanUp
{
    private static WebDriver webdriverTest;

    private static WebDriver webdriverAfter;

    private static WebDriver webdriverAfter1;

    @Test
    public void first() throws Exception
    {
        Assert.assertNotNull("Browser should be started for the cleanup", Neodymium.getDriver());
        webdriverTest = Neodymium.getDriver();
    }

    @After
    public void after()
    {
        Assert.assertNotNull("Browser should be started for the cleanup", Neodymium.getDriver());
        webdriverAfter = Neodymium.getDriver();
        Assert.assertNotEquals(webdriverTest, webdriverAfter);
        Assert.assertNotEquals(webdriverAfter, webdriverAfter1);
    }

    @After
    public void after1()
    {
        Assert.assertNotNull("Browser should be started for the cleanup", Neodymium.getDriver());
        webdriverAfter1 = Neodymium.getDriver();
        Assert.assertNotEquals(webdriverTest, webdriverAfter1);
        Assert.assertNotEquals(webdriverAfter, webdriverAfter1);
    }

    @AfterClass
    public static void afterClass()
    {
        NeodymiumWebDriverTest.assertWebDriverClosed(webdriverTest);
        NeodymiumWebDriverTest.assertWebDriverClosed(webdriverAfter);
        NeodymiumWebDriverTest.assertWebDriverClosed(webdriverAfter1);
    }
}
