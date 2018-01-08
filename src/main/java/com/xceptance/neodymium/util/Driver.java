package com.xceptance.neodymium.util;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;

import com.codeborne.selenide.WebDriverRunner;
import com.xceptance.neodymium.multibrowser.WebDriverCache;
import com.xceptance.neodymium.multibrowser.WebDriverFactory;
import com.xceptance.neodymium.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.WebDriverProperties;

public class Driver
{
    public static void setUp(final String browserProfileName)
    {
        // try to find appropriate web driver in cache before creating a new instance
        WebDriver driver = null;

        if (MultibrowserConfiguration.getInstance().getWebDriverProperties().reuseWebDriver())
        {
            driver = WebDriverCache.getIntance().removeGetWebDriver(browserProfileName);
        }

        // nothing in the cache or nothing set yet
        if (driver == null)
        {
            try
            {
                driver = WebDriverFactory.create(browserProfileName);
            }
            catch (MalformedURLException e)
            {
                throw new RuntimeException(e);
            }
            WebDriverRunner.setWebDriver(driver);
        }
        else
        {
            // ok, clean reused driver
            driver.manage().deleteAllCookies();
        }

        // keep in globally
        Context.get().driver = driver;
        Context.get().browserProfileName = browserProfileName;
    }

    public static void tearDown()
    {
        WebDriverProperties webDriverProperties = MultibrowserConfiguration.getInstance().getWebDriverProperties();
        if (webDriverProperties.reuseWebDriver())
        {
            WebDriverCache.getIntance().putWebDriverForTag(Context.get().browserProfileName, Context.get().driver);
        }
        else
        {
            if (!webDriverProperties.keepBrowserOpen() && Context.get().driver != null)
            {
                Context.get().driver.quit();
            }
        }

        Context.get().driver = null;
        Context.get().browserProfileName = null;
    }
}
