package com.xceptance.neodymium.testclasses.webDriver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.browserup.bup.BrowserUpProxy;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.tests.NeodymiumTest;
import com.xceptance.neodymium.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_1024x768")
public class ValidateKeepWebDriverOpenOnFailure
{
    private static WebDriver webDriver1;

    private static WebDriver webDriver2;

    private static WebDriver webDriver3;

    private static BrowserUpProxy proxy1;

    private static BrowserUpProxy proxy2;

    private static BrowserUpProxy proxy3;

    private static File tempConfigFile;

    @BeforeClass
    public static void beforeClass()
    {
        // set up a temporary neodymium.properties
        final String fileLocation = "config/temp-ValidateKeepWebDriverOpenOnFailure-neodymium.properties";
        tempConfigFile = new File("./" + fileLocation);
        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.webDriver.keepBrowserOpenOnFailure", "true");
        properties.put("neodymium.localproxy", "true");
        NeodymiumTest.writeMapToPropertiesFile(properties, tempConfigFile);
        ConfigFactory.setProperty(Neodymium.TEMPORARY_CONFIG_FILE_PROPERTY_NAME, "file:" + fileLocation);

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
        else if (webDriver3 == null)
        {
            webDriver3 = Neodymium.getDriver();
        }
        else
        {
            Assert.assertNotNull(Neodymium.getDriver());
        }
        Assert.assertNotNull(webDriver1);

        if (proxy1 == null)
        {
            proxy1 = Neodymium.getLocalProxy();
        }
        else if (proxy2 == null)
        {
            proxy2 = Neodymium.getLocalProxy();
        }
        else if (proxy3 == null)
        {
            proxy3 = Neodymium.getLocalProxy();
        }
        else
        {
            Assert.assertNotNull(Neodymium.getLocalProxy());
        }
        Assert.assertNotNull(proxy1);
    }

    @Test
    public void test1()
    {
        Assert.assertEquals(webDriver1, Neodymium.getDriver());
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver1);

        Assert.assertEquals(proxy1, Neodymium.getLocalProxy());
        NeodymiumWebDriverTest.assertProxyAlive(proxy1);
    }

    @Test
    public void test2()
    {
        Assert.assertNotEquals(webDriver1, webDriver2);
        Assert.assertEquals(webDriver2, Neodymium.getDriver());
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver2);

        Assert.assertNotEquals(proxy1, proxy2);
        Assert.assertEquals(proxy2, Neodymium.getLocalProxy());
        NeodymiumWebDriverTest.assertProxyStopped(proxy1);
        NeodymiumWebDriverTest.assertProxyAlive(proxy2);

        Selenide.$("#cantFindMe").should(Condition.exist);
    }

    @Test
    public void test3()
    {
        Assert.assertNotEquals(webDriver1, webDriver2);
        Assert.assertNotEquals(webDriver2, webDriver3);
        Assert.assertNotEquals(webDriver1, webDriver3);
        Assert.assertEquals(webDriver3, Neodymium.getDriver());
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver2);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver3);

        Assert.assertNotEquals(proxy1, proxy2);
        Assert.assertNotEquals(proxy2, proxy3);
        Assert.assertNotEquals(proxy1, proxy3);
        Assert.assertEquals(proxy3, Neodymium.getLocalProxy());
        NeodymiumWebDriverTest.assertProxyStopped(proxy1);
        NeodymiumWebDriverTest.assertProxyAlive(proxy2);
        NeodymiumWebDriverTest.assertProxyAlive(proxy3);
    }

    @AfterClass
    public static void afterClass()
    {
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver2);
        webDriver2.quit();
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver1);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver2);
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver3);

        NeodymiumWebDriverTest.assertProxyAlive(proxy2);
        proxy2.stop();
        NeodymiumWebDriverTest.assertProxyStopped(proxy1);
        NeodymiumWebDriverTest.assertProxyStopped(proxy2);
        NeodymiumWebDriverTest.assertProxyStopped(proxy3);

        NeodymiumTest.deleteTempFile(tempConfigFile);
    }
}
