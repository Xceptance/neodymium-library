package com.xceptance.multibrowser.configuration;

import org.aeonbits.owner.ConfigFactory;

public class MultibrowserConfiguration
{
    private DriverServerPath driverServerPath;

    private WebDriverProperties webDriverProperties;

    private MultibrowserConfiguration()
    {
        driverServerPath = ConfigFactory.create(DriverServerPath.class);
        webDriverProperties = ConfigFactory.create(WebDriverProperties.class);
    }

    private static class MultibrowserConfigurationHolder
    {
        private static final MultibrowserConfiguration INSTANCE = new MultibrowserConfiguration();
    }

    public static MultibrowserConfiguration getIntance()
    {
        return MultibrowserConfigurationHolder.INSTANCE;
    }

    public DriverServerPath getDriverServerPath()
    {
        return driverServerPath;
    }

    public WebDriverProperties getWebDriverProperties()
    {
        return webDriverProperties;
    }
}
