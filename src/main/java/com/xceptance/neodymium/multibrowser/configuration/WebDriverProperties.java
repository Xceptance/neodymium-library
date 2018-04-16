package com.xceptance.neodymium.multibrowser.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
    {
        "file:./config/browser.properties"
    })
public interface WebDriverProperties extends Config
{
    @Key("neodymium.webDriver.window.width")
    @DefaultValue("-1")
    public Integer getWindowWidth();

    @Key("neodymium.webDriver.window.height")
    @DefaultValue("-1")
    public Integer getWindowHeight();

    @Key("neodymium.webDriver.firefox.legacyMode")
    @DefaultValue("false")
    public boolean useFirefoxLegacy();

    @Key("neodymium.webDriver.reuseDriver")
    @DefaultValue("false")
    public boolean reuseWebDriver();

    @Key("neodymium.webDriver.keepBrowserOpen")
    @DefaultValue("false")
    public boolean keepBrowserOpen();
}
