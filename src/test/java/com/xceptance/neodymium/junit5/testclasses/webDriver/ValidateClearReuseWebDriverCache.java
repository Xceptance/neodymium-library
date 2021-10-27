package com.xceptance.neodymium.junit5.testclasses.webDriver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import com.browserup.bup.BrowserUpProxy;
import com.xceptance.neodymium.junit5.tests.AbstractNeodymiumTest;
import com.xceptance.neodymium.junit5.tests.NeodymiumWebDriverTest;
import com.xceptance.neodymium.junit5.NeodymiumTest;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.common.browser.WebDriverCache;
import com.xceptance.neodymium.util.Neodymium;

/*
 * Validate that the web driver cache can be cleared manually. 
 * 
 * Scenario: 
 *   setup and use the driver 
 *   reuse the first driver
 *   create another driver
 *   validate that the two drivers are in the cache
 *   clear the cache
 *   validate the cache is empty
 */
public class ValidateClearReuseWebDriverCache
{
    private static WebDriver webDriver1;

    private static BrowserUpProxy proxy1;

    private static File tempConfigFile;

    @BeforeAll
    public static void beforeClass()
    {
        // set up a temporary neodymium.properties
        final String fileLocation = "config/temp-ValidateClearReuseWebDriverCache-neodymium.properties";
        tempConfigFile = new File("./" + fileLocation);
        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.webDriver.reuseDriver", "true");
        properties.put("neodymium.localproxy", "true");
        AbstractNeodymiumTest.writeMapToPropertiesFile(properties, tempConfigFile);
        ConfigFactory.setProperty(Neodymium.TEMPORARY_CONFIG_FILE_PROPERTY_NAME, "file:" + fileLocation);

        Assertions.assertNull(webDriver1);
        Assertions.assertNull(Neodymium.getDriver());
        Assertions.assertNull(proxy1);
        Assertions.assertNull(Neodymium.getLocalProxy());
    }

    @BeforeEach
    public void before()
    {
        if (webDriver1 == null)
        {
            webDriver1 = Neodymium.getDriver();
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
        else
        {
            Assertions.assertNotNull(Neodymium.getLocalProxy());
        }
        Assertions.assertNotNull(proxy1);
    }

    @NeodymiumTest
    @Browser("Chrome_headless")
    public void test1()
    {
        Assertions.assertEquals(webDriver1, Neodymium.getDriver());
        Assertions.assertNotNull(webDriver1);

        Assertions.assertEquals(proxy1, Neodymium.getLocalProxy());
        Assertions.assertNotNull(proxy1);
    }

    @NeodymiumTest
    @Browser("Chrome_headless")
    public void test2()
    {
        Assertions.assertEquals(webDriver1, Neodymium.getDriver());
        Assertions.assertNotNull(webDriver1);

        Assertions.assertEquals(proxy1, Neodymium.getLocalProxy());
        Assertions.assertNotNull(proxy1);
    }

    @NeodymiumTest
    @Browser("Chrome_1500x1000_headless")
    public void test3()
    {
        Assertions.assertNotEquals(webDriver1, Neodymium.getDriver());
        Assertions.assertNotNull(webDriver1);

        Assertions.assertNotEquals(proxy1, Neodymium.getLocalProxy());
        Assertions.assertNotNull(proxy1);
    }

    @AfterEach
    public void after()
    {
        NeodymiumWebDriverTest.assertWebDriverAlive(webDriver1);
        NeodymiumWebDriverTest.assertProxyAlive(proxy1);
    }

    @AfterAll
    public static void afterClass()
    {
        Assertions.assertEquals(2, WebDriverCache.instance.getWebDriverStateContainerCacheSize());
        WebDriverCache.quitCachedBrowsers();
        Assertions.assertEquals(0, WebDriverCache.instance.getWebDriverStateContainerCacheSize());

        NeodymiumWebDriverTest.assertWebDriverClosed(webDriver1);
        NeodymiumWebDriverTest.assertProxyStopped(proxy1);

        AbstractNeodymiumTest.deleteTempFile(tempConfigFile);
    }
}
