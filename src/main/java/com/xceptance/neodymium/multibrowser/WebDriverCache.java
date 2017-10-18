package com.xceptance.neodymium.multibrowser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;

public class WebDriverCache
{
    Map<String, WebDriver> cache;

    private WebDriverCache()
    {
        cache = new HashMap<>();
        Runtime.getRuntime().addShutdownHook(new WebDriverCacheCleaner());
    }

    private static class WebDriverCacheHolder
    {
        private static final WebDriverCache INSTANCE = new WebDriverCache();
    }

    public static WebDriverCache getIntance()
    {
        return WebDriverCacheHolder.INSTANCE;
    }

    public WebDriver getWebDriverForTag(String browserTag)
    {
        synchronized (cache)
        {
            return cache.get(browserTag);
        }
    }

    public void putWebDriverForTag(String browserTag, WebDriver webDriver)
    {
        synchronized (cache)
        {
            cache.put(browserTag, webDriver);
        }
    }

    public boolean removeWebDriver(String browserTag)
    {
        synchronized (cache)
        {
            return (removeGetWebDriver(browserTag) != null);
        }
    }

    public WebDriver removeGetWebDriver(String browserTag)
    {
        synchronized (cache)
        {
            return cache.remove(browserTag);
        }
    }

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

    public Collection<WebDriver> getAllWebdriver()
    {
        synchronized (cache)
        {
            return Collections.unmodifiableCollection(cache.values());
        }
    }
}
