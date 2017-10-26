package com.xceptance.neodymium.multibrowser;

import java.net.MalformedURLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.xceptance.neodymium.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.DriverServerPath;
import com.xceptance.neodymium.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.WebDriverProperties;

public class WebDriverFactory
{
    public static WebDriver create(String browserConfigTag) throws MalformedURLException
    {
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

        final BrowserConfiguration browserConfiguration = parsedBrowserProperties.get(browserConfigTag);
        if (browserConfiguration == null)
        {
            throw new IllegalArgumentException("Can not find browser configuration with tag: " + browserConfigTag);
        }

        WebDriver webdriver = BrowserRunnerHelper.createWebdriver(browserConfiguration);

        // set browser window size
        BrowserRunnerHelper.setBrowserWindowSize(browserConfiguration, webdriver);

        return webdriver;
    }
}
