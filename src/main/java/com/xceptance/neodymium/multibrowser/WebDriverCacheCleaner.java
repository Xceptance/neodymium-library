package com.xceptance.neodymium.multibrowser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverCacheCleaner extends Thread
{

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverCacheCleaner.class);

    @Override
    public void run()
    {
        LOGGER.debug("All tests finished. Quit cached browser");
        BrowserRunner.quitCachedBrowser();
    }
}
