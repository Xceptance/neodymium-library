package com.xceptance.neodymium.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import com.codeborne.selenide.Selenide;

/**
 * See our Github wiki: <a href="https://github.com/Xceptance/neodymium-library/wiki/Context">Context</a>
 * 
 * @author m.kaufmann
 */
public class Neodymium
{
    private static final Map<Thread, Neodymium> CONTEXTS = Collections.synchronizedMap(new WeakHashMap<>());

    // keep our current driver
    private WebDriver driver;

    // keep our current browser profile name
    private String browserProfileName;

    // our global configuration
    private final NeodymiumConfiguration configuration;

    // localization
    private final NeodymiumLocalization localization;

    // our data for anywhere access
    private final Map<String, String> data = new HashMap<>();

    /**
     * Constructor
     */
    private Neodymium()
    {
        configuration = ConfigFactory.create(NeodymiumConfiguration.class, System.getProperties(), System.getenv());
        localization = NeodymiumLocalization.build(configuration.localizationFile());
    }

    /**
     * Retrieves the context instance for the current Thread.
     * 
     * @return the context instance for the current Thread
     */
    public static Neodymium getContext()
    {
        return CONTEXTS.computeIfAbsent(Thread.currentThread(), key -> {
            return new Neodymium();
        });
    }

    /**
     * Clears the context instance for the current Thread. <br>
     * Attention: clearing the context leads to a loss of dynamic test parameter: browserProfileName, data and driver.
     */
    public static void clearThreadContext()
    {
        CONTEXTS.remove(Thread.currentThread());
    }

    /**
     * Shortcut for localized text access. Will fail with an assertion if the key cannot be found
     *
     * @param key
     *            key to lookup
     * @return localized text
     */
    public static String localizedText(final String key)
    {
        return getContext().localization.getText(key);
    }

    public static Map<String, String> getData()
    {
        return getContext().data;
    }

    /**
     * Shortcut for data access. Will fail with an assertion if the key cannot be found
     *
     * @param key
     *            key to lookup
     * @return value of the data map
     */
    public static String dataValue(final String key)
    {
        return getData().get(key);
    }

    public static NeodymiumConfiguration configuration()
    {
        return getContext().configuration;
    }

    public static WebDriver getDriver()
    {
        return getContext().driver;
    }

    public static void setDriver(WebDriver driver)
    {
        getContext().driver = driver;
    }

    public static String getBrowserProfileName()
    {
        return getContext().browserProfileName;
    }

    public static void setBrowserProfileName(String browserProfileName)
    {
        getContext().browserProfileName = browserProfileName;
    }

    /**
     * Current window width and height
     * 
     * @return {@link Dimension} object containing width and height of current window
     */
    public static Dimension getWindowSize()
    {
        return getDriver().manage().window().getSize();
    }

    /**
     * Current viewport width and height
     * 
     * @return {@link Dimension} object containing width and height of current viewport
     */
    public static Dimension getViewportSize()
    {
        Long width = Selenide.executeJavaScript("return window.innerWidth");
        Long height = Selenide.executeJavaScript("return window.innerHeight");

        return new Dimension(width.intValue(), height.intValue());
    }

    /**
     * Current page width and height
     * 
     * @return {@link Dimension} object containing width and height of current page
     */
    public static Dimension getPageSize()
    {
        Long width = Selenide.executeJavaScript("return document.documentElement.clientWidth");
        Long height = Selenide.executeJavaScript("return document.documentElement.clientHeight");

        return new Dimension(width.intValue(), height.intValue());
    }

    /**
     * Phone or smaller
     * 
     * @return boolean value indicating whether it is a mobile device or not
     */
    public static boolean isMobile()
    {
        return getViewportSize().getWidth() < configuration().mediumDeviceBreakpoint();
    }

    /**
     * Tablet or large phone
     * 
     * @return boolean value indicating whether it is a tablet device/large phone or not
     * @see Neodymium
     */
    public static boolean isTablet()
    {
        return getViewportSize().getWidth() >= configuration().mediumDeviceBreakpoint() &&
               getViewportSize().getWidth() < configuration().largeDeviceBreakpoint();
    }

    /**
     * Small desktop aka half window or stuff, can be tablet as well
     * 
     * @return boolean value indicating whether it is a device with small desktop or not
     * @see Neodymium
     */
    public static boolean isSmallDesktop()
    {
        return getViewportSize().getWidth() >= configuration().largeDeviceBreakpoint() &&
               getViewportSize().getWidth() < configuration().xlargeDeviceBreakpoint();
    }

    /**
     * Large desktop resolution?
     * 
     * @return boolean value indicating whether it is a device with small desktop or not
     * @see Neodymium
     */
    public static boolean isLargeDesktop()
    {
        return getViewportSize().getWidth() >= configuration().xlargeDeviceBreakpoint();
    }

    /**
     * Desktop of any kind?
     * 
     * @return boolean value indicating whether it is a device desktop (neither small nor large) or not
     * @see Neodymium
     */
    public static boolean isDesktop()
    {
        return getViewportSize().getWidth() >= configuration().largeDeviceBreakpoint();
    }
}
