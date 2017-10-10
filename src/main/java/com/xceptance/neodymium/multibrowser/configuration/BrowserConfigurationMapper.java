package com.xceptance.neodymium.multibrowser.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

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

    // appium specific propertys
    private static final String APPIUM_VERSION = "appiumVersion";

    private static final String BROWSER_NAME = "browserName";

    private static final String PLATTFORM_VERSION = "platformVersion";

    private static final String APP = "app";

    private static final String AUTOMATION_NAME = "automationName";

    public BrowserConfiguration map(Map<String, String> o)
    {
        BrowserConfiguration r = new BrowserConfiguration();
        DesiredCapabilities capabilities;

        String emulatedBrowser = o.get(BROWSER);
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
            capabilities = DesiredCapabilities.firefox();
        }
        else if ("chrome".equals(emulatedBrowser))
        {
            capabilities = DesiredCapabilities.chrome();
        }
        else if ("internetexplorer".equals(emulatedBrowser))
        {
            capabilities = DesiredCapabilities.internetExplorer();
        }
        else if ("safari".equals(emulatedBrowser))
        {
            capabilities = DesiredCapabilities.safari();
        }
        else
        {
            capabilities = DesiredCapabilities.firefox();
        }

        /*
         * SauceLabs configuration
         */
        String emulatedPlatform = o.get(PLATFORM);
        if (!StringUtils.isEmpty(emulatedPlatform))
            capabilities.setCapability("platform", emulatedPlatform);

        String emulatedVersion = o.get(BROWSER_VERSION);
        if (!StringUtils.isEmpty(emulatedVersion))
            capabilities.setCapability("version", emulatedVersion);

        String emulatedDeviceName = o.get(DEVICE_NAME);
        if (!StringUtils.isEmpty(emulatedDeviceName))
            capabilities.setCapability("deviceName", emulatedDeviceName);

        String emulatedDeviceOrienation = o.get(DEVICE_ORIENTATION);
        if (!StringUtils.isEmpty(emulatedDeviceOrienation))
            capabilities.setCapability("deviceOrientation", emulatedDeviceOrienation);

        String emulatedDeviceScreenResolution = o.get(SCREEN_RESOLUTION);
        if (!StringUtils.isEmpty(emulatedDeviceScreenResolution))
            capabilities.setCapability("screenResolution", emulatedDeviceScreenResolution);

        // appium

        String appiumVersion = o.get(APPIUM_VERSION);
        if (!StringUtils.isEmpty(appiumVersion))
            capabilities.setCapability("appiumVersion", appiumVersion);

        String browserName = o.get(BROWSER_NAME);
        if (!StringUtils.isEmpty(browserName))
            capabilities.setCapability("browserName", browserName);

        String plattformVersion = o.get(PLATTFORM_VERSION);
        if (!StringUtils.isEmpty(plattformVersion))
            capabilities.setCapability("platformVersion", plattformVersion);

        String app = o.get(APP);
        if (!StringUtils.isEmpty(app))
            capabilities.setCapability("app", app);

        String automationName = o.get(AUTOMATION_NAME);
        if (!StringUtils.isEmpty(automationName))
            capabilities.setCapability("automationName", automationName);

        /*
         * Chrome device emulation
         */
        String chromeEmulationProfile = o.get(CHROME_EMULATION_PROFILE);
        if (!StringUtils.isEmpty(chromeEmulationProfile))
        {
            Map<String, String> mobileEmulation = new HashMap<String, String>();
            mobileEmulation.put("deviceName", chromeEmulationProfile);

            Map<String, Object> chromeOptions = new HashMap<String, Object>();
            chromeOptions.put("mobileEmulation", mobileEmulation);
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }

        /*
         * Explicit test environment check
         */
        String testEnvironment = o.get(TEST_ENVIRONMENT);
        if (!StringUtils.isEmpty(testEnvironment))
            r.setTestEnvironment(testEnvironment);

        /*
         * Browser resolution
         */
        String browserResolution = o.get(BROWSER_RESOLUTION);
        if (!StringUtils.isEmpty(browserResolution))
        {
            // split the combined resolution string on every 'x', 'X' or ',' and remove all whitespace
            // e.g: 1920x1080 or 1920, 1080

            String[] browserWidthHeight = browserResolution.replaceAll("[\\s]", "").split("[xX,]");
            if (!StringUtils.isEmpty(browserWidthHeight[0]))
            {
                r.setBrowserWidth(Integer.parseInt(browserWidthHeight[0]));
            }
            if (!StringUtils.isEmpty(browserWidthHeight[0]))
            {
                r.setBrowserHeight(Integer.parseInt(browserWidthHeight[1]));
            }
        }

        capabilities.setCapability("name", o.get("name"));
        r.setCapabilities(capabilities);
        r.setConfigTag(o.get("browserTag"));
        r.setName(o.get("name"));

        return r;
    }
}
