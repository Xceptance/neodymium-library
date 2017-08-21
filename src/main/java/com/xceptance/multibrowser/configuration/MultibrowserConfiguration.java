package com.xceptance.multibrowser.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

public class MultibrowserConfiguration
{
    private static final String TEST_ENVIRONMENT_FILE = "./config/credentials.properties";

    private static final String TEST_ENVIRONMENT_PREFIX = "browserprofile.testEnvironment.";

    private DriverServerPath driverServerPath;

    private WebDriverProperties webDriverProperties;

    private Map<String, TestEnvironment> testEnvironments;

    private MultibrowserConfiguration()
    {
        driverServerPath = ConfigFactory.create(DriverServerPath.class);
        webDriverProperties = ConfigFactory.create(WebDriverProperties.class);
        parseTestEnvironments();
    }

    private void parseTestEnvironments()
    {
        Properties properties = new Properties();
        try
        {
            properties.load(new FileInputStream(new File(TEST_ENVIRONMENT_FILE)));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        testEnvironments = new LinkedHashMap<String, TestEnvironment>();
        List<String> testEnvironmentKeys = new LinkedList<String>();
        for (Object keyObject : properties.keySet())
        {
            String key = (String) keyObject;
            if (key.toLowerCase().startsWith(TEST_ENVIRONMENT_PREFIX))
            {
                String[] split = key.split("\\.");
                if (split != null && split.length > 2 && StringUtils.isNotBlank(split[2]) && !testEnvironmentKeys.contains(split[2]))
                {
                    testEnvironmentKeys.add(split[2]);
                }
            }
        }

        for (String testEnvironmentKey : testEnvironmentKeys)
        {
            testEnvironments.put(testEnvironmentKey, new TestEnvironment(properties, TEST_ENVIRONMENT_PREFIX + testEnvironmentKey));
        }
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

    public TestEnvironment getTestEnvironment(String environment)
    {
        return testEnvironments.get(environment);
    }
}
