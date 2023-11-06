package com.xceptance.neodymium.testclasses.browser.mixed;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;
import com.xceptance.neodymium.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@SuppressBrowsers
public class StartBrowserForCleanUp
{
    private static WebDriver webdriver;

    @Test
    public void first() throws Exception
    {
        Assert.assertNull("Browser should not be started for the test", Neodymium.getDriver());
    }

    @After
    @Browser("chrome")
    public void after()
    {
        Assert.assertNotNull("Browser should be started for the cleanup", Neodymium.getDriver());
        webdriver = Neodymium.getDriver();
    }

    @AfterClass
    public static void afterClass()
    {
        NeodymiumWebDriverTest.assertWebDriverClosed(webdriver);
    }
}
