package com.xceptance.multibrowser.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

import com.xceptance.multibrowser.dto.BrowserConfigurationDto;
import com.xceptance.multibrowser.mapper.PropertiesToBrowserConfigurationMapper;

public class MultibrowserConfiguration
{
    private static final String TEST_ENVIRONMENT_FILE = "./config/credentials.properties";

    private static final String BROWSER_PROFILE_FILE = "./config/browser.properties";

    private static final String BROWSER_PROFILE_PREFIX = "browserprofile.";

    private static final String TEST_ENVIRONMENT_PREFIX = BROWSER_PROFILE_PREFIX + "testenvironment.";

    private DriverServerPath driverServerPath;

    private WebDriverProperties webDriverProperties;

    private Map<String, TestEnvironment> testEnvironments;

    private Map<String, BrowserConfigurationDto> browserProfiles;

    private Properties testEnvironmentProperties;

    private Properties browserProfileProperties;

    private MultibrowserConfiguration()
    {
        testEnvironmentProperties = new Properties();
        browserProfileProperties = new Properties();
        try
        {
            testEnvironmentProperties.load(new FileInputStream(new File(TEST_ENVIRONMENT_FILE)));
            browserProfileProperties.load(new FileInputStream(new File(BROWSER_PROFILE_FILE)));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        parseTestEnvironments();
        parseBrowserProfiles();

        driverServerPath = ConfigFactory.create(DriverServerPath.class);
        webDriverProperties = ConfigFactory.create(WebDriverProperties.class);
    }

    private void parseTestEnvironments()
    {
        testEnvironments = new LinkedHashMap<String, TestEnvironment>();
        List<String> testEnvironmentKeys = getSubkeysForPrefix(testEnvironmentProperties, TEST_ENVIRONMENT_PREFIX);

        for (String testEnvironmentKey : testEnvironmentKeys)
        {
            testEnvironments.put(testEnvironmentKey,
                                 new TestEnvironment(testEnvironmentProperties, TEST_ENVIRONMENT_PREFIX + testEnvironmentKey));
        }
    }

    private void parseBrowserProfiles()
    {
        browserProfiles = new LinkedHashMap<String, BrowserConfigurationDto>();
        List<String> browserProfileKeys = getSubkeysForPrefix(browserProfileProperties, BROWSER_PROFILE_PREFIX);

        PropertiesToBrowserConfigurationMapper mapper = new PropertiesToBrowserConfigurationMapper();

        for (String browserProfile : browserProfileKeys)
        {
            List<String> subkeysForPrefix = getSubkeysForPrefix(browserProfileProperties, BROWSER_PROFILE_PREFIX + browserProfile + ".");
            Map<String, String> browserProfileConfiguration = new HashMap<>();

            for (String subkey : subkeysForPrefix)
            {
                String value = (String) browserProfileConfiguration.get(BROWSER_PROFILE_PREFIX + browserProfile + "." + subkey);
                browserProfileConfiguration.put(subkey, value);
            }
            browserProfiles.put(browserProfile, mapper.toDto(browserProfileConfiguration));
        }
    }

    private List<String> getSubkeysForPrefix(Properties properties, String prefix)
    {
        List<String> keys = new LinkedList<String>();

        for (String key : properties.keySet().toArray(new String[] {}))
        {
            if (key.toLowerCase().startsWith(prefix.toLowerCase())) // TODO: lower case compare is wrong!
            {
                key = key.substring(prefix.length());
                String[] split = key.split("\\.");
                if (split != null && split.length > 0)
                {
                    String newKey = split[0];
                    if (StringUtils.isNotBlank(newKey) && !keys.contains(newKey))
                    {
                        keys.add(newKey);
                    }
                }
            }
        }

        return keys;
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
