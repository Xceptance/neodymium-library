package com.xceptance.neodymium.module.statement.browser.multibrowser;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.runners.statements.RunAfters;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
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

public class BrowserRunAfters extends RunAfters
{
    private final Statement next;

    private final List<FrameworkMethod> afters;

    public BrowserRunAfters(Statement next, List<FrameworkMethod> afters, Object target)
    {
        super(next, afters, target);
        this.afters = afters;
        this.next = next;
    }

    @Override
    public void evaluate() throws Throwable
    {
        List<Throwable> errors = new ArrayList<Throwable>();
        try
        {
            next.evaluate();
        }
        catch (Throwable e)
        {
            errors.add(e);
        }
        finally
        {
            WebDriverStateContainer oldDriver = Neodymium.getWebDriverStateContainer();
            BrowserConfiguration oldBrowserConfiguration = MultibrowserConfiguration.getInstance().getBrowserProfiles()
                                                                                    .get(Neodymium.getBrowserProfileName());
            for (FrameworkMethod each : afters)
            {
                boolean isSuppressed = isSuppressed(each);
                boolean startNewBrowserForCleanUp = shouldStartNewBrowser(each) && !isSuppressed;

                BrowserConfiguration browserConfiguration = oldBrowserConfiguration;
                List<Browser> methodBrowserAnnotations = StatementBuilder.getAnnotations(each.getMethod(), Browser.class);

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
                else if (!isSuppressed && browserConfiguration == null)
                {
                    throw new RuntimeException("No browser setting for @After method '" + each.getName()
                                               + "' was found. "
                                               + "If browser was suppressed for the test and is also not required for the clean up,"
                                               + " please mark the @After method with @DontStartNewBrowserForCleanUp annotation."
                                               + " If you need to start a browser for the clean up,"
                                               + " please, use @Browser annotaion to mention what browser should be used exactly for this @After.");
                }
                boolean afterFailed = false;
                try
                {
                    invokeMethod(each);
                }
                catch (Throwable e)
                {
                    afterFailed = true;
                    errors.add(e);
                }
                finally
                {
                    // if we did a set up of new driver before the @After method, we need to close it
                    if (startNewBrowserForCleanUp && browserConfiguration != null)
                    {
                        // avoid closing driver in case it's selected to keep browser open
                        if (!(!browserConfiguration.isHeadless()
                              && ((Neodymium.configuration().keepBrowserOpenOnFailure() && afterFailed) || Neodymium.configuration().keepBrowserOpen())))
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
        }
        MultipleFailureException.assertEmpty(errors);
    }

    private boolean shouldStartNewBrowser(FrameworkMethod each)
    {
        List<DontStartNewBrowserForCleanUp> methodStartNewBrowserForCleanUp = StatementBuilder.getAnnotations(each.getMethod(),
                                                                                                              DontStartNewBrowserForCleanUp.class);
        List<DontStartNewBrowserForCleanUp> classStartNewBrowserForCleanUp = StatementBuilder.getAnnotations(each.getDeclaringClass(),
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

    private boolean isSuppressed(FrameworkMethod each)
    {
        List<SuppressBrowsers> methodSuppressBrowserAnnotations = StatementBuilder.getAnnotations(each.getMethod(), SuppressBrowsers.class);

        return !methodSuppressBrowserAnnotations.isEmpty();
    }
}
