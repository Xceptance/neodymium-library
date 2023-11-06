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
import com.xceptance.neodymium.module.statement.browser.multibrowser.DontStartNewBrowserForCleanUp;
import com.xceptance.neodymium.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@DontStartNewBrowserForCleanUp
@Browser("chrome")
public class NewBrowserIsNotStartedForCleanUp
{
    private static WebDriver webDriver1;

    @BeforeClass
    public static void beforeClass()
    {
        Assert.assertNull(webDriver1);
        Assert.assertNull(Neodymium.getDriver());
    }

    @Test
    public void test1()
    {
        webDriver1 = Neodymium.getDriver();
        Assert.assertEquals(webDriver1, Neodymium.getDriver());
    }

    @After
    public void after()
    {
        Assert.assertEquals(webDriver1, Neodymium.getDriver());
    }

    @After
    public void after1()
    {
        Assert.assertEquals(webDriver1, Neodymium.getDriver());
    }

    @AfterClass
    public static void afterClass() throws InterruptedException
    {
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver1);
    }
}
