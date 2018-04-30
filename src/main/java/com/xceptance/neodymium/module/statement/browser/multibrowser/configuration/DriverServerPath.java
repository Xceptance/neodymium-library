package com.xceptance.neodymium.module.statement.browser.multibrowser.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
    {
        "file:${configurationFile}"
    })
public interface DriverServerPath extends Config
{
    @Key("neodymium.webDriver.chrome.pathToDriverServer")
    public String getChromeDriverPath();

    @Key("neodymium.webDriver.edge.pathToDriverServer")
    public String getEdgeDriverPath();

    @Key("neodymium.webDriver.firefox.pathToDriverServer")
    public String getFirefoxDriverPath();

    @Key("neodymium.webDriver.ie.pathToDriverServer")
    public String getIeDriverPath();

    @Key("neodymium.webDriver.opera.pathToDriverServer")
    public String getOperaDriverPath();

    @Key("neodymium.webDriver.phantomjs.pathToDriverServer")
    public String getPhantomJsDriverPath();

    @Key("neodymium.webDriver.chrome.pathToBrowser")
    public String getChromeBrowserPath();

    @Key("neodymium.webDriver.firefox.pathToBrowser")
    public String getFirefoxBrowserPath();

    @Key("neodymium.webDriver.opera.pathToBrowser")
    public String getOperaBrowserPath();
}
