package com.xceptance.neodymium.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import com.codeborne.selenide.AssertionMode;
import com.codeborne.selenide.Configuration;
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

    public final static String TEMPORARY_CONFIG_FILE_PROPTERY_NAME = "neodymium.temporaryConfigFile";

    /**
     * Constructor
     */
    private Neodymium()
    {
        // the property needs to be a valid URI in order to satisfy the Owner framework
        if (null == ConfigFactory.getProperty(TEMPORARY_CONFIG_FILE_PROPTERY_NAME))
        {
            ConfigFactory.setProperty(TEMPORARY_CONFIG_FILE_PROPTERY_NAME, "file:this/path/should/never/exist/noOneShouldCreateMe.properties");
        }
        configuration = ConfigFactory.create(NeodymiumConfiguration.class, System.getProperties(), System.getenv());
        localization = NeodymiumLocalization.build(configuration.localizationFile());
    }

    /**
     * Retrieves the context instance for the current Thread.
     * 
     * @return the context instance for the current Thread
     */
    static Neodymium getContext()
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
     * Extra small devices (portrait phones or smaller)
     * 
     * @return boolean value indicating whether it is a mobile device or not
     */
    public static boolean isExtraSmallDevice()
    {
        return getViewportSize().getWidth() < configuration().smallDeviceBreakpoint();
    }

    /**
     * Small devices (landscape phones)
     * 
     * @return boolean value indicating whether it is a tablet device/large phone or not
     * @see Neodymium
     */
    public static boolean isSmallDevice()
    {
        int width = getViewportSize().getWidth();
        NeodymiumConfiguration cfg = configuration();

        return width >= cfg.smallDeviceBreakpoint() && width < cfg.mediumDeviceBreakpoint();
    }

    /**
     * Medium devices (Tablets and small desktop aka half window or stuff)
     * 
     * @return boolean value indicating whether it is a device with small desktop or not
     * @see Neodymium
     */
    public static boolean isMediumDevice()
    {
        int width = getViewportSize().getWidth();
        NeodymiumConfiguration cfg = configuration();

        return width >= cfg.mediumDeviceBreakpoint() && width < cfg.largeDeviceBreakpoint();
    }

    /**
     * Large devices (Desktop)
     * 
     * @return boolean value indicating whether it is a device with large desktop or not
     * @see Neodymium
     */
    public static boolean isLargeDevice()
    {
        int width = getViewportSize().getWidth();
        NeodymiumConfiguration cfg = configuration();

        return width >= cfg.largeDeviceBreakpoint() && width < cfg.xlargeDeviceBreakpoint();
    }

    /**
     * Extra large devices (Large desktop)
     * 
     * @return boolean value indicating whether it is a device with extra large resolution or not
     * @see Neodymium
     */
    public static boolean isExtraLargeDevice()
    {
        return getViewportSize().getWidth() >= configuration().xlargeDeviceBreakpoint();
    }

    /**
     * Mobile of any kind?
     * 
     * @return boolean indicating whether it is a mobile device or not
     * @see Neodymium
     */
    public static boolean isMobile()
    {
        return getViewportSize().getWidth() < configuration().mediumDeviceBreakpoint();
    }

    /**
     * Tablet of any kind?
     * 
     * @return boolean value boolean value indicating whether it is a tablet device/large phone or not
     * @see Neodymium
     */
    public static boolean isTablet()
    {
        return isMediumDevice();
    }

    /**
     * Desktop of any kind?
     * 
     * @return boolean value indicating whether it is a device desktop (isLargeDesktop() or isExtaLargeDesktop()) or not
     * @see Neodymium
     */
    public static boolean isDesktop()
    {
        return getViewportSize().getWidth() >= configuration().largeDeviceBreakpoint();
    }

    /**
     * Shortcut to turn on/off Selenide SoftAssertions <br>
     * You need to add the following JUnit rule to the test class to enable the feature
     * 
     * <pre>
     * &#64;Rule
     * public SoftAsserts softAsserts = new com.codeborne.selenide.junit.SoftAsserts();
     * </pre>
     * 
     * @param useSoftAssertions
     *            boolean if the Selenide soft assertion feature is activated
     */
    public static void softAssertions(boolean useSoftAssertions)
    {
        if (useSoftAssertions)
        {
            Configuration.assertionMode = AssertionMode.SOFT;
        }
        else
        {
            Configuration.assertionMode = AssertionMode.STRICT;
        }
    }

    /**
     * Shortcut to turn on/off Selenide clickViaJs
     * 
     * @param clickViaJs
     *            boolean that decides if a click is executed via JavaScript
     */
    public static void clickViaJs(boolean clickViaJs)
    {
        Configuration.clickViaJs = clickViaJs;
    }

    /**
     * Shortcut to turn on/off Selenide fastSetValue
     * 
     * @param fastSetValue
     *            boolean that decides if a value is set JavaScript
     */
    public static void fastSetValue(boolean fastSetValue)
    {
        Configuration.fastSetValue = fastSetValue;
    }

    /**
     * Shortcut to turn on/off Selenide timeout
     * 
     * @param timeout
     *            the time that a Selenide command waits implicitly before it raises an error if it can't be executed
     */
    public static void timeout(long timeout)
    {
        Configuration.timeout = timeout;
    }
}
