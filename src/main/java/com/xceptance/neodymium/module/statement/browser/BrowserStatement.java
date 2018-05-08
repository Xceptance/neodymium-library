package com.xceptance.neodymium.module.statement.browser;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeborne.selenide.WebDriverRunner;
import com.xceptance.neodymium.module.StatementBuilder;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.BrowserRunnerHelper;
import com.xceptance.neodymium.module.statement.browser.multibrowser.WebDriverCache;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.DriverServerPath;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.WebDriverProperties;
import com.xceptance.neodymium.util.Context;

public class BrowserStatement extends StatementBuilder
{
    public static Logger LOGGER = LoggerFactory.getLogger(BrowserStatement.class);

    private Statement next;

    private String browserTag;

    Set<String> browser = new LinkedHashSet<>();

    private static final String SYSTEM_PROPERTY_BROWSERDEFINITION = "browserdefinition";

    private static final String BROWSER_PROFILE_FILE = "./config/browser.properties";

    private List<String> browserDefinitions = new LinkedList<>();

    private static MultibrowserConfiguration multibrowserConfiguration = MultibrowserConfiguration.getInstance();

    private WebDriver webdriver;

    public BrowserStatement()
    {
    }

    public BrowserStatement(Statement next, String parameter)
    {
        this.next = next;
        this.browserTag = parameter;
    }

    @Override
    public void evaluate() throws Throwable
    {
        boolean testFailed = false;

        LOGGER.debug("setup browser: " + browserTag);
        setUpTest();
        try
        {
            next.evaluate();
        }
        catch (Throwable t)
        {
            testFailed = true;
        }
        finally
        {
            teardown(testFailed);
            LOGGER.debug("teardown browser: " + browserTag);
        }
    }

    /**
     * Sets the test instance up.
     */
    protected void setUpTest()
    {
        webdriver = null;
        LOGGER.debug("Create browser for name: " + browserTag);
        BrowserConfiguration browserConfiguration = multibrowserConfiguration.getBrowserProfiles().get(browserTag);

        try
        {
            // try to find appropriate web driver in cache before create a new instance
            if (MultibrowserConfiguration.getInstance().getWebDriverProperties().reuseWebDriver())
            {
                webdriver = WebDriverCache.instance.getRemoveWebDriver(browserConfiguration.getConfigTag());
                if (webdriver != null)
                {
                    webdriver.manage().deleteAllCookies();
                }
            }

            if (webdriver == null)
            {
                LOGGER.debug("Create new browser instance");
                webdriver = BrowserRunnerHelper.createWebdriver(browserConfiguration);
            }
            else
            {
                LOGGER.debug("Browser instance served from cache");
            }
        }
        catch (final MalformedURLException e)
        {
            throw new RuntimeException("An error occured during URL creation. See nested exception.", e);
        }
        if (webdriver != null)
        {
            // set browser window size
            BrowserRunnerHelper.setBrowserWindowSize(browserConfiguration, webdriver);
            WebDriverRunner.setWebDriver(webdriver);
            Context.get().driver = webdriver;
            Context.get().browserProfileName = browserConfiguration.getConfigTag();
        }
        else
        {
            throw new RuntimeException("Could not create driver for browsertag: " + browserConfiguration.getConfigTag() +
                                       ". Please check your browserconfigurations.");
        }
    }

    public void teardown(boolean testFailed)
    {
        WebDriverProperties webDriverProperties = MultibrowserConfiguration.getInstance().getWebDriverProperties();

        if (testFailed && webDriverProperties.keepBrowserOpenOnFailure())
        {
            // test failed and we want to leave the browser instance open
            // don't quit the webdriver, just remove references
            // TODO: logging
            Context.get().driver = null;
            Context.get().browserProfileName = null;
            return;
        }

        if (webDriverProperties.reuseWebDriver())
        {
            LOGGER.debug("Put browser into cache");
            WebDriverCache.instance.putWebDriver(browserTag, webdriver);
        }
        else
        {
            if (!webDriverProperties.keepBrowserOpen() && webdriver != null)
            {
                LOGGER.debug("Teardown browser");
                webdriver.quit();
            }
        }
        Context.get().driver = null;
        Context.get().browserProfileName = null;
    }

    public static void quitCachedBrowser()
    {
        WebDriverProperties webDriverProperties = MultibrowserConfiguration.getInstance().getWebDriverProperties();
        if (!webDriverProperties.keepBrowserOpen())
        {
            Collection<WebDriver> allWebdriver = WebDriverCache.instance.getAllWebdriver();

            for (WebDriver wd : allWebdriver)
            {
                try
                {
                    LOGGER.debug("Quit web driver: " + wd.toString());
                    wd.quit();
                }
                catch (Exception e)
                {
                    LOGGER.debug("Error on quitting web driver", e);
                }
            }
        }
    }

    @Override
    public List<Object> createIterationData(TestClass testClass, FrameworkMethod method)
    {
        // get the @Browser annotation from the method to run as well as from the enclosing class
        // if it doesn't exist check the class for a @Browser annotation
        Browser methodBrowser = method.getAnnotation(Browser.class);
        Browser classBrowser = testClass.getAnnotation(Browser.class);

        if (methodBrowser != null)
        {
            browser.addAll(Arrays.asList(methodBrowser.value()));
        }
        else if (classBrowser != null)
        {
            browser.addAll(Arrays.asList(classBrowser.value()));
        }

        // that is like a dirty hack to provide testing ability
        if (multibrowserConfiguration == null)
            multibrowserConfiguration = MultibrowserConfiguration.getInstance(BROWSER_PROFILE_FILE);

        DriverServerPath driverServerPath = multibrowserConfiguration.getDriverServerPath();
        WebDriverProperties webDriverProperties = multibrowserConfiguration.getWebDriverProperties();

        final String ieDriverPath = driverServerPath.getIeDriverPath();
        final String chromeDriverPath = driverServerPath.getChromeDriverPath();
        final String geckoDriverPath = driverServerPath.getFirefoxDriverPath();

        // shall we run old school firefox?
        final boolean firefoxLegacy = webDriverProperties.useFirefoxLegacy();
        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, Boolean.toString(!firefoxLegacy));

        if (!StringUtils.isEmpty(ieDriverPath))
        {
            System.setProperty("webdriver.ie.driver", ieDriverPath);
        }
        if (!StringUtils.isEmpty(chromeDriverPath))
        {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        }
        if (!StringUtils.isEmpty(geckoDriverPath))
        {
            System.setProperty("webdriver.gecko.driver", geckoDriverPath);
        }

        // get test specific browser definitions (aka browser tag see browser.properties)
        // could be one value or comma separated list of values
        String browserDefinitionsProperty = System.getProperty(SYSTEM_PROPERTY_BROWSERDEFINITION, "");
        browserDefinitionsProperty = browserDefinitionsProperty.replaceAll("\\s", "");

        // parse test specific browser definitions
        if (!StringUtils.isEmpty(browserDefinitionsProperty))
        {
            browserDefinitions.addAll(Arrays.asList(browserDefinitionsProperty.split(",")));
        }
        Map<String, BrowserConfiguration> parsedBrowserProperties = multibrowserConfiguration.getBrowserProfiles();

        List<Object> iterations = new LinkedList<>();
        for (String browserTag : browser)
        {
            // check if the annotated target is in the list of targets specified via system property
            if (browserDefinitions != null && !browserDefinitions.isEmpty() && !browserDefinitions.contains(browserTag))
            {
                continue;
            }

            final BrowserConfiguration foundBrowserConfiguration = parsedBrowserProperties.get(browserTag);
            if (foundBrowserConfiguration == null)
            {
                throw new IllegalArgumentException("Can not find browser configuration with tag: " + browserTag);
            }

            // create the JUnit children
            iterations.add(browserTag);
        }

        return iterations;
    }

    @Override
    public StatementBuilder createStatement(Object testClassInstance, Statement next, Object parameter)
    {
        return new BrowserStatement(next, (String) parameter);
    }

    @Override
    public String getTestName(Object data)
    {
        return MessageFormat.format("[Browser {0}]", (String) data);
    }

}
