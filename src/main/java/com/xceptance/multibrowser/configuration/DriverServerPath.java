package com.xceptance.multibrowser.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
    {
        "file:./config/browser.properties"
    })
public interface DriverServerPath extends Config
{
    @Key("xlt.webDriver.chrome.pathToDriverServer")
    public String getChromeDriverPath();

    @Key("xlt.webDriver.edge.pathToDriverServer")
    public String getEdgeDriverPath();

    @Key("xlt.webDriver.firefox.pathToDriverServer")
    public String getFirefoxDriverPath();

    @Key("xlt.webDriver.ie.pathToDriverServer")
    public String getIeDriverPath();

    @Key("xlt.webDriver.opera.pathToDriverServer")
    public String getOperaDriverPath();

    @Key("xlt.webDriver.phantomjs.pathToDriverServer")
    public String getPhantomJsDriverPath();

    @Key("xlt.webDriver.chrome.pathToBrowser")
    public String getChromeBrowserPath();

    @Key("xlt.webDriver.firefox.pathToBrowser")
    public String getFirefoxBrowserPath();

    @Key("xlt.webDriver.opera.pathToBrowser")
    public String getOperaBrowserPath();
}
