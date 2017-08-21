package com.xceptance.multibrowser.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
    {
        "file:./config/browser.properties"
    })
public interface WebDriverProperties extends Config
{
    @Key("xlt.webDriver.window.width")
    public Integer getWindowWidth();

    @Key("xlt.webDriver.window.height")
    public Integer getWindowHeight();

    @Key("xlt.webDriver.firefox.legacyMode")
    public boolean useFirefoxLegacy();

    @Key("xlt.webDriver.reuseDriver")
    public boolean reuseWebDriver();
}
