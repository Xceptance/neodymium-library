package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.testclasses.browser.classonly.ClassBrowserSuppressed;
import com.xceptance.neodymium.testclasses.browser.classonly.ClassBrowserSuppressedNoBrowserAnnotation;
import com.xceptance.neodymium.testclasses.browser.classonly.EmptyBrowser;
import com.xceptance.neodymium.testclasses.browser.classonly.OneClassBrowserOneMethod;
import com.xceptance.neodymium.testclasses.browser.classonly.TwoClassBrowserOneMethod;
import com.xceptance.neodymium.testclasses.browser.classonly.TwoSameClassBrowserOneMethod;
import com.xceptance.neodymium.testclasses.browser.methodonly.MethodBrowserSuppressNoBrowserAnnotation;
import com.xceptance.neodymium.testclasses.browser.methodonly.OneBrowserOneMethodBrowserSuppressed;
import com.xceptance.neodymium.testclasses.browser.mixed.ClassAndMethodSameBrowserOneMethod;
import com.xceptance.neodymium.util.Context;

public class BrowserStatementTest extends NeodymiumTest
{
    private static MultibrowserConfiguration browserConfig;

    @Before
    public void setJUnitViewModeFlat()
    {
        Context.get().configuration.setProperty("junit.viewmode", "flat");
    }

    @BeforeClass
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties = new HashMap<>();

        properties.put("browserprofile.chrome.name", "Google Chrome");
        properties.put("browserprofile.chrome.browser", "chrome");
        properties.put("browserprofile.chrome.testEnvironment", "local");
        properties.put("browserprofile.chrome.acceptInsecureCertificates", "true");

        properties.put("browserprofile.firefox.name", "Mozilla Firefox");
        properties.put("browserprofile.firefox.browser", "firefox");

        File tempConfigFile = File.createTempFile("browser", "", new File("./config/"));
        tempFiles.add(tempConfigFile);
        writeMapToPropertiesFile(properties, tempConfigFile);

        MultibrowserConfiguration.clearAllInstances();
        browserConfig = MultibrowserConfiguration.getInstance(tempConfigFile.getPath());
    }

    @Test
    public void testEmptyBrowser()
    {
        // an empty browser tag (@Browser({""})) should raise an error
        Result result = JUnitCore.runClasses(EmptyBrowser.class);
        checkFail(result, 1, 0, 1, "java.lang.IllegalArgumentException: Can not find browser configuration with tag: ");
    }

    @Test
    public void testTestBrowser() throws Throwable
    {
        // one test method and one browser annotated on class
        String[] expected = new String[]
        {
          "first :: Browser chrome"
        };
        checkDescription(OneClassBrowserOneMethod.class, expected);
    }

    @Test
    public void testClassBrowserSuppressed() throws Throwable
    {
        //
        String[] expected = new String[]
        {
          "first"
        };
        checkDescription(ClassBrowserSuppressed.class, expected);
    }

    @Test
    public void testTwoClassBrowserOneMethod() throws Throwable
    {
        String[] expected = new String[]
        {
          "first :: Browser chrome", "first :: Browser firefox"
        };
        checkDescription(TwoClassBrowserOneMethod.class, expected);
    }

    @Test
    public void testTwoSameClassBrowserOneMethod() throws Throwable
    {
        // two browser annotated on class, both have same value
        String[] expected = new String[]
        {
          "first :: Browser chrome"
        };
        checkDescription(TwoSameClassBrowserOneMethod.class, expected);
    }

    @Test
    public void testClassAndMethodSameBrowserOneMethod() throws Throwable
    {
        // same browser annotated on class and method
        String[] expected = new String[]
        {
          "first :: Browser chrome"
        };
        checkDescription(ClassAndMethodSameBrowserOneMethod.class, expected);
    }

    @Test
    public void testClassBrowserSuppressedNoBrowserAnnotation() throws Throwable
    {
        // no browser definition but browser suppressed on class
        String[] expected = new String[]
        {
          "first"
        };
        checkDescription(ClassBrowserSuppressedNoBrowserAnnotation.class, expected);
    }

    @Test
    public void testName() throws Throwable
    {
        // no browser definition but browser suppressed on method
        String[] expected = new String[]
        {
          "first"
        };
        checkDescription(MethodBrowserSuppressNoBrowserAnnotation.class, expected);
    }

    @Test
    public void testOneBrowserOneMethodBrowserSuppressed() throws Throwable
    {
        // a browser definition on a method and a suppress browser
        String[] expected = new String[]
        {
          "first"
        };
        checkDescription(OneBrowserOneMethodBrowserSuppressed.class, expected);
    }

    @Test
    public void testMultibrowserConfiguration() throws Throwable
    {
        // define one chrome browser then validate the parsed multi-browser configuration
        // TODO: we need more of this tests...
        Map<String, BrowserConfiguration> browserProfiles = browserConfig.getBrowserProfiles();
        // Assert.assertEquals(1, browserProfiles.size());

        checkChrome(browserProfiles.get("chrome"));
        checkFirefox(browserProfiles.get("firefox"));
    }

    public void checkChrome(BrowserConfiguration config)
    {
        Assert.assertNotNull(config);
        Assert.assertEquals("chrome", config.getConfigTag());
        Assert.assertEquals("Google Chrome", config.getName());
        Assert.assertEquals("local", config.getTestEnvironment());
        DesiredCapabilities testCapabilities = config.getCapabilities();
        Assert.assertEquals("chrome", testCapabilities.getBrowserName());
        Assert.assertEquals(true, testCapabilities.acceptInsecureCerts());
    }

    public void checkFirefox(BrowserConfiguration config)
    {
        Assert.assertNotNull(config);
        Assert.assertEquals("firefox", config.getConfigTag());
        Assert.assertEquals("Mozilla Firefox", config.getName());
        Assert.assertEquals(null, config.getTestEnvironment());
        DesiredCapabilities testCapabilities = config.getCapabilities();
        Assert.assertEquals("firefox", testCapabilities.getBrowserName());
    }
}
