package com.xceptance.neodymium.module.statement.browser.multibrowser;

import java.util.List;

import org.junit.internal.runners.statements.RunBefores;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.browserup.bup.BrowserUpProxy;
import com.codeborne.selenide.WebDriverRunner;
import com.xceptance.neodymium.NeodymiumWebDriverListener;
import com.xceptance.neodymium.module.StatementBuilder;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.util.Neodymium;

public class BrowserRunBefores extends RunBefores
{
    private final Statement next;

    private final List<FrameworkMethod> befores;

    public BrowserRunBefores(Statement next, List<FrameworkMethod> befores, Object target)
    {
        super(next, befores, target);
        this.befores = befores;
        this.next = next;
    }

    @Override
    public void evaluate() throws Throwable
    {
        WebDriverStateContainer oldWDsCont = Neodymium.getWebDriverStateContainer();
        BrowserConfiguration oldBrowserConfiguration = MultibrowserConfiguration.getInstance().getBrowserProfiles()
                                                                                .get(Neodymium.getBrowserProfileName());
        for (FrameworkMethod before : befores)
        {
            boolean isSuppressed = isSuppressed(before);
            boolean startNewBrowserForSetUp = shouldStartNewBrowser(before) && !isSuppressed;
            BrowserConfiguration browserConfiguration = oldBrowserConfiguration;

            List<Browser> methodBrowserAnnotations = StatementBuilder.getAnnotations(before.getMethod(), Browser.class);

            // if @Before method is annotated with @Browser tag, it might need to be executed with another
            // browser
            if (!methodBrowserAnnotations.isEmpty())
            {
                browserConfiguration = MultibrowserConfiguration.getInstance().getBrowserProfiles()
                                                                .get(methodBrowserAnnotations.get(0).value());
            }

            // if we don't need to start new browser for the setup and the browser for the test was not suppressed
            // it means that we should use the same browser for setup
            // as the might have been other @Before methods with new browser running previously, let's explicitly set
            // the driver to original
            if (!startNewBrowserForSetUp && !isSuppressed && (Neodymium.getDriver() == null || !Neodymium.getDriver().equals(oldWDsCont.getWebDriver()))
                && oldWDsCont != null)
            {
                WebDriverRunner.setWebDriver(oldWDsCont.getWebDriver());
                Neodymium.setWebDriverStateContainer(oldWDsCont);
                Neodymium.setBrowserProfileName(oldBrowserConfiguration.getConfigTag());
                Neodymium.setBrowserName(oldBrowserConfiguration.getCapabilities().getBrowserName());
            }

            // if we need to start new browser for the set up and any browser configuration for the @Before method
            // was found, create a new driver
            else if (startNewBrowserForSetUp && browserConfiguration != null)
            {
                WebDriverStateContainer wDSCont = BrowserRunnerHelper.createWebDriverStateContainer(browserConfiguration);

                EventFiringWebDriver eFWDriver = new EventFiringWebDriver(wDSCont.getWebDriver());
                eFWDriver.register(new NeodymiumWebDriverListener());
                wDSCont.setWebDriver(eFWDriver);

                BrowserRunnerHelper.setBrowserWindowSize(browserConfiguration, wDSCont.getWebDriver());
                WebDriverRunner.setWebDriver(wDSCont.getWebDriver());
                Neodymium.setWebDriverStateContainer(wDSCont);
                Neodymium.setBrowserProfileName(browserConfiguration.getConfigTag());
                Neodymium.setBrowserName(browserConfiguration.getCapabilities().getBrowserName());

            }
            else if (startNewBrowserForSetUp)
            {
                throw new RuntimeException("No browser setting for @Before method '" + before.getName()
                                           + "' was found. "
                                           + "If browser is suppressed for the test and is also not required for the set up,"
                                           + " please mark the @Before method with @DontStartNewBrowserForSetUp annotation."
                                           + " If you need to start a browser for the set up,"
                                           + " please, use @Browser annotaion to mention what browser should be used exactly for this @Before.");
            }
            boolean beforeFailed = false;
            try
            {
                invokeMethod(before);
            }
            catch (Throwable e)
            {
                beforeFailed = true;
                throw e;
            }
            finally
            {
                // if we did a set up of new driver before the @Before method, we need to close it
                if (startNewBrowserForSetUp && browserConfiguration != null)
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

                // set driver back to the original to execute the test or clean up (in case of failure)
                Neodymium.setWebDriverStateContainer(oldWDsCont);
                if (oldBrowserConfiguration != null)
                {
                    Neodymium.setBrowserProfileName(oldBrowserConfiguration.getConfigTag());
                    Neodymium.setBrowserName(oldBrowserConfiguration.getCapabilities().getBrowserName());
                }
                if (oldWDsCont != null && oldWDsCont.getWebDriver() != null)
                {
                    WebDriverRunner.setWebDriver(oldWDsCont.getWebDriver());
                }
            }
        }

        next.evaluate();
    }

    private boolean shouldStartNewBrowser(FrameworkMethod each)
    {
        List<DontStartNewBrowserForSetUp> methodStartNewBrowserForSetUp = StatementBuilder.getAnnotations(each.getMethod(),
                                                                                                          DontStartNewBrowserForSetUp.class);
        List<DontStartNewBrowserForSetUp> classStartNewBrowserForSetUp = StatementBuilder.getAnnotations(each.getDeclaringClass(),
                                                                                                         DontStartNewBrowserForSetUp.class);

        // if global config for dontStartNewBrowserForSetUp is set to false, we should not reach this point
        boolean dontStartNewBrowserForSetUp = true;
        if (!classStartNewBrowserForSetUp.isEmpty())
        {
            dontStartNewBrowserForSetUp = false;
        }

        if (!methodStartNewBrowserForSetUp.isEmpty())
        {
            dontStartNewBrowserForSetUp = false;
        }

        // if @Before method is annotated with @SuppressBrowser annotation, no new browser should be started
        return dontStartNewBrowserForSetUp;
    }

    private boolean isSuppressed(FrameworkMethod each)
    {
        List<SuppressBrowsers> methodSuppressBrowserAnnotations = StatementBuilder.getAnnotations(each.getMethod(), SuppressBrowsers.class);

        return !methodSuppressBrowserAnnotations.isEmpty();
    }
}
