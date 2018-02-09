package com.xceptance.neodymium.multibrowser;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeborne.selenide.WebDriverRunner;
import com.xceptance.neodymium.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.DriverServerPath;
import com.xceptance.neodymium.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.WebDriverProperties;
import com.xceptance.neodymium.util.Context;

/**
 * JUnit runner used to run testcases that are annotated with {@link Browser}. This class reads the annotation based
 * configuration of {@link Browser} and executes the testcase multiple-times with different configurations.
 * 
 * @author m.kaufmann
 * @see Browser
 */
public class BrowserRunner extends ParentRunner<Runner>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserRunner.class);

    /**
     * The JUnit children of this runner.
     */
    private final List<Runner> browser = new LinkedList<Runner>();

    private WebDriver webdriver;

    private BrowserConfiguration browserConfig;

    private static final String SYSTEM_PROPERTY_BROWSERDEFINITION = "browserdefinition";

    /**
     * Sets the test instance up.
     */
    protected void setUpTest()
    {
        webdriver = null;
        LOGGER.debug("Create browser for name: " + browserConfig.getName());
        try
        {
            // try to find appropriate web driver in cache before create a new instance
            if (MultibrowserConfiguration.getInstance().getWebDriverProperties().reuseWebDriver())
            {
                webdriver = WebDriverCache.instance.getRemoveWebDriver(browserConfig.getConfigTag());
                if (webdriver != null)
                {
                    webdriver.manage().deleteAllCookies();
                }
            }

            if (webdriver == null)
            {
                LOGGER.debug("Create new browser instance");
                webdriver = BrowserRunnerHelper.createWebdriver(browserConfig);
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
            BrowserRunnerHelper.setBrowserWindowSize(browserConfig, webdriver);
            WebDriverRunner.setWebDriver(webdriver);
            Context.get().driver = webdriver;
            Context.get().browserProfileName = browserConfig.getConfigTag();
        }
        else
        {
            throw new RuntimeException("Could not create driver for browsertag: " + browserConfig.getConfigTag() +
                                       ". Please check your browserconfigurations.");
        }
    }

    @Override
    public void run(RunNotifier notifier)
    {
        setUpTest();
    }

    private BrowserRunner(Class<?> testCaseClass, BrowserConfiguration browserConfig) throws InitializationError
    {
        super(testCaseClass);
        this.browserConfig = browserConfig;
    }

    public BrowserRunner(final Class<?> testCaseClass) throws Throwable
    {
        super(testCaseClass);

        MultibrowserConfiguration multibrowserConfiguration = MultibrowserConfiguration.getInstance();

        DriverServerPath driverServerPath = multibrowserConfiguration.getDriverServerPath();
        WebDriverProperties webDriverProperties = multibrowserConfiguration.getWebDriverProperties();

        final Map<String, BrowserConfiguration> parsedBrowserProperties = multibrowserConfiguration.getBrowserProfiles();

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
        boolean foundTargetsAnnotation = false;

        // get test specific browser definitions (aka browser tag see browser.properties)
        // could be one value or comma separated list of values
        String browserDefinitionsProperty = System.getProperty(SYSTEM_PROPERTY_BROWSERDEFINITION, "");
        browserDefinitionsProperty = browserDefinitionsProperty.replaceAll("\\s", "");

        List<String> browserDefinitions = null;

        // parse test specific browser definitions
        if (!StringUtils.isEmpty(browserDefinitionsProperty))
        {
            browserDefinitions = Arrays.asList(browserDefinitionsProperty.split(","));
        }

        // Get annotations of test class.
        final Annotation[] annotations = testCaseClass.getAnnotations();
        for (final Annotation annotation : annotations)
        {
            // only check Browser annotation with a list browser configuration tags
            if (annotation instanceof Browser)
            {
                foundTargetsAnnotation = true;

                // remove duplicate targets by putting them into a set and get them back out
                // use LinkedHashSet to preserve order of insertion
                Set<String> targetSet = new LinkedHashSet<>();
                targetSet.addAll(Arrays.asList(((Browser) annotation).value()));
                final String[] targets = targetSet.toArray(new String[0]);

                for (final String target : targets)
                {
                    // check if the annotated target is in the list of targets specified via system property
                    if (browserDefinitions != null && !browserDefinitions.contains(target))
                    {
                        continue;
                    }

                    final BrowserConfiguration foundBrowserConfiguration = parsedBrowserProperties.get(target);
                    if (foundBrowserConfiguration == null)
                    {
                        throw new IllegalArgumentException("Can not find browser configuration with tag: " + target);
                    }

                    // create the JUnit children
                    browser.add(new BrowserRunner(testCaseClass, foundBrowserConfiguration));
                }
            }
        }

        if (!foundTargetsAnnotation)
            throw new IllegalArgumentException("The class (" + testCaseClass.getSimpleName() +
                                               ") does not have a required Browser annotation.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Runner> getChildren()
    {
        return browser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Description getDescription()
    {
        return Description.createTestDescription(browserConfig.getName(), "");
    }

    public void teardown()
    {
        WebDriverProperties webDriverProperties = MultibrowserConfiguration.getInstance().getWebDriverProperties();
        if (webDriverProperties.reuseWebDriver())
        {
            LOGGER.debug("Put browser into cache");
            WebDriverCache.instance.putWebDriver(browserConfig.getConfigTag(), webdriver);
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
    protected Description describeChild(Runner child)
    {
        return null;
    }

    @Override
    protected void runChild(Runner child, RunNotifier notifier)
    {
    }

    /**
     * Returns a name used to describe this Runner
     */
    @Override
    public String getName()
    {
        return browserConfig.getName();
    }
}
