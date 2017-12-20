package com.xceptance.neodymium.multibrowser.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
    {
        "file:./config/default.properties"
    })
public interface ProxyConfiguration extends Config
{
    @Key("neodymium.proxy")
    @DefaultValue("false")
    public boolean useProxy();

    @Key("neodymium.proxy.host")
    public String getHost();

    @Key("neodymium.proxy.port")
    public String getPort();

    @Key("neodymium.proxy.userName")
    public String getUsername();

    @Key("neodymium.proxy.password")
    public String getPassword();

    @Key("neodymium.proxy.bypassForHosts")
    public String getBypass();
}
