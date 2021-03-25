package com.xceptance.neodymium.module.statement.browser;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
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
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browserup.bup.BrowserUpProxy;
import com.codeborne.selenide.WebDriverRunner;
import com.xceptance.neodymium.NeodymiumWebDriverListener;
import com.xceptance.neodymium.module.StatementBuilder;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.BrowserRunnerHelper;
import com.xceptance.neodymium.module.statement.browser.multibrowser.RandomBrowsers;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;
import com.xceptance.neodymium.module.statement.browser.multibrowser.WebDriverCache;
import com.xceptance.neodymium.module.statement.browser.multibrowser.WebDriverStateContainer;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.util.Neodymium;

public class BrowserStatement extends StatementBuilder
{
    public static Logger LOGGER = LoggerFactory.getLogger(BrowserStatement.class);

    private Statement next;

    private String browserTag;

    Set<String> browser = new LinkedHashSet<>();

    private static final String SYSTEM_PROPERTY_BROWSERDEFINITION = "browserdefinition";

    private List<String> browserDefinitions = new LinkedList<>();

    private MultibrowserConfiguration multibrowserConfiguration = MultibrowserConfiguration.getInstance();

    private WebDriverStateContainer wDSCont;

    public BrowserStatement()
    {
        // that is like a dirty hack to provide testing ability
        if (multibrowserConfiguration == null)
            multibrowserConfiguration = MultibrowserConfiguration.getInstance();

        final String ieDriverPath = Neodymium.configuration().getIeDriverPath();
        final String chromeDriverPath = Neodymium.configuration().getChromeDriverPath();
        final String geckoDriverPath = Neodymium.configuration().getFirefoxDriverPath();

        // shall we run old school firefox?
        final boolean firefoxLegacy = Neodymium.configuration().useFirefoxLegacy();
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
        wDSCont = null;
        this.browserTag = browserTag;
        LOGGER.debug("Create browser for name: " + browserTag);
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
                LOGGER.debug("Create new browser instance");
                wDSCont = BrowserRunnerHelper.createWebDriverStateContainer(browserConfiguration);
                EventFiringWebDriver eFWDriver = new EventFiringWebDriver(wDSCont.getWebDriver());
                eFWDriver.register(new NeodymiumWebDriverListener());
                wDSCont.setWebDriver(eFWDriver);
            }
            else
            {
                LOGGER.debug("Browser instance served from cache");
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

    public void teardown(boolean testFailed)
    {
        teardown(testFailed, false, wDSCont);
    }

    public void teardown(boolean testFailed, boolean preventReuse, WebDriverStateContainer webDriverStateContainer)
    {
        BrowserConfiguration browserConfiguration = multibrowserConfiguration.getBrowserProfiles().get(Neodymium.getBrowserProfileName());

        // keep browser open
        if (keepOpen(testFailed, browserConfiguration))
        {
            LOGGER.debug("Keep browser open");
            // nothing to do
        }
        // reuse
        else if (canReuse(preventReuse, webDriverStateContainer))
        {
            LOGGER.debug("Put browser into cache");
            webDriverStateContainer.incrementUsedCount();
            WebDriverCache.instance.putWebDriverStateContainer(browserTag, webDriverStateContainer);
        }
        // close the WebDriver
        else
        {
            LOGGER.debug("Teardown browser");
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

    @Override
    public List<Object> createIterationData(TestClass testClass, FrameworkMethod method)
    {
        // get the @Browser annotation from the method to run as well as from the enclosing class
        // if it doesn't exist check the class for a @Browser annotation
        List<Browser> methodBrowserAnnotations = getAnnotations(method.getMethod(), Browser.class);
        List<Browser> classBrowserAnnotations = findBrowserRelatedClassAnnotation(testClass.getJavaClass(), Browser.class);
        List<SuppressBrowsers> methodSuppressBrowserAnnotations = getAnnotations(method.getMethod(), SuppressBrowsers.class);
        List<SuppressBrowsers> classSuppressBrowserAnnotations = getAnnotations(testClass.getJavaClass(), SuppressBrowsers.class);
        List<RandomBrowsers> methodRandomBrowsersAnnotations = getAnnotations(method.getMethod(), RandomBrowsers.class);
        List<RandomBrowsers> classRandomBrowsersAnnotation = findBrowserRelatedClassAnnotation(testClass.getJavaClass(), RandomBrowsers.class);

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

        // choose a random set from the available browser annotations
        if (!methodRandomBrowsersAnnotations.isEmpty())
        {
            // evaluate the method level (top priority)
            browserAnnotations = computeRandomBrowsers(method, methodRandomBrowsersAnnotations, browserAnnotations);
        }
        else if (!classRandomBrowsersAnnotation.isEmpty() && methodBrowserAnnotations.isEmpty())
        {
            // evaluate the class level (including inheritance)
            // Note: if browsers are annotated on method level they prohibit the evaluation of the random
            // browser annotation on class level
            browserAnnotations = computeRandomBrowsers(method, classRandomBrowsersAnnotation, browserAnnotations);
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

    private List<Browser> computeRandomBrowsers(final FrameworkMethod method, final List<RandomBrowsers> randomBrowsersAnnotation,
                                                final List<Browser> browserAnnotations)
    {
        if (randomBrowsersAnnotation.get(0).value() > browserAnnotations.size())
        {
            String msg = MessageFormat.format("Method ''{0}'' is marked to be run with {1} random browsers, but there are only {2} available",
                                              method.getName(), randomBrowsersAnnotation.get(0).value(), browserAnnotations.size());
            throw new IllegalArgumentException(msg);
        }
        if (randomBrowsersAnnotation.get(0).value() > 0)
        {
            Collections.shuffle(browserAnnotations, Neodymium.getRandom());
            return browserAnnotations.subList(0, randomBrowsersAnnotation.get(0).value());
        }
        return browserAnnotations;
    }

    public <T extends Annotation> List<T> findBrowserRelatedClassAnnotation(final Class<?> clazz, final Class<T> annotationToFind)
    {
        // this function is used to find the first (!) @Browser annotation on class level in the hierarchy
        // furthermore its not the first but also the first that doesn't have @SuppressBrowsers annotated

        if (clazz == null)
            return new LinkedList<>();

        // check class for browser annotation
        // if class has browser annotation and no suppress browsers its fine, else take the super class and check again
        List<T> browserAnnotations = getDeclaredAnnotations(clazz, annotationToFind);
        List<SuppressBrowsers> suppressBrowsersAnnotations = getDeclaredAnnotations(clazz, SuppressBrowsers.class);

        if (!suppressBrowsersAnnotations.isEmpty() || browserAnnotations.isEmpty())
        {
            return findBrowserRelatedClassAnnotation(clazz.getSuperclass(), annotationToFind);
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

    public List<String> getBrowserTags()
    {
        // make a copy of all available browser tags
        List<String> tags = new LinkedList<>();
        tags.addAll(multibrowserConfiguration.getBrowserProfiles().keySet());

        return tags;
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
            LOGGER.warn("Couldn't detect if the WebDriver is still open!", e);
            return true;
        }
    }
}
