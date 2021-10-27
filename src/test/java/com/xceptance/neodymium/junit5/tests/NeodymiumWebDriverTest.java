package com.xceptance.neodymium.junit5.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import com.xceptance.neodymium.junit5.testclasses.webDriver.LocalProxyTrustAllServers;
import com.xceptance.neodymium.junit5.testclasses.webDriver.LocalProxyUsingProvidedCertificates;
import com.xceptance.neodymium.junit5.testclasses.webDriver.LocalProxyUsingProvidedCertificatesRuntimeException;
import com.xceptance.neodymium.junit5.testclasses.webDriver.LocalProxyUsingSelfCreatedCertificates;
import com.xceptance.neodymium.junit5.testclasses.webDriver.ValidateClearReuseWebDriverCache;
import com.xceptance.neodymium.junit5.testclasses.webDriver.ValidateKeepWebDriverOpen;
import com.xceptance.neodymium.junit5.testclasses.webDriver.ValidateKeepWebDriverOpenOnFailure;
import com.xceptance.neodymium.junit5.testclasses.webDriver.ValidatePreventReuseWebDriver;
import com.xceptance.neodymium.junit5.testclasses.webDriver.ValidateReuseWebDriver;
import com.xceptance.neodymium.junit5.testclasses.webDriver.ValidateWebDriverClosed;
import com.xceptance.neodymium.junit5.testclasses.webDriver.ValidateWebDriverMaxReuse;
import com.xceptance.neodymium.junit5.testclasses.webDriver.ValidateWebDriverMaxReuseWithTwoWebDrivers;
import com.xceptance.neodymium.junit5.testclasses.webDriver.ValidateWebDriverReuseCounter;
import com.xceptance.neodymium.junit5.tests.utils.NeodymiumTestExecutionSummary;

public class NeodymiumWebDriverTest extends AbstractNeodymiumTest
{
    @Test
    public void testValidateWebDriverClosed()
    {
        NeodymiumTestExecutionSummary summary = run(ValidateWebDriverClosed.class);
        checkPass(summary, 2, 0);
    }

    @Test
    public void testValidateReuseWebDriver()
    {
        NeodymiumTestExecutionSummary summary = run(ValidateReuseWebDriver.class);
        checkPass(summary, 2, 0);
    }

    @Test
    public void testValidateWebDriverReuseCounter()
    {
        NeodymiumTestExecutionSummary summary = run(ValidateWebDriverReuseCounter.class);
        checkPass(summary, 6, 0);
    }

    @Test
    public void testValidateWebDriverMaxReuse()
    {
        NeodymiumTestExecutionSummary summary = run(ValidateWebDriverMaxReuse.class);
        checkPass(summary, 5, 0);
    }

    @Test
    public void testValidateWebDriverMaxReuseWithTwoWebDrivers()
    {
        NeodymiumTestExecutionSummary summary = run(ValidateWebDriverMaxReuseWithTwoWebDrivers.class);
        checkPass(summary, 9, 0);
    }

    @Test
    public void testValidateClearReuseWebDriverCache()
    {
        NeodymiumTestExecutionSummary summary = run(ValidateClearReuseWebDriverCache.class);
        checkPass(summary, 3, 0);
    }

    @Test
    public void testValidatePreventReuseWebDriver()
    {
        NeodymiumTestExecutionSummary summary = run(ValidatePreventReuseWebDriver.class);
        checkPass(summary, 3, 0);
    }

    @Test
    public void testValidateKeepWebDriverOpen()
    {
        // XVFB or a display needed
        NeodymiumTestExecutionSummary summary = run(ValidateKeepWebDriverOpen.class);
        checkPass(summary, 2, 0);
    }

    @Test
    public void testValidateKeepWebDriverOpenOnFailure()
    {
        // XVFB or a display needed
        NeodymiumTestExecutionSummary summary = run(ValidateKeepWebDriverOpenOnFailure.class);
        checkFail(summary, 3, 0, 1);
    }

    @Test
    public void testLocalProxyTrustAllServers()
    {
        NeodymiumTestExecutionSummary summary = run(LocalProxyTrustAllServers.class);
        checkPass(summary, 1, 0);
    }

    @Test
    public void testLocalProxyUsingSelfCreatedCertificates()
    {
        NeodymiumTestExecutionSummary summary = run(LocalProxyUsingSelfCreatedCertificates.class);
        checkPass(summary, 1, 0);
    }

    @Test
    public void testLocalProxyUsingProvidedCertificates()
    {
        NeodymiumTestExecutionSummary summary = run(LocalProxyUsingProvidedCertificates.class);
        checkPass(summary, 1, 0);
    }

    @Test
    public void testLocalProxyUsingProvidedCertificatesRuntimeException()
    {
        NeodymiumTestExecutionSummary summary = run(LocalProxyUsingProvidedCertificatesRuntimeException.class);
        checkFail(summary, 1, 0, 1,
                  "java.lang.RuntimeException: The local proxy certificate isn't fully configured. Please check: certificate archive type, certificate archive file, certificate name and certificate password.");
    }

    public static void assertWebDriverClosed(WebDriver webDriver)
    {
        Assertions.assertNotNull(webDriver);
        RemoteWebDriver driver = (RemoteWebDriver) ((EventFiringWebDriver) webDriver).getWrappedDriver();
        Assertions.assertNull(driver.getSessionId());
    }

    public static void assertWebDriverAlive(WebDriver webDriver)
    {
        Assertions.assertNotNull(webDriver);
        RemoteWebDriver driver = (RemoteWebDriver) ((EventFiringWebDriver) webDriver).getWrappedDriver();
        Assertions.assertNotNull(driver.getSessionId());
    }

    public static void assertProxyStopped(BrowserUpProxy proxy)
    {
        Assertions.assertNotNull(proxy);
        Assertions.assertTrue(proxy.isStarted());
        Assertions.assertTrue(((BrowserUpProxyServer) proxy).isStopped());
    }

    public static void assertProxyAlive(BrowserUpProxy proxy)
    {
        Assertions.assertNotNull(proxy);
        Assertions.assertTrue(proxy.isStarted());
        Assertions.assertFalse(((BrowserUpProxyServer) proxy).isStopped());
    }
}
