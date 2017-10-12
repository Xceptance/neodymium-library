package com.xceptance.neodymium.multibrowser.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
    {
        "file:./config/browser.properties"
    })
public interface WebDriverProperties extends Config
{
    @Key("xlt.webDriver.window.width")
    @DefaultValue("-1")
    public Integer getWindowWidth();

    @Key("xlt.webDriver.window.height")
    @DefaultValue("-1")
    public Integer getWindowHeight();

    @Key("xlt.webDriver.firefox.legacyMode")
    public boolean useFirefoxLegacy();

    @Key("xlt.webDriver.reuseDriver")
    public boolean reuseWebDriver();

    @Key("xlt.webDriver.keepBrowserOpen")
    @DefaultValue("false")
    public boolean keepBrowserOpen();
}
