package com.xceptance.neodymium.junit5.testclasses.webDriver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import com.browserup.bup.BrowserUpProxy;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.junit5.tests.AbstractNeodymiumTest;
import com.xceptance.neodymium.junit5.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.junit5.NeodymiumTest;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.common.browser.WebDriverCache;
import com.xceptance.neodymium.common.browser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.util.Neodymium;

/*
 * Validate that the web driver is kept open after an error occurred.
 * Validate that the web driver is not reused.
 * Attention: this test needs to use browsers that are not headless.
 */
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

    private static File tempBrowserConfigFile;

    @BeforeAll
    public static void beforeClass()
    {
        // set up a temporary neodymium.properties
        final String fileLocation = "config/temp-ValidateKeepWebDriverOpenOnFailure-neodymium.properties";
        tempConfigFile = new File("./" + fileLocation);
        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.webDriver.keepBrowserOpenOnFailure", "true");
        properties.put("neodymium.localproxy", "true");
        AbstractNeodymiumTest.writeMapToPropertiesFile(properties, tempConfigFile);
        ConfigFactory.setProperty(Neodymium.TEMPORARY_CONFIG_FILE_PROPERTY_NAME, "file:" + fileLocation);

        Map<String, String> browserProperties = new HashMap<>();
        browserProperties.put("browserprofile.Chrome_1024x768.headless", "false");

        tempBrowserConfigFile = new File("./config/temp-ValidateKeepWebDriverOpenOnFailure-browser.properties");
        AbstractNeodymiumTest.writeMapToPropertiesFile(properties, tempConfigFile);

        // this line is important as we initialize the config from the temporary file we created above
        MultibrowserConfiguration.clearAllInstances();
        MultibrowserConfiguration.getInstance(tempConfigFile.getPath());

        Assertions.assertNull(webDriver1);
        Assertions.assertNull(Neodymium.getDriver());
    }

    @BeforeEach
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
            Assertions.assertNotNull(Neodymium.getDriver());
        }
        Assertions.assertNotNull(webDriver1);

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
            Assertions.assertNotNull(Neodymium.getLocalProxy());
        }
        Assertions.assertNotNull(proxy1);
    }

    @NeodymiumTest
    public void test1()
    {
        Assertions.assertEquals(webDriver1, Neodymium.getDriver());
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver1);

        Assertions.assertEquals(proxy1, Neodymium.getLocalProxy());
        NeodymiumWebDriverTest.assertProxyAlive(proxy1);
    }

    @NeodymiumTest
    public void test2()
    {
        Assertions.assertNotEquals(webDriver1, webDriver2);
        Assertions.assertEquals(webDriver2, Neodymium.getDriver());
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver2);

        Assertions.assertNotEquals(proxy1, proxy2);
        Assertions.assertEquals(proxy2, Neodymium.getLocalProxy());
        NeodymiumWebDriverTest.assertProxyStopped(proxy1);
        NeodymiumWebDriverTest.assertProxyAlive(proxy2);

        // Let condition fail so that the WebDriver/browser is kept open
        Selenide.$("#cantFindMe").should(Condition.exist);
    }

    @NeodymiumTest
    public void test3()
    {
        Assertions.assertNotEquals(webDriver1, webDriver2);
        Assertions.assertNotEquals(webDriver2, webDriver3);
        Assertions.assertNotEquals(webDriver1, webDriver3);
        Assertions.assertEquals(webDriver3, Neodymium.getDriver());
        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver1);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver2);
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver3);

        Assertions.assertNotEquals(proxy1, proxy2);
        Assertions.assertNotEquals(proxy2, proxy3);
        Assertions.assertNotEquals(proxy1, proxy3);
        Assertions.assertEquals(proxy3, Neodymium.getLocalProxy());
        NeodymiumWebDriverTest.assertProxyStopped(proxy1);
        NeodymiumWebDriverTest.assertProxyAlive(proxy2);
        NeodymiumWebDriverTest.assertProxyAlive(proxy3);
    }

    @AfterAll
    public static void afterClass()
    {
        Assertions.assertEquals(0, WebDriverCache.instance.getWebDriverStateContainerCacheSize());

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

        AbstractNeodymiumTest.deleteTempFile(tempConfigFile);
        AbstractNeodymiumTest.deleteTempFile(tempBrowserConfigFile);
    }
}
