package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;

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
import com.xceptance.neodymium.util.Neodymium;

public class BrowserStatementTest extends NeodymiumTest
{
    private static MultibrowserConfiguration browserConfig;

    @BeforeClass
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties = new HashMap<>();

        properties.put("browserprofile.chrome.name", "Google Chrome");
        properties.put("browserprofile.chrome.browser", "chrome");
        properties.put("browserprofile.chrome.testEnvironment", "local");
        properties.put("browserprofile.chrome.acceptInsecureCertificates", "true");
        properties.put("browserprofile.chrome.arguments", "headless");

        properties.put("browserprofile.firefox.name", "Mozilla Firefox");
        properties.put("browserprofile.firefox.browser", "firefox");
        properties.put("browserprofile.firefox.arguments", "headless");

        properties.put("browserprofile.multiFirefox.name", "Multi Argument Firefox");
        properties.put("browserprofile.multiFirefox.browser", "firefox");
        properties.put("browserprofile.multiFirefox.arguments", "-headless ; -width=1024; -height=768 ");

        properties.put("browserprofile.multiChrome.name", "Multi Argument Chrome");
        properties.put("browserprofile.multiChrome.browser", "chrome");
        properties.put("browserprofile.multiChrome.arguments", " -crash-test ; -window-position=0,0 ;-window-size=1024,768 ");

        properties.put("browserprofile.testEnvironmentFlags.name", "Test Environment Browser");
        properties.put("browserprofile.testEnvironmentFlags.browser", "chrome");
        properties.put("browserprofile.testEnvironmentFlags.idleTimeout", "1234");
        properties.put("browserprofile.testEnvironmentFlags.maxDuration", "5678");
        properties.put("browserprofile.testEnvironmentFlags.seleniumVersion", "3.1234");
        properties.put("browserprofile.testEnvironmentFlags.screenResolution", "800x600");
        properties.put("browserprofile.testEnvironmentFlags.platform", "Windows 7");
        properties.put("browserprofile.testEnvironmentFlags.platformName", "Windows 10");
        properties.put("browserprofile.testEnvironmentFlags.deviceName", "MyDevice");
        properties.put("browserprofile.testEnvironmentFlags.deviceOrientation", "portrait");
        properties.put("browserprofile.testEnvironmentFlags.orientation", "landscape");

        File tempConfigFile = File.createTempFile("browser", "", new File("./config/"));
        tempFiles.add(tempConfigFile);
        writeMapToPropertiesFile(properties, tempConfigFile);

        MultibrowserConfiguration.clearAllInstances();
        browserConfig = MultibrowserConfiguration.getInstance(tempConfigFile.getPath());
    }

    @Before
    public void setJUnitViewModeFlat()
    {
        Neodymium.configuration().setProperty("neodymium.junit.viewmode", "flat");
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
          "first",
          "second"
        };
        checkDescription(OneBrowserOneMethodBrowserSuppressed.class, expected);
    }

    @Test
    public void testAnnotationsCorrect() throws Throwable
    {
        Map<String, List<String>> expectedAnnotations = new HashMap<String, List<String>>();
        List<String> anno1 = new ArrayList<String>();
        anno1.add("@org.junit.Test(timeout=0, expected=org.junit.Test$None.class)");
        anno1.add("@com.xceptance.neodymium.module.statement.browser.multibrowser.Browser(value=\"chrome\")");
        anno1.add("@com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers()");
        expectedAnnotations.put("first", anno1);

        List<String> anno2 = new ArrayList<String>();
        anno2.add("@org.junit.Test(timeout=0, expected=org.junit.Test$None.class)");
        anno2.add("@org.junit.Ignore(value=\"This should be visible\")");
        expectedAnnotations.put("second", anno2);

        checkAnnotations(OneBrowserOneMethodBrowserSuppressed.class, expectedAnnotations);
    }

    @Test
    public void testMultibrowserConfiguration() throws Throwable
    {
        // define one chrome browser then validate the parsed multi-browser configuration
        // TODO: we need more of this tests...
        Map<String, BrowserConfiguration> browserProfiles = browserConfig.getBrowserProfiles();
        // Assert.assertEquals(1, browserProfiles.size());

        checkChrome(browserProfiles.get("chrome"));
        checkMultiChrome(browserProfiles.get("multiChrome"));
        checkFirefox(browserProfiles.get("firefox"));
        checkMultiFirefox(browserProfiles.get("multiFirefox"));
        checkTestEnvironment(browserProfiles.get("testEnvironmentFlags"));
    }

    private void checkChrome(BrowserConfiguration config)
    {
        Assert.assertNotNull(config);
        Assert.assertEquals("chrome", config.getConfigTag());
        Assert.assertEquals("Google Chrome", config.getName());
        Assert.assertEquals("local", config.getTestEnvironment());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assert.assertEquals("chrome", testCapabilities.getBrowserName());
        Assert.assertEquals(true, testCapabilities.getCapability(CapabilityType.ACCEPT_INSECURE_CERTS));
        LinkedList<String> list = new LinkedList<>();
        list.add("headless");
        Assert.assertEquals(list, config.getArguments());
    }

    private void checkMultiChrome(BrowserConfiguration config)
    {
        Assert.assertNotNull(config);
        Assert.assertEquals("multiChrome", config.getConfigTag());
        Assert.assertEquals("Multi Argument Chrome", config.getName());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assert.assertEquals("chrome", testCapabilities.getBrowserName());
        LinkedList<String> list = new LinkedList<>();
        list.add("-crash-test");
        list.add("-window-position=0,0");
        list.add("-window-size=1024,768");
        Assert.assertEquals(list, config.getArguments());
    }

    private void checkFirefox(BrowserConfiguration config)
    {
        Assert.assertNotNull(config);
        Assert.assertEquals("firefox", config.getConfigTag());
        Assert.assertEquals("Mozilla Firefox", config.getName());
        Assert.assertEquals(null, config.getTestEnvironment());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assert.assertEquals("firefox", testCapabilities.getBrowserName());
        LinkedList<String> list = new LinkedList<>();
        list.add("headless");
        Assert.assertEquals(list, config.getArguments());
    }

    private void checkMultiFirefox(BrowserConfiguration config)
    {
        Assert.assertNotNull(config);
        Assert.assertEquals("multiFirefox", config.getConfigTag());
        Assert.assertEquals("Multi Argument Firefox", config.getName());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assert.assertEquals("firefox", testCapabilities.getBrowserName());
        LinkedList<String> list = new LinkedList<>();
        list.add("-headless");
        list.add("-width=1024");
        list.add("-height=768");
        Assert.assertEquals(list, config.getArguments());
    }

    private void checkTestEnvironment(BrowserConfiguration config)
    {
        Assert.assertNotNull(config);
        Assert.assertEquals("testEnvironmentFlags", config.getConfigTag());
        Assert.assertEquals("Test Environment Browser", config.getName());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assert.assertEquals("chrome", testCapabilities.getBrowserName());
        Assert.assertEquals(1234, testCapabilities.getCapability("idleTimeout"));
        Assert.assertEquals(1234, testCapabilities.getCapability("idletimeout"));
        Assert.assertEquals(5678, testCapabilities.getCapability("maxDuration"));
        Assert.assertEquals(5678, testCapabilities.getCapability("maxduration"));
        Assert.assertEquals("3.1234", testCapabilities.getCapability("seleniumVersion"));
        Assert.assertEquals("3.1234", testCapabilities.getCapability("selenium-version"));
        Assert.assertEquals("800x600", testCapabilities.getCapability("screenResolution"));
        Assert.assertEquals("800x600", testCapabilities.getCapability("screen-resolution"));
        Assert.assertEquals(Platform.VISTA, testCapabilities.getCapability("platform"));
        Assert.assertEquals("Windows 10", testCapabilities.getCapability("platformName"));
        Assert.assertEquals("MyDevice", testCapabilities.getCapability("deviceName"));
        Assert.assertEquals("portrait", testCapabilities.getCapability("deviceOrientation"));
        Assert.assertEquals("landscape", testCapabilities.getCapability("orientation"));
    }
}
