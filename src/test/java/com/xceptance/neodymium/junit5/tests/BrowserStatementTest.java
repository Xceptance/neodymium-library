package com.xceptance.neodymium.junit5.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;

import com.xceptance.neodymium.junit5.testclasses.browser.classonly.ClassBrowserSuppressed;
import com.xceptance.neodymium.junit5.testclasses.browser.classonly.ClassBrowserSuppressedNoBrowserAnnotation;
import com.xceptance.neodymium.junit5.testclasses.browser.classonly.OneClassBrowserOneMethod;
import com.xceptance.neodymium.junit5.testclasses.browser.classonly.TwoClassBrowserOneMethod;
import com.xceptance.neodymium.junit5.testclasses.browser.classonly.TwoSameClassBrowserOneMethod;
import com.xceptance.neodymium.junit5.testclasses.browser.methodonly.MethodBrowserSuppressNoBrowserAnnotation;
import com.xceptance.neodymium.junit5.testclasses.browser.methodonly.OneBrowserOneMethodBrowserSuppressed;
import com.xceptance.neodymium.junit5.testclasses.browser.mixed.ClassAndMethodSameBrowserOneMethod;
import com.xceptance.neodymium.common.browser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.common.browser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.util.Neodymium;

public class BrowserStatementTest extends AbstractNeodymiumTest
{
    private static MultibrowserConfiguration browserConfig;

    @BeforeAll
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

        properties.put("browserprofile.testEnvironmentFlags2.name", "Test Environment Browser");
        properties.put("browserprofile.testEnvironmentFlags2.browser", "chrome");
        properties.put("browserprofile.testEnvironmentFlags2.idleTimeout", "1234");
        properties.put("browserprofile.testEnvironmentFlags2.maxDuration", "5678");
        properties.put("browserprofile.testEnvironmentFlags2.seleniumVersion", "3.1234");
        properties.put("browserprofile.testEnvironmentFlags2.screenResolution", "800x600");
        properties.put("browserprofile.testEnvironmentFlags2.platform", "Windows 7");
        properties.put("browserprofile.testEnvironmentFlags2.platformName", "Windows 10");
        properties.put("browserprofile.testEnvironmentFlags2.deviceName", "MyDevice");
        properties.put("browserprofile.testEnvironmentFlags2.deviceOrientation", "portrait");

        File tempConfigFile = File.createTempFile("browser", "", new File("./config/"));
        tempFiles.add(tempConfigFile);
        writeMapToPropertiesFile(properties, tempConfigFile);

        MultibrowserConfiguration.clearAllInstances();
        browserConfig = MultibrowserConfiguration.getInstance(tempConfigFile.getPath());
    }

    @BeforeEach
    public void setJUnitViewModeFlat()
    {
        Neodymium.configuration().setProperty("neodymium.junit.viewmode", "flat");
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
          "second()"
        };
        checkDescription(OneBrowserOneMethodBrowserSuppressed.class, expected);
    }

    @Test
    public void testMultibrowserConfiguration() throws Throwable
    {
        // define one chrome browser then validate the parsed multi-browser configuration
        // TODO: we need more of this tests...
        Map<String, BrowserConfiguration> browserProfiles = browserConfig.getBrowserProfiles();
        // Assertions.assertEquals(1, browserProfiles.size());

        checkChrome(browserProfiles.get("chrome"));
        checkMultiChrome(browserProfiles.get("multiChrome"));
        checkFirefox(browserProfiles.get("firefox"));
        checkMultiFirefox(browserProfiles.get("multiFirefox"));
        checkTestEnvironment(browserProfiles.get("testEnvironmentFlags"));
        checkTestEnvironment2(browserProfiles.get("testEnvironmentFlags2"));
    }

    private void checkChrome(BrowserConfiguration config)
    {
        Assertions.assertNotNull(config);
        Assertions.assertEquals("chrome", config.getConfigTag());
        Assertions.assertEquals("Google Chrome", config.getName());
        Assertions.assertEquals("local", config.getTestEnvironment());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assertions.assertEquals("chrome", testCapabilities.getBrowserName());
        Assertions.assertEquals(true, testCapabilities.getCapability(CapabilityType.ACCEPT_INSECURE_CERTS));
        LinkedList<String> list = new LinkedList<>();
        list.add("headless");
        Assertions.assertEquals(list, config.getArguments());
    }

    private void checkMultiChrome(BrowserConfiguration config)
    {
        Assertions.assertNotNull(config);
        Assertions.assertEquals("multiChrome", config.getConfigTag());
        Assertions.assertEquals("Multi Argument Chrome", config.getName());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assertions.assertEquals("chrome", testCapabilities.getBrowserName());
        LinkedList<String> list = new LinkedList<>();
        list.add("-crash-test");
        list.add("-window-position=0,0");
        list.add("-window-size=1024,768");
        Assertions.assertEquals(list, config.getArguments());
    }

    private void checkFirefox(BrowserConfiguration config)
    {
        Assertions.assertNotNull(config);
        Assertions.assertEquals("firefox", config.getConfigTag());
        Assertions.assertEquals("Mozilla Firefox", config.getName());
        Assertions.assertEquals(null, config.getTestEnvironment());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assertions.assertEquals("firefox", testCapabilities.getBrowserName());
        LinkedList<String> list = new LinkedList<>();
        list.add("headless");
        Assertions.assertEquals(list, config.getArguments());
    }

    private void checkMultiFirefox(BrowserConfiguration config)
    {
        Assertions.assertNotNull(config);
        Assertions.assertEquals("multiFirefox", config.getConfigTag());
        Assertions.assertEquals("Multi Argument Firefox", config.getName());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assertions.assertEquals("firefox", testCapabilities.getBrowserName());
        LinkedList<String> list = new LinkedList<>();
        list.add("-headless");
        list.add("-width=1024");
        list.add("-height=768");
        Assertions.assertEquals(list, config.getArguments());
    }

    private void checkTestEnvironment(BrowserConfiguration config)
    {
        Assertions.assertNotNull(config);
        Assertions.assertEquals("testEnvironmentFlags", config.getConfigTag());
        Assertions.assertEquals("Test Environment Browser", config.getName());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assertions.assertEquals("chrome", testCapabilities.getBrowserName());
        Assertions.assertEquals(1234, testCapabilities.getCapability("idleTimeout"));
        Assertions.assertEquals(1234, testCapabilities.getCapability("idletimeout"));
        Assertions.assertEquals(5678, testCapabilities.getCapability("maxDuration"));
        Assertions.assertEquals(5678, testCapabilities.getCapability("maxduration"));
        Assertions.assertEquals("3.1234", testCapabilities.getCapability("seleniumVersion"));
        Assertions.assertEquals("3.1234", testCapabilities.getCapability("selenium-version"));
        Assertions.assertEquals("800x600", testCapabilities.getCapability("screenResolution"));
        Assertions.assertEquals("800x600", testCapabilities.getCapability("screen-resolution"));
        Assertions.assertEquals(Platform.VISTA, testCapabilities.getCapability("platform"));
        Assertions.assertEquals("Windows 10", testCapabilities.getCapability("platformName"));
        Assertions.assertEquals("MyDevice", testCapabilities.getCapability("deviceName"));
        Assertions.assertEquals("landscape", testCapabilities.getCapability("deviceOrientation"));
        Assertions.assertEquals("landscape", testCapabilities.getCapability("orientation"));
    }

    private void checkTestEnvironment2(BrowserConfiguration config)
    {
        Assertions.assertNotNull(config);
        Assertions.assertEquals("testEnvironmentFlags2", config.getConfigTag());
        Assertions.assertEquals("Test Environment Browser", config.getName());
        MutableCapabilities testCapabilities = config.getCapabilities();
        Assertions.assertEquals("chrome", testCapabilities.getBrowserName());
        Assertions.assertEquals(1234, testCapabilities.getCapability("idleTimeout"));
        Assertions.assertEquals(1234, testCapabilities.getCapability("idletimeout"));
        Assertions.assertEquals(5678, testCapabilities.getCapability("maxDuration"));
        Assertions.assertEquals(5678, testCapabilities.getCapability("maxduration"));
        Assertions.assertEquals("3.1234", testCapabilities.getCapability("seleniumVersion"));
        Assertions.assertEquals("3.1234", testCapabilities.getCapability("selenium-version"));
        Assertions.assertEquals("800x600", testCapabilities.getCapability("screenResolution"));
        Assertions.assertEquals("800x600", testCapabilities.getCapability("screen-resolution"));
        Assertions.assertEquals(Platform.VISTA, testCapabilities.getCapability("platform"));
        Assertions.assertEquals("Windows 10", testCapabilities.getCapability("platformName"));
        Assertions.assertEquals("MyDevice", testCapabilities.getCapability("deviceName"));
        Assertions.assertEquals("portrait", testCapabilities.getCapability("deviceOrientation"));
        Assertions.assertEquals(null, testCapabilities.getCapability("orientation"));
    }
}
