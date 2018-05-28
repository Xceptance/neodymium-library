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

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.xceptance.neodymium.module.StatementBuilder;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.BrowserRunnerHelper;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;
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

    private MultibrowserConfiguration multibrowserConfiguration = MultibrowserConfiguration.getInstance();

    private WebDriver webdriver;

    public BrowserStatement()
    {
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
        setUpTest(browserTag);
        try
        {
            next.evaluate();
        }
        catch (Throwable t)
        {
            testFailed = true;
            throw t;
        }
        finally
        {
            teardown(testFailed);
        }
    }

    /**
     * Sets the test instance up.
     * 
     * @param browserTag
     *            name of the browser corresponding to the browser.properties
     */
    public void setUpTest(String browserTag)
    {
        webdriver = null;
        this.browserTag = browserTag;
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

            // set our default timeout
            Configuration.timeout = Context.get().configuration.timeout();
            Configuration.collectionsTimeout = Configuration.timeout * 2;
        }
        else
        {
            throw new RuntimeException("Could not create driver for browsertag: " + browserConfiguration.getConfigTag() +
                                       ". Please check your browserconfigurations.");
        }
    }

    public void teardown(boolean testFailed)
    {
        WebDriverProperties webDriverProperties = multibrowserConfiguration.getWebDriverProperties();
        Context context = Context.get();
        BrowserConfiguration browserConfiguration = multibrowserConfiguration.getBrowserProfiles().get(context.browserProfileName);

        if (testFailed && webDriverProperties.keepBrowserOpenOnFailure() && !browserConfiguration.isHeadless())
        {
            // test failed and we want to leave the browser instance open
            // don't quit the webdriver, just remove references
            LOGGER.debug("Keep browser open");
            context.driver = null;
            context.browserProfileName = null;
            return;
        }

        if (webDriverProperties.reuseWebDriver())
        {
            LOGGER.debug("Put browser into cache");
            WebDriverCache.instance.putWebDriver(browserTag, webdriver);
        }
        else
        {
            if (browserConfiguration.isHeadless() || !webDriverProperties.keepBrowserOpen())
            {
                LOGGER.debug("Teardown browser");
                if (webdriver != null)
                    webdriver.quit();
            }
        }
        context.driver = null;
        context.browserProfileName = null;
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
        List<Browser> methodBrowserAnnotations = getAnnotations(method.getMethod(), Browser.class);
        List<Browser> classBrowserAnnotations = findClassBrowserAnnotation(testClass.getJavaClass());
        List<SuppressBrowsers> methodSuppressBrowserAnnotations = getAnnotations(method.getMethod(), SuppressBrowsers.class);
        List<SuppressBrowsers> classSuppressBrowserAnnotations = getAnnotations(testClass.getJavaClass(), SuppressBrowsers.class);

        if (!methodSuppressBrowserAnnotations.isEmpty())
        {
            // method is marked to suppress browser
            return new LinkedList<>();
        }

        if (!classSuppressBrowserAnnotations.isEmpty() && methodBrowserAnnotations.isEmpty())
        {
            // class is marked to suppress browsers and there is no override on the method
            return new LinkedList<>();
        }

        // so there might be a browser suppress on the class but there is at least one override on the method
        List<Browser> browserAnnotations = new LinkedList<>();

        // add all browser annotations from the method
        browserAnnotations.addAll(methodBrowserAnnotations);

        // if the class doesn't have suppress and method doesn't have any overrides then add them too
        if (classSuppressBrowserAnnotations.isEmpty() && methodBrowserAnnotations.isEmpty())
        {
            browserAnnotations.addAll(classBrowserAnnotations);
        }

        for (Browser b : browserAnnotations)
        {
            browser.add(b.value());
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

    public List<Browser> findClassBrowserAnnotation(Class<?> clazz)
    {
        // this function is used to find the first (!) @Browser annotation on class level in the hierarchy
        // furthermore its not the first but also the first that doesn't have @SuppressBrowsers annotated

        if (clazz == null)
            return new LinkedList<>();

        // check class for browser annotation
        // if class has browser annotation and no suppress browsers its fine, else take the super class and check again
        List<Browser> browserAnnotations = getDeclaredAnnotations(clazz, Browser.class);
        List<SuppressBrowsers> suppressBrowsersAnnotations = getDeclaredAnnotations(clazz, SuppressBrowsers.class);

        if (!suppressBrowsersAnnotations.isEmpty() || browserAnnotations.isEmpty())
        {
            return findClassBrowserAnnotation(clazz.getSuperclass());
        }
        else
        {
            return browserAnnotations;
        }

    }

    @Override
    public StatementBuilder createStatement(Object testClassInstance, Statement next, Object parameter)
    {
        return new BrowserStatement(next, (String) parameter);
    }

    @Override
    public String getTestName(Object data)
    {
        return MessageFormat.format("Browser {0}", (String) data);
    }

    @Override
    public String getCategoryName(Object data)
    {
        return getTestName(data);
    }
}
