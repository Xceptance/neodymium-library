package com.xceptance.neodymium.util;

import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources(
    {
        "file:config/myconfig.properties"
    })
public interface MyConfig extends Configuration // extends Config //
{
    // @DefaultValue("3000")
    @Key("selenide.timeout")
    public long timeout();

    @Key("myconfig.works")
    public boolean works();
}
