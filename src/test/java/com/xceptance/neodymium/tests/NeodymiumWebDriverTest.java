package com.xceptance.neodymium.tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import com.xceptance.neodymium.testclasses.webDriver.LocalProxyTrustAllServers;
import com.xceptance.neodymium.testclasses.webDriver.LocalProxyUsingProvidedCertificates;
import com.xceptance.neodymium.testclasses.webDriver.LocalProxyUsingProvidedCertificatesRuntimeException;
import com.xceptance.neodymium.testclasses.webDriver.LocalProxyUsingSelfCreatedCertificates;
import com.xceptance.neodymium.testclasses.webDriver.ValidateClearReuseWebDriverCache;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepBrowserOpenAnnotationClassDoesntInterfereWithConfig;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepBrowserOpenAnnotationClassOverridesConfig;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepBrowserOpenAnnotationMethodDoesntInterfereWithConfig;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepBrowserOpenAnnotationMethodOverridesClass;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepBrowserOpenAnnotationMethodOverridesConfig;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepBrowserOpenOnFailureAnnotationClassDoesntInterfereWithConfig;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepBrowserOpenOnFailureAnnotationClassOverridesConfig;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepBrowserOpenOnFailureAnnotationMethodDoesntInterfereWithConfig;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepBrowserOpenOnFailureAnnotationMethodOverridesConfig;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepWebDriverOpen;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepWebDriverOpenOnFailure;
import com.xceptance.neodymium.testclasses.webDriver.ValidatePreventReuseWebDriver;
import com.xceptance.neodymium.testclasses.webDriver.ValidateReuseWebDriver;
import com.xceptance.neodymium.testclasses.webDriver.ValidateWebDriverClosed;
import com.xceptance.neodymium.testclasses.webDriver.ValidateWebDriverMaxReuse;
import com.xceptance.neodymium.testclasses.webDriver.ValidateWebDriverMaxReuseWithTwoWebDrivers;
import com.xceptance.neodymium.testclasses.webDriver.ValidateWebDriverReuseCounter;

public class NeodymiumWebDriverTest extends NeodymiumTest
{
    @Test
    public void testValidateWebDriverClosed()
    {
        Result result = JUnitCore.runClasses(ValidateWebDriverClosed.class);
        checkPass(result, 2, 0);
    }

    @Test
    public void testValidateReuseWebDriver()
    {
        Result result = JUnitCore.runClasses(ValidateReuseWebDriver.class);
        checkPass(result, 2, 0);
    }

    @Test
    public void testValidateWebDriverReuseCounter()
    {
        Result result = JUnitCore.runClasses(ValidateWebDriverReuseCounter.class);
        checkPass(result, 6, 0);
    }

    @Test
    public void testValidateWebDriverMaxReuse()
    {
        Result result = JUnitCore.runClasses(ValidateWebDriverMaxReuse.class);
        checkPass(result, 5, 0);
    }

    @Test
    public void testValidateWebDriverMaxReuseWithTwoWebDrivers()
    {
        Result result = JUnitCore.runClasses(ValidateWebDriverMaxReuseWithTwoWebDrivers.class);
        checkPass(result, 9, 0);
    }

    @Test
    public void testValidateClearReuseWebDriverCache()
    {
        Result result = JUnitCore.runClasses(ValidateClearReuseWebDriverCache.class);
        checkPass(result, 3, 0);
    }

    @Test
    public void testValidatePreventReuseWebDriver()
    {
        Result result = JUnitCore.runClasses(ValidatePreventReuseWebDriver.class);
        checkPass(result, 3, 0);
    }

    @Test
    public void testValidateKeepWebDriverOpen()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepWebDriverOpen.class);
        checkFail(result, 3, 0, 1);
    }

    @Test
    public void testValidateKeepWebDriverOpenOnFailure()
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.webDriver.keepBrowserOpenOnFailure", "true");
        properties.put("neodymium.webDriver.keepBrowserOpen", "false");
        properties.put("neodymium.localproxy", "true");

        addPropertiesForTest("temp-ValidateKeepWebDriverOpenOnFailure-neodymium.properties", properties);

        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepWebDriverOpenOnFailure.class);
        checkFail(result, 3, 0, 1);
    }


    @Test
    public void testValidateKeepBrowserOpenAnnotationClassDoesntInterfereWithConfig()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepBrowserOpenAnnotationClassDoesntInterfereWithConfig.class);
        checkFail(result, 3, 0, 1);
    }

    @Test
    public void testValidateKeepBrowserOpenOnFailureAnnotationClassDoesntInterfereWithConfig()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepBrowserOpenOnFailureAnnotationClassDoesntInterfereWithConfig.class);
        checkFail(result, 3, 0, 1);
    }

    @Test
    public void testValidateKeepBrowserOpenAnnotationMethodDoesntInterfereWithConfig()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepBrowserOpenAnnotationMethodDoesntInterfereWithConfig.class);
        checkFail(result, 3, 0, 1);
    }

    @Test
    public void testValidateKeepBrowserOpenOnFailureAnnotationMethodDoesntInterfereWithConfig()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepBrowserOpenOnFailureAnnotationMethodDoesntInterfereWithConfig.class);
        checkFail(result, 3, 0, 1);
    }

    @Test
    public void testValidateKeepBrowserOpenAnnotationClassOverridesConfig()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepBrowserOpenAnnotationClassOverridesConfig.class);
        checkFail(result, 3, 0, 1);
    }

    @Test
    public void testValidateKeepBrowserOpenOnFailureAnnotationClassOverridesConfig()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepBrowserOpenOnFailureAnnotationClassOverridesConfig.class);
        checkFail(result, 3, 0, 1);
    }

    @Test
    public void testValidateKeepBrowserOpenAnnotationMethodOverridesConfig()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepBrowserOpenAnnotationMethodOverridesConfig.class);
        checkFail(result, 3, 0, 1);
    }

    @Test
    public void testValidateKeepBrowserOpenOnFailureAnnotationMethodOverridesConfig()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepBrowserOpenOnFailureAnnotationMethodOverridesConfig.class);
        checkFail(result, 3, 0, 1);
    }

    @Test
    public void testValidateKeepBrowserOpenAnnotationMethodOverridesClass()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateKeepBrowserOpenAnnotationMethodOverridesClass.class);
        checkFail(result, 3, 0, 1);
    }

    @Test
    public void testLocalProxyTrustAllServers()
    {
        Result result = JUnitCore.runClasses(LocalProxyTrustAllServers.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testLocalProxyUsingSelfCreatedCertificates()
    {
        Result result = JUnitCore.runClasses(LocalProxyUsingSelfCreatedCertificates.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testLocalProxyUsingProvidedCertificates()
    {
        Result result = JUnitCore.runClasses(LocalProxyUsingProvidedCertificates.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testLocalProxyUsingProvidedCertificatesRuntimeException()
    {
        Result result = JUnitCore.runClasses(LocalProxyUsingProvidedCertificatesRuntimeException.class);
        checkFail(result, 1, 0, 1,
                  "The local proxy certificate isn't fully configured. Please check: certificate archive type, certificate archive file, certificate name and certificate password.");
    }

    public static void assertWebDriverClosed(WebDriver webDriver)
    {
        Assert.assertNotNull(webDriver);
        RemoteWebDriver driver = (RemoteWebDriver) ((EventFiringWebDriver) webDriver).getWrappedDriver();
        Assert.assertNull(driver.getSessionId());
    }

    public static void assertWebDriverAlive(WebDriver webDriver)
    {
        Assert.assertNotNull(webDriver);
        RemoteWebDriver driver = (RemoteWebDriver) ((EventFiringWebDriver) webDriver).getWrappedDriver();
        Assert.assertNotNull(driver.getSessionId());
    }

    public static void assertProxyStopped(BrowserUpProxy proxy)
    {
        Assert.assertNotNull(proxy);
        Assert.assertTrue(proxy.isStarted());
        Assert.assertTrue(((BrowserUpProxyServer) proxy).isStopped());
    }

    public static void assertProxyAlive(BrowserUpProxy proxy)
    {
        Assert.assertNotNull(proxy);
        Assert.assertTrue(proxy.isStarted());
        Assert.assertFalse(((BrowserUpProxyServer) proxy).isStopped());
    }
}
