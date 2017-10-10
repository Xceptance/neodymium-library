package com.xceptance.neodymium.multibrowser.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

public class MultibrowserConfiguration
{
    private static final String TEST_ENVIRONMENT_FILE = "./config/credentials.properties";

    private static final String BROWSER_PROFILE_FILE = "./config/browser.properties";

    private static final String BROWSER_PROFILE_PREFIX = "browserprofile.";

    private static final String TEST_ENVIRONMENT_PREFIX = BROWSER_PROFILE_PREFIX + "testenvironment.";

    private DriverServerPath driverServerPath;

    private WebDriverProperties webDriverProperties;

    private ProxyConfiguration proxyConfiguration;

    private Map<String, TestEnvironment> testEnvironments;

    private Map<String, BrowserConfiguration> browserProfiles;

    private Properties testEnvironmentProperties;

    private Properties browserProfileProperties;

    private MultibrowserConfiguration()
    {
        testEnvironmentProperties = new Properties();
        browserProfileProperties = new Properties();
        try
        {
            File testEnvironmentFile = new File(TEST_ENVIRONMENT_FILE);
            if (testEnvironmentFile.exists())
            {
                testEnvironmentProperties.load(new FileInputStream(testEnvironmentFile));
            }

            File browerProfileFile = new File(BROWSER_PROFILE_FILE);
            if (browerProfileFile.exists())
            {
                browserProfileProperties.load(new FileInputStream(browerProfileFile));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        parseTestEnvironments();
        parseBrowserProfiles();

        driverServerPath = ConfigFactory.create(DriverServerPath.class);
        webDriverProperties = ConfigFactory.create(WebDriverProperties.class);
        proxyConfiguration = ConfigFactory.create(ProxyConfiguration.class);
    }

    private void parseTestEnvironments()
    {
        testEnvironments = new LinkedHashMap<String, TestEnvironment>();
        Set<String> testEnvironmentKeys = getSubkeysForPrefix(testEnvironmentProperties, TEST_ENVIRONMENT_PREFIX);

        for (String testEnvironmentKey : testEnvironmentKeys)
        {
            testEnvironments.put(testEnvironmentKey,
                                 new TestEnvironment(testEnvironmentProperties, TEST_ENVIRONMENT_PREFIX + testEnvironmentKey));
        }
    }

    private void parseBrowserProfiles()
    {
        browserProfiles = new LinkedHashMap<String, BrowserConfiguration>();
        Set<String> browserProfileKeys = getSubkeysForPrefix(browserProfileProperties, BROWSER_PROFILE_PREFIX);

        BrowserConfigurationMapper mapper = new BrowserConfigurationMapper();

        for (String browserProfile : browserProfileKeys)
        {
            Set<String> subkeysForPrefix = getSubkeysForPrefix(browserProfileProperties, BROWSER_PROFILE_PREFIX + browserProfile + ".");
            Map<String, String> browserProfileConfiguration = new HashMap<>();

            for (String subkey : subkeysForPrefix)
            {
                String value = (String) browserProfileProperties.get(BROWSER_PROFILE_PREFIX + browserProfile + "." + subkey);
                browserProfileConfiguration.put(subkey, value);
            }
            browserProfiles.put(browserProfile, mapper.map(browserProfileConfiguration));
        }
    }

    private Set<String> getSubkeysForPrefix(Properties properties, String prefix)
    {
        Set<String> keys = new HashSet<String>();

        for (Object key : properties.keySet())
        {
            String keyString = (String) key;
            if (keyString.toLowerCase().startsWith(prefix.toLowerCase())) // TODO: lower case compare is wrong!
            {
                // cut off prefix
                keyString = keyString.substring(prefix.length());

                // split on the next dots
                String[] split = keyString.split("\\.");
                if (split != null && split.length > 0)
                {
                    // the first entry in the resulting array will be the key we are searching for
                    String newKey = split[0];
                    if (StringUtils.isNotBlank(newKey))
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

    public Map<String, BrowserConfiguration> getBrowserProfiles()
    {
        return browserProfiles;
    }

    public ProxyConfiguration getProxyConfiguration()
    {
        return proxyConfiguration;
    }
}
