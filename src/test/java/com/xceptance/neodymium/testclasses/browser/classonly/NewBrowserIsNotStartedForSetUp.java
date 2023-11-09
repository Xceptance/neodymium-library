package com.xceptance.neodymium.testclasses.browser.classonly;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.DontStartNewBrowserForSetUp;
import com.xceptance.neodymium.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@DontStartNewBrowserForSetUp
@Browser("chrome")
public class NewBrowserIsNotStartedForSetUp
{
    private static WebDriver webDriverBefore;

    private static WebDriver webDriverBefore1;

    private static WebDriver webDriverTest;

    @BeforeClass
    public static void beforeClass()
    {
        Assert.assertNull(webDriverBefore);
        Assert.assertNull(webDriverBefore1);
        Assert.assertNull(webDriverTest);
        Assert.assertNull(Neodymium.getDriver());
    }

    @Before
    public void before()
    {
        NeodymiumWebDriverTest.assertWebDriverAlive(Neodymium.getDriver());
        if (webDriverBefore1 != null)
        {
            Assert.assertEquals(webDriverBefore1, Neodymium.getDriver());
        }
        webDriverBefore = Neodymium.getDriver();
    }

    @Before
    public void before1()
    {
        NeodymiumWebDriverTest.assertWebDriverAlive(Neodymium.getDriver());
        if (webDriverBefore != null)
        {
            Assert.assertEquals(webDriverBefore, Neodymium.getDriver());
        }
        webDriverBefore1 = Neodymium.getDriver();
    }

    @Test
    public void test1()
    {
        webDriverTest = Neodymium.getDriver();
        Assert.assertEquals(webDriverBefore, webDriverTest);
        Assert.assertEquals(webDriverBefore1, webDriverTest);
    }

    @AfterClass
    public static void afterClass() throws InterruptedException
    {
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriverTest);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriverBefore);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriverBefore1);
    }
}
