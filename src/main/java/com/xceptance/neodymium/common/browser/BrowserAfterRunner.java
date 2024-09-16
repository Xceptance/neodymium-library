package com.xceptance.neodymium.common.browser;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;

import com.browserup.bup.BrowserUpProxy;
import com.codeborne.selenide.WebDriverRunner;
import com.xceptance.neodymium.common.Data;
import com.xceptance.neodymium.common.browser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.common.browser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.junit4.statement.browser.DontStartNewBrowserForCleanUp;
import com.xceptance.neodymium.util.Neodymium;

public class BrowserAfterRunner
{
    public void run(Supplier<Throwable> afterMethodInvocation, Method after, boolean junit5) throws Throwable
    {
        WebDriverStateContainer oldDriver = Neodymium.getWebDriverStateContainer();
        BrowserConfiguration oldBrowserConfiguration = MultibrowserConfiguration.getInstance().getBrowserProfiles()
                                                                                .get(Neodymium.getBrowserProfileName());
        boolean isSuppressed = isSuppressed(after);
        boolean startNewBrowserForCleanUp = shouldStartNewBrowser(after) && !isSuppressed;

        BrowserConfiguration browserConfiguration = oldBrowserConfiguration;
        List<Browser> methodBrowserAnnotations = Data.getAnnotations(after, Browser.class);

        // if @After method is annotated with @Browser tag, it might need to be executed with another
        // browser
        if (!methodBrowserAnnotations.isEmpty())
        {
            browserConfiguration = MultibrowserConfiguration.getInstance().getBrowserProfiles()
                                                            .get(methodBrowserAnnotations.get(0).value());
        }

        // if we don't need to start new browser for the cleanup and the browser for the test was not suppressed
        // it means that we should use the same browser for cleanup
        // as the might have been other @After methods with new browser running previously, let's explicitly set
        // the driver to original
        if (!startNewBrowserForCleanUp && !isSuppressed && (Neodymium.getDriver() == null || !Neodymium.getDriver().equals(oldDriver.getWebDriver()))
            && oldDriver != null)
        {
            WebDriverRunner.setWebDriver(oldDriver.getWebDriver());
            Neodymium.setWebDriverStateContainer(oldDriver);
            Neodymium.setBrowserProfileName(oldBrowserConfiguration.getConfigTag());
            Neodymium.setBrowserName(oldBrowserConfiguration.getCapabilities().getBrowserName());
        }
        // if we need to start new browser for the clean up and any browser configuration for the @After method
        // was found, create a new driver
        else if (startNewBrowserForCleanUp && browserConfiguration != null)
        {
            WebDriverStateContainer wDSCont = BrowserRunnerHelper.createWebDriverStateContainer(browserConfiguration, after.getDeclaringClass());

            BrowserRunnerHelper.setBrowserWindowSize(browserConfiguration, wDSCont.getWebDriver());
            WebDriverRunner.setWebDriver(wDSCont.getWebDriver());
            Neodymium.setWebDriverStateContainer(wDSCont);
            Neodymium.setBrowserProfileName(browserConfiguration.getConfigTag());
            Neodymium.setBrowserName(browserConfiguration.getCapabilities().getBrowserName());
        }
        else if (!isSuppressed && browserConfiguration == null)
        {
            throw new RuntimeException("No browser setting for " + (junit5 ? "@AfterEach" : "@After") + " method '" + after.getName()
                                       + "' was found. "
                                       + "If browser was suppressed for the test and is also not required for the clean up,"
                                       + " please mark the " + (junit5 ? "@AfterEach" : "@After") + " method with @DontStartNewBrowserForCleanUp annotation."
                                       + " If you need to start a browser for the clean up,"
                                       + " please, use @Browser annotaion to mention what browser should be used exactly for this "
                                       + (junit5 ? "@AfterEach" : "@After") + ".");
        }
        boolean beforeFailed = false;
        try
        {
            Throwable e = afterMethodInvocation.get();
            if (e != null)
            {
                beforeFailed = true;
                throw e;
            }
        }
        finally
        {
            // if we did a set up of new driver before the @After method, we need to close it
            if (startNewBrowserForCleanUp && browserConfiguration != null)
            {
                // avoid closing driver in case it's selected to keep browser open
                if (!(!browserConfiguration.isHeadless()
                      && ((Neodymium.configuration().keepBrowserOpenOnFailure() && beforeFailed) || Neodymium.configuration().keepBrowserOpen())))
                {
                    WebDriverStateContainer wdst = Neodymium.getWebDriverStateContainer();
                    WebDriver driver = wdst != null ? wdst.getWebDriver() : null;
                    if (driver != null)
                    {
                        driver.quit();
                    }
                    BrowserUpProxy proxy = wdst != null ? wdst.getProxy() : null;
                    if (proxy != null)
                    {
                        proxy.stop();
                    }
                    Neodymium.setWebDriverStateContainer(null);
                }
            }
        }
    }

    private boolean shouldStartNewBrowser(Method each)
    {
        List<DontStartNewBrowserForCleanUp> methodStartNewBrowserForCleanUp = Data.getAnnotations(each,
                                                                                                  DontStartNewBrowserForCleanUp.class);
        List<DontStartNewBrowserForCleanUp> classStartNewBrowserForCleanUp = Data.getAnnotations(each.getDeclaringClass(),
                                                                                                 DontStartNewBrowserForCleanUp.class);

        // if global config for dontStartNewBrowserForCleanUp is set to false, we should not reach this point
        boolean dontStartNewBrowserForCleanUp = true;
        if (!classStartNewBrowserForCleanUp.isEmpty())
        {
            dontStartNewBrowserForCleanUp = false;
        }

        if (!methodStartNewBrowserForCleanUp.isEmpty())
        {
            dontStartNewBrowserForCleanUp = false;
        }

        // if @After method is annotated with @SuppressBrowser annotation, no new browser should be started
        return dontStartNewBrowserForCleanUp;
    }

    private boolean isSuppressed(Method each)
    {
        List<SuppressBrowsers> methodSuppressBrowserAnnotations = Data.getAnnotations(each, SuppressBrowsers.class);

        return !methodSuppressBrowserAnnotations.isEmpty();
    }
}
