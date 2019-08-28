package com.xceptance.neodymium.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.xceptance.neodymium.testclasses.webDriver.ValidateClearReuseWebDriverCache;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepWebDriverOpen;
import com.xceptance.neodymium.testclasses.webDriver.ValidateKeepWebDriverOpenOnFailure;
import com.xceptance.neodymium.testclasses.webDriver.ValidatePreventReuseWebDriver;
import com.xceptance.neodymium.testclasses.webDriver.ValidateReuseWebDriver;
import com.xceptance.neodymium.testclasses.webDriver.ValidateWebDriverClosed;

public class NeodymiumWebDriverTest extends NeodymiumTest
{
    @Test
    public void testValidateWebDriverClosed()
    {
        Result result = JUnitCore.runClasses(ValidateWebDriverClosed.class);
        checkPass(result, 2, 0, 0);
    }

    @Test
    public void testValidateReuseWebDriver()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateReuseWebDriver.class);
        checkPass(result, 2, 0, 0);
    }

    @Test
    public void testValidateClearReuseWebDriverCache()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidateClearReuseWebDriverCache.class);
        checkPass(result, 2, 0, 0);
    }

    @Test
    public void testValidatePreventReuseWebDriver()
    {
        // XVFB or a display needed
        Result result = JUnitCore.runClasses(ValidatePreventReuseWebDriver.class);
        checkPass(result, 3, 0, 0);
    }

    @Test
    public void testValidateKeepWebDriverOpen()
    {
        Result result = JUnitCore.runClasses(ValidateKeepWebDriverOpen.class);
        checkPass(result, 2, 0, 0);
    }

    @Test
    public void testValidateKeepWebDriverOpenOnFailure()
    {
        Result result = JUnitCore.runClasses(ValidateKeepWebDriverOpenOnFailure.class);
        checkFail(result, 3, 0, 1, null);
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
}
