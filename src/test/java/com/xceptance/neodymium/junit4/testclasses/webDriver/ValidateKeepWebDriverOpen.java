package com.xceptance.neodymium.junit4.testclasses.webDriver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.browserup.bup.BrowserUpProxy;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.common.browser.WebDriverCache;
import com.xceptance.neodymium.junit4.NeodymiumRunner;
import com.xceptance.neodymium.junit4.tests.NeodymiumTest;
import com.xceptance.neodymium.junit4.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.util.Neodymium;

/*
 * Validate that the web driver is kept open after the test is finished.
 * Validate that the web driver is not reused.
 * Attention: this test needs to use browsers that are not headless.
 */
@RunWith(NeodymiumRunner.class)
public class ValidateKeepWebDriverOpen
{
    private static WebDriver webDriverBeforeForTest1;

    private static WebDriver webDriverBeforeForTest2;

    private static WebDriver webDriverTest1;

    private static WebDriver webDriverTest2;

    private static WebDriver webDriverAfterForTest1;

    private static WebDriver webDriverAfterForTest2;

    private static BrowserUpProxy proxyBeforeForTest1;

    private static BrowserUpProxy proxyBeforeForTest2;

    private static BrowserUpProxy proxyTest1;

    private static BrowserUpProxy proxyTest2;

    private static BrowserUpProxy proxyAfterForTest1;

    private static BrowserUpProxy proxyAfterForTest2;

    private static File tempConfigFile;

    @BeforeClass
    public static void beforeClass()
    {
        // set up a temporary neodymium.properties
        final String fileLocation = "config/temp-ValidateKeepWebDriverOpen-neodymium.properties";
        tempConfigFile = new File("./" + fileLocation);
        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.webDriver.keepBrowserOpen", "true");
        properties.put("neodymium.localproxy", "true");
        NeodymiumTest.writeMapToPropertiesFile(properties, tempConfigFile);
        ConfigFactory.setProperty(Neodymium.TEMPORARY_CONFIG_FILE_PROPERTY_NAME, "file:" + fileLocation);
    }

    @Before
    public void before()
    {
        if (webDriverAfterForTest1 == null)
        {
            webDriverBeforeForTest1 = Neodymium.getDriver();
            proxyBeforeForTest1 = Neodymium.getLocalProxy();
        }
        else
        {
            webDriverBeforeForTest2 = Neodymium.getDriver();
            proxyBeforeForTest2 = Neodymium.getLocalProxy();
        }
    }

    @Test
    @Browser("Chrome_1024x768")
    public void test1()
    {
        webDriverTest1 = Neodymium.getDriver();
        Assert.assertNotEquals(webDriverBeforeForTest1, webDriverTest1);
        Assert.assertNotEquals(webDriverBeforeForTest2, webDriverTest1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriverBeforeForTest1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriverTest1);
        if (webDriverAfterForTest2 != null)
        {
            NeodymiumWebDriverTest.assertWebDriverAlive(webDriverAfterForTest2);
        }

        proxyTest1 = Neodymium.getLocalProxy();
        Assert.assertNotEquals(proxyBeforeForTest1, proxyTest1);
        Assert.assertNotEquals(proxyBeforeForTest2, proxyTest1);
        NeodymiumWebDriverTest.assertProxyAlive(proxyBeforeForTest1);
        NeodymiumWebDriverTest.assertProxyAlive(proxyTest1);
        if (proxyBeforeForTest2 != null)
        {
            NeodymiumWebDriverTest.assertProxyAlive(proxyBeforeForTest2);
        }
    }

    @Test
    @Browser("Chrome_1024x768")
    public void test2()
    {
        webDriverTest2 = Neodymium.getDriver();
        Assert.assertNotEquals(webDriverBeforeForTest1, webDriverTest2);
        Assert.assertNotEquals(webDriverBeforeForTest2, webDriverTest1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriverBeforeForTest2);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriverTest2);
        if (webDriverAfterForTest2 != null)
        {
            NeodymiumWebDriverTest.assertWebDriverAlive(webDriverAfterForTest2);
        }

        proxyTest2 = Neodymium.getLocalProxy();
        Assert.assertNotEquals(proxyBeforeForTest1, proxyTest2);
        Assert.assertNotEquals(proxyBeforeForTest2, proxyTest2);
        NeodymiumWebDriverTest.assertProxyAlive(proxyBeforeForTest2);
        NeodymiumWebDriverTest.assertProxyAlive(proxyTest2);
        if (proxyBeforeForTest2 != null)
        {
            NeodymiumWebDriverTest.assertProxyAlive(proxyBeforeForTest2);
        }
    }

    @After
    public void after()
    {
        if (webDriverTest2 == null)
        {
            webDriverAfterForTest1 = Neodymium.getDriver();
            proxyAfterForTest1 = Neodymium.getLocalProxy();
            Assert.assertNotEquals(webDriverAfterForTest1, webDriverTest1);
            Assert.assertNotEquals(proxyAfterForTest1, proxyTest1);
        }
        else
        {
            webDriverAfterForTest2 = Neodymium.getDriver();
            proxyAfterForTest2 = Neodymium.getLocalProxy();
            Assert.assertNotEquals(webDriverAfterForTest1, webDriverTest1);
            Assert.assertNotEquals(webDriverAfterForTest2, webDriverTest1);
            Assert.assertNotEquals(webDriverAfterForTest2, webDriverTest2);
            Assert.assertNotEquals(proxyAfterForTest1, proxyTest1);
            Assert.assertNotEquals(proxyAfterForTest2, proxyTest1);
            Assert.assertNotEquals(proxyAfterForTest2, proxyTest2);
        }

        NeodymiumWebDriverTest.assertWebDriverAlive(Neodymium.getDriver());
        NeodymiumWebDriverTest.assertProxyAlive(Neodymium.getLocalProxy());
    }

    @AfterClass
    public static void afterClass()
    {
        Assert.assertEquals(0, WebDriverCache.instance.getWebDriverStateContainerCacheSize());

        NeodymiumWebDriverTest.assertWebDriverAlive(webDriverBeforeForTest1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriverBeforeForTest2);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriverTest1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriverTest2);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriverAfterForTest1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriverAfterForTest2);

        webDriverBeforeForTest1.quit();
        webDriverBeforeForTest2.quit();
        webDriverTest1.quit();
        webDriverTest2.quit();
        webDriverAfterForTest1.quit();
        webDriverAfterForTest2.quit();

        NeodymiumWebDriverTest.assertWebDriverClosed(webDriverBeforeForTest1);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriverBeforeForTest2);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriverTest1);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriverTest2);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriverAfterForTest1);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriverAfterForTest2);

        NeodymiumWebDriverTest.assertProxyAlive(proxyBeforeForTest1);
        NeodymiumWebDriverTest.assertProxyAlive(proxyAfterForTest2);
        NeodymiumWebDriverTest.assertProxyAlive(proxyTest1);
        NeodymiumWebDriverTest.assertProxyAlive(proxyTest2);
        NeodymiumWebDriverTest.assertProxyAlive(proxyAfterForTest1);
        NeodymiumWebDriverTest.assertProxyAlive(proxyAfterForTest2);

        proxyBeforeForTest1.stop();
        proxyBeforeForTest2.stop();
        proxyTest1.stop();
        proxyTest2.stop();
        proxyAfterForTest1.stop();
        proxyAfterForTest2.stop();

        NeodymiumWebDriverTest.assertProxyStopped(proxyBeforeForTest1);
        NeodymiumWebDriverTest.assertProxyStopped(proxyAfterForTest2);
        NeodymiumWebDriverTest.assertProxyStopped(proxyTest1);
        NeodymiumWebDriverTest.assertProxyStopped(proxyTest2);
        NeodymiumWebDriverTest.assertProxyStopped(proxyAfterForTest1);
        NeodymiumWebDriverTest.assertProxyStopped(proxyAfterForTest2);

        NeodymiumTest.deleteTempFile(tempConfigFile);
    }
}
