package com.xceptance.neodymium.module.statement.browser.multibrowser.configuration;

import java.util.Properties;

public class TestEnvironment
{
    private String url;

    private String username;

    private String password;

    protected TestEnvironment(Properties properties, String baseKey)
    {
        url = properties.getProperty(baseKey + ".url");
        username = properties.getProperty(baseKey + ".username");
        password = properties.getProperty(baseKey + ".password");
    }

    public String getUrl()
    {
        return url;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

}
