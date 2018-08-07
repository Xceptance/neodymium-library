package com.xceptance.neodymium.module.statement.browser.multibrowser.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources(
{
  "file:config/neodymium.properties", "file:config/proxy.properties"
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

    @Key("neodymium.proxy.bypassForHosts")
    public String getBypass();

    @Key("neodymium.proxy.socket.version")
    public Integer getSocketVersion();

    @Key("neodymium.proxy.socket.userName")
    public String getSocketUsername();

    @Key("neodymium.proxy.socket.password")
    public String getSocketPassword();
}
