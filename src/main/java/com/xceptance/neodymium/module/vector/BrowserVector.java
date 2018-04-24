package com.xceptance.neodymium.module.vector;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Collection;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeborne.selenide.WebDriverRunner;
import com.xceptance.neodymium.multibrowser.BrowserRunnerHelper;
import com.xceptance.neodymium.multibrowser.WebDriverCache;
import com.xceptance.neodymium.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.WebDriverProperties;
import com.xceptance.neodymium.util.Context;

public class BrowserVector implements RunVector
{
    public static Logger LOGGER = LoggerFactory.getLogger(BrowserVector.class);

    private String browserTag;

    private int vectorHashCode;

    private WebDriver webdriver;

    private BrowserConfiguration browserConfiguration;

    public BrowserVector(BrowserConfiguration browserConfiguration, int vectorHashCode)
    {
        this.browserConfiguration = browserConfiguration;
        this.browserTag = browserConfiguration.getConfigTag();
        this.vectorHashCode = vectorHashCode;
    }

    @Override
    public String getTestName()
    {
        return MessageFormat.format("[Browser {0}]", browserTag);
    }

    @Override
    public void beforeMethod()
    {
        setUpTest();
    }

    @Override
    public void afterMethod()
    {
        teardown();
    }

    /**
     * Sets the test instance up.
     */
    protected void setUpTest()
    {
        webdriver = null;
        LOGGER.debug("Create browser for name: " + browserConfiguration.getName());
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

    public void teardown()
    {
        WebDriverProperties webDriverProperties = MultibrowserConfiguration.getInstance().getWebDriverProperties();
        if (webDriverProperties.reuseWebDriver())
        {
            LOGGER.debug("Put browser into cache");
            WebDriverCache.instance.putWebDriver(browserConfiguration.getConfigTag(), webdriver);
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

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((browserTag == null) ? 0 : browserTag.hashCode());
        return result;
    }

    @Override
    public int vectorHashCode()
    {
        return vectorHashCode;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof BrowserVector))
        {
            return false;
        }
        BrowserVector other = (BrowserVector) obj;
        if (browserTag == null)
        {
            if (other.browserTag != null)
            {
                return false;
            }
        }
        else if (!browserTag.equals(other.browserTag))
        {
            return false;
        }
        return true;
    }

    @Override
    public void setTestClassInstance(Object testClassInstance)
    {
        // we dont need this for browsers
    }
}
