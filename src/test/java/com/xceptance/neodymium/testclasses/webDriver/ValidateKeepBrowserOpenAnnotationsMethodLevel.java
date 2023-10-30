package com.xceptance.neodymium.testclasses.webDriver;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.KeepBrowserOpen;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.WebDriverCache;
import com.xceptance.neodymium.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.util.Neodymium;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(NeodymiumRunner.class)
public class ValidateKeepBrowserOpenAnnotationsMethodLevel
{
    private static WebDriver webDriver1;

    private static WebDriver webDriver2;

    @BeforeClass
    public static void beforeClass()
    {
        Assert.assertNull(webDriver1);
        Assert.assertNull(Neodymium.getDriver());
    }

    @Before
    public void before()
    {
        if (webDriver1 == null)
        {
            webDriver1 = Neodymium.getDriver();
        }
        else if (webDriver2 == null)
        {
            webDriver2 = Neodymium.getDriver();
        }
        else
        {
            Assert.assertNotNull(Neodymium.getDriver());
        }
        Assert.assertNotNull(webDriver1);
    }

    @Test
    @Browser("Chrome_1024x768")
    @KeepBrowserOpen(false)
    public void test1()
    {
        Assert.assertEquals(webDriver1, Neodymium.getDriver());
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver1);
    }

    @Test
    @Browser("Chrome_1024x768")
    @KeepBrowserOpen(false)
    public void test2()
    {
        Assert.assertNotEquals(webDriver1, webDriver2);
        Assert.assertEquals(webDriver2, Neodymium.getDriver());
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver2);
    }

    @After
    public void after()
    {
        NeodymiumWebDriverTest.assertWebDriverAlive(Neodymium.getDriver());
    }

    @AfterClass
    public static void afterClass()
    {
        Assert.assertEquals(0, WebDriverCache.instance.getWebDriverStateContainerCacheSize());

        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver2);
        webDriver1.quit();
        webDriver2.quit();
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver1);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver2);
    }
}
