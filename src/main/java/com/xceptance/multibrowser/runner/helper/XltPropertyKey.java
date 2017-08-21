package com.xceptance.multibrowser.runner.helper;

public abstract class XltPropertyKey
{
    public static final String PROXY            = "com.xceptance.xlt.proxy";
    public static final String PROXY_HOST       = "com.xceptance.xlt.proxy.host";
    public static final String PROXY_PORT       = "com.xceptance.xlt.proxy.port";
    public static final String PROXY_USERNAME   = "com.xceptance.xlt.proxy.userName";
    public static final String PROXY_PASSWORD   = "com.xceptance.xlt.proxy.password";
    public static final String PROXY_BYPASS     = "com.xceptance.xlt.proxy.bypassForHosts";

    public static final String WEBDRIVER_PATH_IE        = "xlt.webDriver.ie.pathToDriverServer";
    public static final String WEBDRIVER_PATH_CHROME    = "xlt.webDriver.chrome.pathToDriverServer";
    public static final String WEBDRIVER_PATH_FIREFOX   = "xlt.webDriver.firefox.pathToDriverServer";

    public static final String BROWSERPROFILE_TEST_ENVIRONMENT = "browserprofile.testEnvironment.";
    
    public static final String DRIVER_USE_MARIONETTE = "webdriver.firefox.marionette";
    public static final String WEBDRIVER_FIREFOX_LEGACY = "xlt.webDriver.firefox.legacyMode";
    
    public static final String CHROME_PATH = "xlt.webDriver.chrome.pathToBrowser";
    public static final String FIREFOX_PATH = "xlt.webDriver.firefox.pathToBrowser";
}
