package com.xceptance.neodymium.multibrowser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;

/**
 * A cache to hold different instances of {@link WebDriver}. Instances are kept in a synchronized {@link HashMap} which
 * are indexed by an "browserTag" {@link String}. That browserTag is a unique character sequence. All access to the
 * cache will be done in a synchronized way. The constructor adds an VM shutdown hook to clean up the cache and close
 * the cached {@link WebDriver} gracefully.
 * 
 * @author m.kaufmann
 */
public class WebDriverCache
{
    public static final WebDriverCache instance = new WebDriverCache();

    private static final Map<String, WebDriver> cache = Collections.synchronizedMap(new HashMap<>());

    /**
     * The private constructor of the {@link WebDriverCache}. Creates the synchronized {@link HashMap} instance and add's a
     * VM shutdown hook to clean up the cache and close the cached {@link WebDriver} gracefully if the
     * neodymium.webDriver.keepBrowserOpen property is set to <code>false</code> in property file "browser.properties". See
     * config folder
     */
    private WebDriverCache()
    {
        Runtime.getRuntime().addShutdownHook(new WebDriverCacheCleanupHook());
    }

    /**
     * Look's up the cache for a {@link WebDriver} that is referenced with the argument browserTag
     * 
     * @param browserTag
     *            a {@link String} that will be used find a referenced {@link WebDriver} instance in the cache
     * @return the {@link WebDriver} if one was found in the cache, else <code>null</code>
     */
    public WebDriver getWebDriverForBrowserTag(String browserTag)
    {
        return cache.get(browserTag);
    }

    /**
     * Put's the instance of a {@link WebDriver} into the cache and uses browserTag to reference it. If there is already an
     * {@link WebDriver} stored in the cache with the same browsreTag {@link String} then the instance will be overwritten.
     * 
     * @param browserTag
     *            a {@link String} that will be used to reference the cached {@link WebDriver}
     * @param webDriver
     *            an instance of {@link WebDriver} that should be stored in the cache
     */
    public void putWebDriver(String browserTag, WebDriver webDriver)
    {
        cache.put(browserTag, webDriver);
    }

    /**
     * Look's up the cache for the browserTag argument and removes {@link WebDriver} instance from cache and returns a
     * boolean indicating whether it was found and removed or not
     * 
     * @param browserTag
     *            a {@link String} that will be used to find the referenced {@link WebDriver} in the cache
     * @return {@link Boolean} indicating whether it was found and removed or not
     */
    public boolean removeWebDriver(String browserTag)
    {
        return (getRemoveWebDriver(browserTag) != null);
    }

    /**
     * Look's up the cache for the browserTag argument and removes {@link WebDriver} instance from cache and returns the
     * stored {@link WebDriver} instance if found.
     * 
     * @param browserTag
     * @return {@link WebDriver} if found, else <code>null</code>
     */
    public WebDriver getRemoveWebDriver(String browserTag)
    {
        return cache.remove(browserTag);
    }

    /**
     * Looks up cache for argument {@link WebDriver} and removes it from cache.
     * 
     * @param driver
     *            an instance of {@link WebDriver}
     * @return {@link Boolean} which indicates if the {@link WebDriver} was found and removed from cache.
     */
    public boolean removeWebDriver(WebDriver driver)
    {
        synchronized (cache)
        {
            boolean removed = false;
            for (Entry<String, WebDriver> entry : cache.entrySet())
            {
                if (entry.getValue() == driver)
                {
                    cache.remove(entry.getKey());
                    removed = true;
                }
            }

            return removed;
        }
    }

    /**
     * Retrieves a unmodifiable copy of all cached {@link WebDriver}
     * 
     * @return {@link Collection}<{@link WebDriver}>
     */
    public Collection<WebDriver> getAllWebdriver()
    {
        return Collections.unmodifiableCollection(cache.values());
    }
}
