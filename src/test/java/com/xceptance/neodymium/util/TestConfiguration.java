package com.xceptance.neodymium.util;

import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Mutable;

@LoadPolicy(LoadType.MERGE)
@Sources(
{
  "system:env",
  "system:properties",
  "file:config/dev-testconfiguration.properties"
})
public interface TestConfiguration extends Mutable
{
    @Key("browserstack_username")
    public String browserstackUsername();

    @Key("browserstack_password")
    public String browserstackPassword();
}
