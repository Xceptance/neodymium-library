package com.xceptance.multibrowser.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
    {
        "file:./config/default.properties"
    })
public interface ProxyConfiguration extends Config
{
    @Key("com.xceptance.xlt.proxy.host")
    public String getHost();

    @Key("com.xceptance.xlt.proxy.port")
    public String getPort();

    @Key("com.xceptance.xlt.proxy.userName")
    public String getUsername();

    @Key("com.xceptance.xlt.proxy.password")
    public String getPassword();

    @Key("com.xceptance.xlt.proxy.bypassForHosts")
    public String getBypass();
}
