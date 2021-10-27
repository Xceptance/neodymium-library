package com.xceptance.neodymium.junit5.browser;

import java.net.MalformedURLException;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.browserup.bup.BrowserUpProxy;
import com.codeborne.selenide.WebDriverRunner;
import com.xceptance.neodymium.NeodymiumWebDriverListener;
import com.xceptance.neodymium.common.browser.BrowserRunnerHelper;
import com.xceptance.neodymium.common.browser.WebDriverCache;
import com.xceptance.neodymium.common.browser.WebDriverStateContainer;
import com.xceptance.neodymium.common.browser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.common.browser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.util.Neodymium;

public class BrowserExecutionCallback implements BeforeEachCallback, TestWatcher
{
    private String browserTag;

    private WebDriverStateContainer wDSCont;

    private MultibrowserConfiguration multibrowserConfiguration = MultibrowserConfiguration.getInstance();

    public BrowserExecutionCallback(String browserTag)
    {
        this.browserTag = browserTag;
    }

    public BrowserExecutionCallback()
    {
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception
    {
        System.out.println("Setup " + browserTag);
        setUpTest(browserTag);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause)
    {
        teardown(true, false, wDSCont);
    }

    @Override
    public void testSuccessful(ExtensionContext context)
    {
        teardown(false, false, wDSCont);
    }

    /**
     * Sets the test instance up.
     * 
     * @param browserTag
     *            name of the browser corresponding to the browser.properties
     */
    public void setUpTest(String browserTag)
    {
        wDSCont = null;
        this.browserTag = browserTag;
        BrowserConfiguration browserConfiguration = multibrowserConfiguration.getBrowserProfiles().get(browserTag);
        try
        {
            // try to find appropriate web driver in cache before create a new instance
            if (Neodymium.configuration().reuseWebDriver())
            {
                wDSCont = WebDriverCache.instance.removeWebDriverStateContainerByBrowserTag(browserConfiguration.getConfigTag());
                if (wDSCont != null && wDSCont.getWebDriver() != null)
                {
                    wDSCont.getWebDriver().manage().deleteAllCookies();
                }
            }

            if (wDSCont == null)
            {
                wDSCont = BrowserRunnerHelper.createWebDriverStateContainer(browserConfiguration);
                EventFiringWebDriver eFWDriver = new EventFiringWebDriver(wDSCont.getWebDriver());
                eFWDriver.register(new NeodymiumWebDriverListener());
                wDSCont.setWebDriver(eFWDriver);
            }
            else
            {
                System.out.println("Browser instance served from cache");
            }
        }
        catch (final MalformedURLException e)
        {
            throw new RuntimeException("An error occurred during URL creation. See nested exception.", e);
        }
        if (wDSCont != null)
        {
            // set browser window size
            BrowserRunnerHelper.setBrowserWindowSize(browserConfiguration, wDSCont.getWebDriver());
            WebDriverRunner.setWebDriver(wDSCont.getWebDriver());
            Neodymium.setWebDriverStateContainer(wDSCont);
            Neodymium.setBrowserProfileName(browserConfiguration.getConfigTag());
            Neodymium.setBrowserName(browserConfiguration.getCapabilities().getBrowserName());

            initSelenideConfiguration();
        }
        else
        {
            throw new RuntimeException("Could not create driver for browsertag: " + browserConfiguration.getConfigTag() +
                                       ". Please check your browserconfigurations.");
        }
    }

    private void initSelenideConfiguration()
    {
        // set our default timeout
        Neodymium.timeout(Neodymium.configuration().selenideTimeout());

        Neodymium.fastSetValue(Neodymium.configuration().selenideFastSetValue());
        Neodymium.clickViaJs(Neodymium.configuration().selenideClickViaJs());
    }

    public void teardown(boolean testFailed, boolean preventReuse, WebDriverStateContainer webDriverStateContainer)
    {
        BrowserConfiguration browserConfiguration = multibrowserConfiguration.getBrowserProfiles().get(Neodymium.getBrowserProfileName());
        // keep browser open
        if (keepOpen(testFailed, browserConfiguration))
        {
            System.out.println("Keep browser open");
            // nothing to do
        }
        // reuse
        else if (canReuse(preventReuse, webDriverStateContainer))
        {
            System.out.println("Put browser into cache");
            webDriverStateContainer.incrementUsedCount();
            WebDriverCache.instance.putWebDriverStateContainer(browserTag, webDriverStateContainer);
        }
        // close the WebDriver
        else
        {
            System.out.println("Teardown browser");
            WebDriver webDriver = webDriverStateContainer.getWebDriver();
            if (webDriver != null)
            {
                webDriver.quit();
            }

            BrowserUpProxy proxy = webDriverStateContainer != null ? webDriverStateContainer.getProxy() : null;
            if (proxy != null)
            {
                try
                {
                    proxy.stop();
                }
                catch (IllegalStateException e)
                {
                    // nothing to do here except for catching error of a second stop of the proxy
                }
            }
        }

        Neodymium.setWebDriverStateContainer(null);
        Neodymium.setBrowserProfileName(null);
        Neodymium.setBrowserName(null);
    }

    private boolean keepOpen(boolean testFailed, BrowserConfiguration browserConfiguration)
    {
        return (browserConfiguration != null && !browserConfiguration.isHeadless())
               && ((Neodymium.configuration().keepBrowserOpenOnFailure() && testFailed) || Neodymium.configuration().keepBrowserOpen());
    }

    private boolean canReuse(boolean preventReuse, WebDriverStateContainer webDriverStateContainer)
    {
        boolean maxReuseReached = (Neodymium.configuration().maxWebDriverReuse() > 0)
                                  && (webDriverStateContainer.getUsedCount() >= Neodymium.configuration().maxWebDriverReuse());
        return Neodymium.configuration().reuseWebDriver() && !preventReuse && !maxReuseReached && isWebDriverStillOpen(webDriverStateContainer.getWebDriver());
    }

    private boolean isWebDriverStillOpen(final WebDriver webDriver)
    {
        if (webDriver == null)
        {
            return false;
        }
        try
        {
            RemoteWebDriver driver = (RemoteWebDriver) ((EventFiringWebDriver) webDriver).getWrappedDriver();
            return driver.getSessionId() != null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Couldn't detect if the WebDriver is still open!");
            return true;
        }
    }
}
