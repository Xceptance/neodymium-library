package com.xceptance.xrunner;

import org.aeonbits.owner.ConfigFactory;
import org.junit.Test;

import com.xceptance.neodymium.util.Configuration;
import com.xceptance.neodymium.util.MyConfig;

public class TestConfiguration
{
    @Test
    public void testOverride()
    {
        MyConfig config = ConfigFactory.create(MyConfig.class, System.getProperties(), System.getenv());
        System.out.println(config.timeout());
        System.out.println(config.works());

        Configuration parentConfig = config;
        System.out.println(parentConfig.timeout());

        // Context.initializeUserConfiguration(MyConfig.class);
        // MyConfig userConfiguration = Context.getUserConfiguration(MyConfig.class);
        //
        // System.out.println(userConfiguration.works());
    }
}
