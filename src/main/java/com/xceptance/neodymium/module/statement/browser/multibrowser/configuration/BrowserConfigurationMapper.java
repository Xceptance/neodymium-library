package com.xceptance.neodymium.module.statement.browser.multibrowser.configuration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

public class BrowserConfigurationMapper
{
    private static final String BROWSER = "browser";

    private static final String BROWSER_VERSION = "version";

    private static final String PLATFORM = "platform";

    private static final String DEVICE_NAME = "deviceName";

    private static final String DEVICE_ORIENTATION = "deviceOrientation";

    private static final String SCREEN_RESOLUTION = "screenResolution";

    private static final String BROWSER_RESOLUTION = "browserResolution";

    private static final String CHROME_EMULATION_PROFILE = "chromeEmulationProfile";

    private static final String TEST_ENVIRONMENT = "testEnvironment";

    private static final String PAGE_LOAD_STRATEGY = "pageLoadStrategy";

    private static final String ACCEPT_INSECURE_CERTS = "acceptInsecureCertificates";

    private static final String HEADLESS = "headless";

    private static final String ARGUMENTS = "arguments";

    // Appium specific properties
    private static final String APPIUM_VERSION = "appiumVersion";

    private static final String BROWSER_NAME = "browserName";

    private static final String PLATFORM_NAME = "platformName";

    private static final String PLATFORM_VERSION = "platformVersion";

    private static final String APP = "app";

    private static final String AUTOMATION_NAME = "automationName";

    private static final String ORIENTATION = "orientation";

    public BrowserConfiguration map(Map<String, String> browserProfileConfiguration)
    {
        BrowserConfiguration browserConfiguration = new BrowserConfiguration();

        MutableCapabilities capabilities;

        String emulatedBrowser = browserProfileConfiguration.get(BROWSER);
        if (emulatedBrowser != null)
            emulatedBrowser = emulatedBrowser.toLowerCase();

        if ("iphone".equals(emulatedBrowser))
        {
            capabilities = DesiredCapabilities.iphone();
        }
        else if ("ipad".equals(emulatedBrowser))
        {
            capabilities = DesiredCapabilities.ipad();
        }
        else if ("android".equals(emulatedBrowser))
        {
            capabilities = DesiredCapabilities.android();
        }
        else if ("firefox".equals(emulatedBrowser))
        {
            capabilities = new FirefoxOptions();
        }
        else if ("chrome".equals(emulatedBrowser))
        {
            capabilities = new ChromeOptions();
        }
        else if ("internetexplorer".equals(emulatedBrowser))
        {
            capabilities = DesiredCapabilities.internetExplorer();
        }
        else if ("safari".equals(emulatedBrowser))
        {
            capabilities = new SafariOptions();
        }
        else if ("edge".equals(emulatedBrowser))
        {
            capabilities = new EdgeOptions();
        }
        else if ("opera".equals(emulatedBrowser))
        {
            capabilities = new OperaOptions();
        }
        else
        {
            capabilities = new DesiredCapabilities();
        }

        /*
         * SauceLabs configuration
         */
        String emulatedPlatform = browserProfileConfiguration.get(PLATFORM);
        if (!StringUtils.isEmpty(emulatedPlatform))
            capabilities.setCapability(CapabilityType.PLATFORM, emulatedPlatform);

        String emulatedPlatformName = browserProfileConfiguration.get(PLATFORM_NAME);
        if (!StringUtils.isEmpty(emulatedPlatformName))
            capabilities.setCapability(CapabilityType.PLATFORM_NAME, emulatedPlatformName);

        String emulatedVersion = browserProfileConfiguration.get(BROWSER_VERSION);
        if (!StringUtils.isEmpty(emulatedVersion))
            capabilities.setCapability(CapabilityType.VERSION, emulatedVersion);

        String emulatedDeviceName = browserProfileConfiguration.get(DEVICE_NAME);
        if (!StringUtils.isEmpty(emulatedDeviceName))
            capabilities.setCapability("deviceName", emulatedDeviceName);

        String emulatedDeviceOrientation = browserProfileConfiguration.get(DEVICE_ORIENTATION);
        if (!StringUtils.isEmpty(emulatedDeviceOrientation))
            capabilities.setCapability("deviceOrientation", emulatedDeviceOrientation);

        String emulatedDeviceScreenResolution = browserProfileConfiguration.get(SCREEN_RESOLUTION);
        if (!StringUtils.isEmpty(emulatedDeviceScreenResolution))
        {
            // SauceLabs
            capabilities.setCapability("screenResolution", emulatedDeviceScreenResolution);
            // testingBot
            capabilities.setCapability("screen-resolution", emulatedDeviceScreenResolution);
        }

        // appium

        String appiumVersion = browserProfileConfiguration.get(APPIUM_VERSION);
        if (!StringUtils.isEmpty(appiumVersion))
            capabilities.setCapability(APPIUM_VERSION, appiumVersion);

        String browserName = browserProfileConfiguration.get(BROWSER_NAME);
        if (!StringUtils.isEmpty(browserName))
            capabilities.setCapability(BROWSER_NAME, browserName);

        String platformVersion = browserProfileConfiguration.get(PLATFORM_VERSION);
        if (!StringUtils.isEmpty(platformVersion))
            capabilities.setCapability(PLATFORM_VERSION, platformVersion);

        String platformName = browserProfileConfiguration.get(PLATFORM_NAME);
        if (!StringUtils.isEmpty(platformName))
            capabilities.setCapability(PLATFORM_NAME, platformName);

        String app = browserProfileConfiguration.get(APP);
        if (!StringUtils.isEmpty(app))
            capabilities.setCapability(APP, app);

        String automationName = browserProfileConfiguration.get(AUTOMATION_NAME);
        if (!StringUtils.isEmpty(automationName))
            capabilities.setCapability(AUTOMATION_NAME, automationName);

        String orientation = browserProfileConfiguration.get(ORIENTATION);
        if (!StringUtils.isEmpty(orientation))
            capabilities.setCapability(ORIENTATION, orientation);

        /*
         * Chrome device emulation
         */
        String chromeEmulationProfile = browserProfileConfiguration.get(CHROME_EMULATION_PROFILE);
        if (!StringUtils.isEmpty(chromeEmulationProfile))
        {
            Map<String, String> mobileEmulation = new HashMap<String, String>();
            mobileEmulation.put("deviceName", chromeEmulationProfile);

            ((ChromeOptions) capabilities).setExperimentalOption("mobileEmulation", mobileEmulation);
        }

        /*
         * Explicit test environment check
         */
        String testEnvironment = browserProfileConfiguration.get(TEST_ENVIRONMENT);
        if (!StringUtils.isEmpty(testEnvironment))
            browserConfiguration.setTestEnvironment(testEnvironment.trim());

        /*
         * Browser resolution
         */
        String browserResolution = browserProfileConfiguration.get(BROWSER_RESOLUTION);
        if (!StringUtils.isEmpty(browserResolution))
        {
            // split the combined resolution string on every 'x', 'X' or ',' and remove all whitespace
            // e.g: 1920x1080 or 1920, 1080

            String[] browserWidthHeight = browserResolution.replaceAll("[\\s]", "").split("[xX,]");
            if (!StringUtils.isEmpty(browserWidthHeight[0]))
            {
                browserConfiguration.setBrowserWidth(Integer.parseInt(browserWidthHeight[0]));
            }
            if (!StringUtils.isEmpty(browserWidthHeight[0]))
            {
                browserConfiguration.setBrowserHeight(Integer.parseInt(browserWidthHeight[1]));
            }
        }

        // page load strategy
        String pageLoadStrategy = browserProfileConfiguration.get(PAGE_LOAD_STRATEGY);
        if (!StringUtils.isEmpty(pageLoadStrategy))
            capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, pageLoadStrategy);

        // accept insecure certificates
        String acceptInsecureCerts = browserProfileConfiguration.get(ACCEPT_INSECURE_CERTS);
        if (!StringUtils.isEmpty(acceptInsecureCerts))
            capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, Boolean.parseBoolean(acceptInsecureCerts));

        // headless
        String headless = browserProfileConfiguration.get(HEADLESS);
        if (!StringUtils.isEmpty(headless))
            browserConfiguration.setHeadless(Boolean.valueOf(headless));

        // additional browser arguments
        String arguments = browserProfileConfiguration.get(ARGUMENTS);
        if (!StringUtils.isEmpty(arguments))
        {
            List<String> args = new LinkedList<>();

            for (String arg : arguments.split(";"))
            {
                // cut off trailing/leading whitespace because the browsers can't handle it
                args.add(arg.trim());
            }
            browserConfiguration.setArguments(args);
        }

        capabilities.setCapability("name", browserProfileConfiguration.get("name"));
        browserConfiguration.setCapabilities(capabilities);
        browserConfiguration.setConfigTag(browserProfileConfiguration.get("browserTag"));
        browserConfiguration.setName(browserProfileConfiguration.get("name"));

        return browserConfiguration;
    }
}
