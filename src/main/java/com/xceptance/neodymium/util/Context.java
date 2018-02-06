package com.xceptance.neodymium.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;

public class Context
{
    private static final Map<Thread, Context> CONTEXTS = Collections.synchronizedMap(new WeakHashMap<>());

    // keep our current driver
    public WebDriver driver;

    // keep our current browser profile name
    public String browserProfileName;

    // our global configuration
    public final Configuration configuration;

    public final Class<? extends Configuration> clazz;

    // localization
    public final Localization localization;

    // our data for anywhere access
    public final Map<String, String> data = new HashMap<>();

    /**
     * Constructor
     * 
     * @param clazz
     */
    private Context(final Map<String, String> testDataOfTestCase, Class<? extends Configuration> clazz)
    {
        this.data.putAll(testDataOfTestCase);
        this.clazz = clazz;

        // build our config and use the official caching for that
        // use our test data as properties as well
        final Properties testData = new Properties();
        testData.putAll(data);

        configuration = ConfigFactory.create(clazz,
                                             System.getProperties(),
                                             System.getenv(),
                                             testData);

        localization = Localization.build(configuration.localizationFile());
    }

    /**
     * Create a new context and associates it with the threads, if there is any previous context, it is just
     * overwritten.
     * 
     * @param clazz
     * @return the context instance for the current Thread
     */
    public static Context create(Class<? extends Configuration> clazz)
    {
        return create(Collections.emptyMap(), clazz);
    }

    public static Context create(final Map<String, String> testDataOfTestCase)
    {
        return create(testDataOfTestCase, Configuration.class);
    }

    /**
     * Create a new context and associates it with the threads, if there is any previous context, it is just
     * overwritten.
     * 
     * @param clazz
     * @return the context instance for the current Thread
     */
    public static Context create(final Map<String, String> testDataOfTestCase, Class<? extends Configuration> clazz)
    {
        return CONTEXTS.computeIfAbsent(Thread.currentThread(), (key) -> {
            return new Context(testDataOfTestCase, clazz);
        });
    }

    /**
     * Retrieves the context instance for the current Thread.
     * 
     * @param clazz
     * @return the context instance for the current Thread
     */
    public static Context get()
    {
        return CONTEXTS.computeIfAbsent(Thread.currentThread(),
                                        key -> {
                                            return new Context(Collections.emptyMap(), Configuration.class);
                                        });
            }

    /**
     * Current window height
     *
     * @return current height of the window, not viewport height!
     */
    public int getWindowHeight()
    {
        return driver.manage().window().getSize().getHeight();
    }

    /**
     * Current window width
     *
     * @return current width of the window, not viewport width!
     */
    public int getWindowWidth()
    {
        return driver.manage().window().getSize().getWidth();
    }

    /**
     * Phone or smaller
     */
    public boolean isMobile()
    {
        return getWindowWidth() < configuration.mediumDeviceBreakpoint();
    }

    /**
     * Tablet or large phone
     */
    public boolean isTablet()
    {
        return getWindowWidth() >= configuration.mediumDeviceBreakpoint()
               && getWindowWidth() < configuration.largeDeviceBreakpoint();
    }

    /**
     * Small desktop aka half window or stuff, can be tablet as well
     */
    public boolean isSmallDesktop()
    {
        return getWindowWidth() >= configuration.largeDeviceBreakpoint()
               && getWindowWidth() < configuration.xlargeDeviceBreakpoint();
    }

    /**
     * Large desktop resolution?
     */
    public boolean isLargeDesktop()
    {
        return getWindowWidth() >= configuration.xlargeDeviceBreakpoint();
    }

    /**
     * Desktop of any kind?
     */
    public boolean isDesktop()
    {
        return getWindowWidth() >= configuration.largeDeviceBreakpoint();
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
