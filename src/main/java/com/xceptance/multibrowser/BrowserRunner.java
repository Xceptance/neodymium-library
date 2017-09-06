package com.xceptance.multibrowser;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.codeborne.selenide.WebDriverRunner;
import com.xceptance.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.multibrowser.configuration.DriverServerPath;
import com.xceptance.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.multibrowser.configuration.WebDriverProperties;

/**
 * JUnit runner used to run testcases that are annotated with {@link Browser}. This class reads the annotation based
 * configuration of {@link Browser} and executes the testcase multiple-times with different configurations.
 * 
 * @author m.kaufmann
 * @see {@link Browser}
 */
public class BrowserRunner extends ParentRunner<Runner>
{
    /**
     * The JUnit children of this runner.
     */
    private final List<Runner> browser = new LinkedList<Runner>();

    private WebDriver driver;

    private BrowserConfiguration browserConfig;

    private Class<?> testCaseClass;

    /**
     * Sets the test instance up.
     *
     * @param method
     *            the method
     * @param test
     *            the test instance
     */
    protected void setUpTest(final FrameworkMethod method, final Object test)
    {
        // set the test data set at the test instance
        final BrowserFrameworkMethod frameworkMethod = (BrowserFrameworkMethod) method;

        // get the browser configuration for this testcase
        final BrowserConfiguration config = frameworkMethod.getBrowserConfiguration();

        driver = null;
        try
        {
            driver = BrowserRunnerHelper.createWebdriver(config);
        }
        catch (final MalformedURLException e)
        {
            throw new RuntimeException("An error occured during URL creation. See nested exception.", e);
        }
        if (driver != null)
        {
            // set browser window size
            BrowserRunnerHelper.setBrowserWindowSize(config, driver);
            WebDriverRunner.setWebDriver(driver);
            // ((AbstractScriptTestCase) test).setTestDataSet(frameworkMethod.getDataSet()); //TODO:

        }
        else
        {
            throw new RuntimeException("Could not create driver for browsertag: " + config.getConfigTag() +
                                       ". Please check your browserconfigurations.");
        }
    }

    /**
     * Sets the test instance up.
     *
     * @param method
     *            the method
     * @param test
     *            the test instance
     */
    protected void setUpTest()
    {
        driver = null;
        try
        {
            driver = BrowserRunnerHelper.createWebdriver(browserConfig);
        }
        catch (final MalformedURLException e)
        {
            throw new RuntimeException("An error occured during URL creation. See nested exception.", e);
        }
        if (driver != null)
        {
            // set browser window size
            BrowserRunnerHelper.setBrowserWindowSize(browserConfig, driver);
            WebDriverRunner.setWebDriver(driver);
            // ((AbstractScriptTestCase) test).setTestDataSet(frameworkMethod.getDataSet()); //TODO:

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
        // super.run(notifier);
        notifier.fireTestRunStarted(getDescription());
        setUpTest();
        notifier.fireTestFinished(getDescription());
    }

    private BrowserRunner(Class<?> testCaseClass, BrowserConfiguration browserConfig) throws InitializationError
    {
        super(testCaseClass);
        this.testCaseClass = testCaseClass;
        this.browserConfig = browserConfig;
    }

    public BrowserRunner(final Class<?> testCaseClass) throws Throwable
    {
        super(testCaseClass);

        MultibrowserConfiguration multibrowserConfiguration = MultibrowserConfiguration.getIntance();

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

        // // get test specific browser definitions (aka browser tag see browser.properties)
        // // could be one value or comma separated list of values
        // String browserDefinitionsProperty = XltProperties.getInstance().getProperty(SYSTEM_PROPERTY_BROWSERDEFINITION, "");
        // if (browserDefinitionsProperty != null)
        // browserDefinitionsProperty = browserDefinitionsProperty.replaceAll("\\s", "");

        List<String> browserDefinitions = null;

        // // parse test specific browser definitions
        // if (!StringUtils.isEmpty(browserDefinitionsProperty))
        // {
        // browserDefinitions = Arrays.asList(browserDefinitionsProperty.split(","));
        // }

        // Get annotations of test class.
        final Annotation[] annotations = testCaseClass.getAnnotations();
        for (final Annotation annotation : annotations)
        {
            // only check Browser annotation with a list browser configuration tags
            if (annotation instanceof Browser)
            {
                foundTargetsAnnotation = true;

                final String[] targets = ((Browser) annotation).value();

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

                    // for (final FrameworkMethod frameworkMethod : getTestClass().getAnnotatedMethods(Test.class))
                    // {
                    // get the test method to run
                    // final Method testMethod = frameworkMethod.getMethod();

                    // create the JUnit children
                    browser.add(new BrowserRunner(testCaseClass, foundBrowserConfiguration));
                    // }
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

    // /**
    // * {@inheritDoc}
    // */
    // @Override
    // protected Statement methodInvoker(final FrameworkMethod method, final Object test)
    // {
    // try
    // {
    // // prepare the test instance before executing it
    // setUpTest(method, test);
    // }
    // catch (Exception e)
    // {
    // return new Fail(e);
    // }
    // // the real job is done here
    // return super.methodInvoker(method, test);
    // }

    // @Override
    // protected Statement withAfters(FrameworkMethod method, Object target, Statement statement)
    // {
    // List<FrameworkMethod> methods = new LinkedList<>();
    // try
    // {
    // methods.add(new FrameworkMethod(this.getClass().getMethod("teardown")));
    // }
    // catch (NoSuchMethodException | SecurityException e)
    // {
    // throw new RuntimeException(e);
    // }
    //
    // statement = new RunAfters(statement, methods, this);
    // return super.withAfters(method, target, statement);
    // }

    public void teardown()
    {
        if (driver != null && !MultibrowserConfiguration.getIntance().getWebDriverProperties().keepBrowserOpen())
        {
            driver.quit();
        }
    }

    @Override
    protected Description describeChild(Runner child)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void runChild(Runner child, RunNotifier notifier)
    {
        // TODO Auto-generated method stub
        System.out.println("run annotation child");
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
