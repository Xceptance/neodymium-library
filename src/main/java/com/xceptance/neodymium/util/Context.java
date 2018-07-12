package com.xceptance.neodymium.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
public class Context
{
    private static final Map<Thread, Context> CONTEXTS = Collections.synchronizedMap(new WeakHashMap<>());

    // keep our current driver
    public WebDriver driver;

    // keep our current browser profile name
    public String browserProfileName;

    // our global configuration
    public final NeodymiumConfiguration configuration;

    public final Class<? extends NeodymiumConfiguration> clazz;

    // localization
    public final NeodymiumLocalization localization;

    // our data for anywhere access
    public final Map<String, String> data = new HashMap<>();

    /**
     * Constructor
     * 
     * @param clazz
     */
    private Context(final Map<String, String> testDataOfTestCase, Class<? extends NeodymiumConfiguration> clazz)
    {
        this.data.putAll(testDataOfTestCase);
        this.clazz = clazz;

        // build our config and use the official caching for that
        // use our test data as properties as well
        final Properties testData = new Properties();
        testData.putAll(data);

        configuration = ConfigFactory.create(clazz, System.getProperties(), System.getenv(), testData);

        localization = NeodymiumLocalization.build(configuration.localizationFile());
    }

    /**
     * Create a new context and associates it with the threads, if there is any previous context, it is just
     * overwritten.
     * 
     * @param clazz
     *            A {@link Class} extends {@link NeodymiumConfiguration} that is used to initialize the {@link Context}
     * @return the context instance for the current Thread
     */
    public static Context create(Class<? extends NeodymiumConfiguration> clazz)
    {
        return create(Collections.emptyMap(), clazz);
    }

    public static Context create(final Map<String, String> testDataOfTestCase)
    {
        return create(testDataOfTestCase, NeodymiumConfiguration.class);
    }

    /**
     * Create a new context and associates it with the threads, if there is any previous context, it is just
     * overwritten.
     * 
     * @param testDataOfTestCase
     *            A {@link Map} that contains all the
     *            <a href="https://github.com/Xceptance/neodymium-library/wiki/Test-data-provider">test data</a> that
     *            can be accessed from the test
     * @param clazz
     *            A {@link Class} extends {@link NeodymiumConfiguration} that is used to initialize the {@link Context}
     * @return {@link Context} that was freshly created or served from cache
     */
    public static Context create(final Map<String, String> testDataOfTestCase, Class<? extends NeodymiumConfiguration> clazz)
    {
        return CONTEXTS.computeIfAbsent(Thread.currentThread(), (key) -> {
            return new Context(testDataOfTestCase, clazz);
        });
    }

    /**
     * Retrieves the context instance for the current Thread.
     * 
     * @return the context instance for the current Thread
     */
    public static Context get()
    {
        return CONTEXTS.computeIfAbsent(Thread.currentThread(), key -> {
            return new Context(Collections.emptyMap(), NeodymiumConfiguration.class);
        });
    }

    /**
     * Clears the context instance for the current Thread.
     */
    public static void clearThreadContext()
    {
        CONTEXTS.remove(Thread.currentThread());
    }

    /**
     * Current window width and height
     * 
     * @return {@link Dimension} object containing width and height of current window
     */
    public Dimension getWindowSize()
    {
        return driver.manage().window().getSize();
    }

    /**
     * Current viewport width and height
     * 
     * @return {@link Dimension} object containing width and height of current viewport
     */
    public Dimension getViewportSize()
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
    public Dimension getPageSize()
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
    public boolean isMobile()
    {
        return getViewportSize().getWidth() < configuration.mediumDeviceBreakpoint();
    }

    /**
     * Tablet or large phone
     * 
     * @return boolean value indicating whether it is a tablet device/large phone or not
     * @see Context
     */
    public boolean isTablet()
    {
        return getViewportSize().getWidth() >= configuration.mediumDeviceBreakpoint() &&
               getViewportSize().getWidth() < configuration.largeDeviceBreakpoint();
    }

    /**
     * Small desktop aka half window or stuff, can be tablet as well
     * 
     * @return boolean value indicating whether it is a device with small desktop or not
     * @see Context
     */
    public boolean isSmallDesktop()
    {
        return getViewportSize().getWidth() >= configuration.largeDeviceBreakpoint() &&
               getViewportSize().getWidth() < configuration.xlargeDeviceBreakpoint();
    }

    /**
     * Large desktop resolution?
     * 
     * @return boolean value indicating whether it is a device with small desktop or not
     * @see Context
     */
    public boolean isLargeDesktop()
    {
        return getViewportSize().getWidth() >= configuration.xlargeDeviceBreakpoint();
    }

    /**
     * Desktop of any kind?
     * 
     * @return boolean value indicating whether it is a device desktop (neither small nor large) or not
     * @see Context
     */
    public boolean isDesktop()
    {
        return getViewportSize().getWidth() >= configuration.largeDeviceBreakpoint();
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
        return get().localization.getText(key);
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
        return get().data.get(key);
    }
}
