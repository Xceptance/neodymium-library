package com.xceptance.neodymium.module.statement.browser.multibrowser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browserup.bup.BrowserUpProxy;

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
    public static Logger LOGGER = LoggerFactory.getLogger(WebDriverCache.class);

    public static final WebDriverCache instance = new WebDriverCache();

    private static final Map<String, CachingContainer> cache = Collections.synchronizedMap(new HashMap<>());

    /**
     * The private constructor of the {@link WebDriverCache}. Creates the synchronized {@link HashMap} instance and
     * add's a VM shutdown hook to clean up the cache and close the cached {@link WebDriver} gracefully if the
     * neodymium.webDriver.keepBrowserOpen property is set to <code>false</code> in property file "browser.properties".
     * See config folder
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
        CachingContainer container = cache.get(browserTag);
        return container != null ? container.getWebDriver() : null;
    }

    /**
     * Put's the instance of a {@link WebDriver} into the cache and uses browserTag to reference it. If there is already
     * an {@link WebDriver} stored in the cache with the same browserTag {@link String} then the instance will be
     * overwritten.
     * 
     * @param browserTag
     *            a {@link String} that will be used to reference the cached {@link WebDriver}
     * @param webDriver
     *            an instance of {@link WebDriver} that should be stored in the cache
     * @param proxy
     *            an instance of {@link BrowserUpProxy} that should be stored in the cache this can be null if no local
     *            proxy is used
     */
    public void putWebDriverAndProxy(String browserTag, WebDriver webDriver, BrowserUpProxy proxy)
    {
        CachingContainer container = new CachingContainer();
        container.setWebDriver(webDriver);
        container.setProxy(proxy);
        cache.put(browserTag, container);
    }

    /**
     * Look's up the cache for the browserTag argument and removes {@link WebDriver} instance from cache and returns a
     * boolean indicating whether it was found and removed or not
     * 
     * @param browserTag
     *            a {@link String} that will be used to find the referenced {@link WebDriver} in the cache
     * @return {@link Boolean} indicating whether it was found and removed or not
     */
    public boolean removeWebDriverAndProxy(String browserTag)
    {
        return (getRemoveWebDriverAndProxy(browserTag) != null);
    }

    /**
     * Look's up the cache for the browserTag argument and removes {@link WebDriver} instance from cache and returns the
     * stored {@link WebDriver} instance if found.
     * 
     * @param browserTag
     *            The String used in {@link Browser} to reference a browser configuration
     * @return {@link WebDriver} if found, else <code>null</code>
     */
    public CachingContainer getRemoveWebDriverAndProxy(String browserTag)
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
    public boolean removeWebDriverAndProxy(WebDriver driver)
    {
        synchronized (cache)
        {
            boolean removed = false;
            for (Entry<String, CachingContainer> entry : cache.entrySet())
            {
                if (entry.getValue().getWebDriver() == driver)
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
     * @return unmodifiable {@link Collection} of all {@link WebDriver} that are currently in the {@link WebDriverCache}
     */
    public Collection<CachingContainer> getAllWebDriverAndProxy()
    {
        return Collections.unmodifiableCollection(cache.values());
    }

    /**
     * This function can be used within a function of a JUnit test case that is annotated with @AfterClass to clear the
     * WebDriverCache of the WebDrivers ready for reuse.
     * <p>
     * <b>Attention:</b> It is save to run this function during a sequential test execution. It can have repercussions
     * (e.g. longer test duration) in a parallel execution environment.
     *
     * <pre>
     * &#64;AfterClass
     * public void afterClass()
     * {
     *     WebDriverCache.quitCachedBrowsers();
     * }
     * </pre>
     **/
    public static void quitCachedBrowsers()
    {
        Collection<CachingContainer> allWebdriver = instance.getAllWebDriverAndProxy();
        for (CachingContainer cont : allWebdriver)
        {
            try
            {
                WebDriver wd = cont.getWebDriver();
                LOGGER.debug("Quit web driver: " + wd.toString());
                wd.quit();
                BrowserUpProxy proxy = cont.getProxy();
                if (proxy != null)
                {
                    proxy.stop();
                }
                instance.removeWebDriverAndProxy(wd);
            }
            catch (Exception e)
            {
                LOGGER.debug("Error on quitting web driver", e);
            }
        }
    }
}
