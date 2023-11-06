package com.xceptance.neodymium.testclasses.browser.classonly;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("chrome")
public class NewBrowserIsStartedForCleanUp
{
    private static WebDriver webDriver1;

    private static WebDriver webDriver2;

    private static WebDriver webDriver3;

    @BeforeClass
    public static void beforeClass()
    {
        Assert.assertNull(webDriver1);
        Assert.assertNull(webDriver2);
        Assert.assertNull(webDriver3);
        Assert.assertNull(Neodymium.getDriver());
    }

    @Test
    public void test1()
    {
        Assert.assertNotNull(Neodymium.getDriver());
        webDriver1 = Neodymium.getDriver();
        Assert.assertEquals(webDriver1, Neodymium.getDriver());
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver1);
    }

    @After
    public void after()
    {
        Assert.assertNotNull(Neodymium.getDriver());
        Assert.assertNotEquals(webDriver1, Neodymium.getDriver());
        webDriver2 = Neodymium.getDriver();
    }

    @After
    public void after1()
    {
        Assert.assertNotNull(Neodymium.getDriver());
        Assert.assertNotEquals(webDriver2, Neodymium.getDriver());
        webDriver3 = Neodymium.getDriver();
    }

    @AfterClass
    public static void afterClass()
    {
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver1);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver2);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver3);
    }
}
