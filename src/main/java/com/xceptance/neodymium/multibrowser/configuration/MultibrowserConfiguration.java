package com.xceptance.neodymium.multibrowser.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Factory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultibrowserConfiguration
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MultibrowserConfiguration.class);

    private static final Map<String, MultibrowserConfiguration> CONFIGURATIONS = Collections.synchronizedMap(new LinkedHashMap<>());

    private static final String TEST_ENVIRONMENT_FILE = "./config/credentials.properties";

    private static final String BROWSER_PROFILE_PREFIX = "browserprofile.";

    private static final String TEST_ENVIRONMENT_PREFIX = BROWSER_PROFILE_PREFIX + "testEnvironment.";

    private static final String DEFAULT_BROWSER_PROFILE_FILE = "./config/browser.properties";

    private DriverServerPath driverServerPath;

    private WebDriverProperties webDriverProperties;

    private ProxyConfiguration proxyConfiguration;

    private Map<String, TestEnvironment> testEnvironments;

    private Map<String, BrowserConfiguration> browserProfiles;

    private Properties testEnvironmentProperties;

    private Properties browserProfileProperties;

    private MultibrowserConfiguration(String configFile)
    {
        testEnvironmentProperties = new Properties();
        browserProfileProperties = new Properties();
        try
        {
            File testEnvironmentFile = new File(TEST_ENVIRONMENT_FILE);
            if (testEnvironmentFile.exists())
            {
                FileInputStream fileInputStream = new FileInputStream(testEnvironmentFile);
                testEnvironmentProperties.load(fileInputStream);
                fileInputStream.close();
            }

            File browerProfileFile = new File(configFile);
            if (browerProfileFile.exists())
            {
                FileInputStream fileInputStream = new FileInputStream(browerProfileFile);
                browserProfileProperties.load(fileInputStream);
                fileInputStream.close();
            }

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        parseTestEnvironments();
        parseBrowserProfiles();

        Factory configurationFactory = ConfigFactory.newInstance();
        configurationFactory.setProperty("configurationFile", configFile);
        driverServerPath = configurationFactory.create(DriverServerPath.class);
        webDriverProperties = configurationFactory.create(WebDriverProperties.class);
        proxyConfiguration = configurationFactory.create(ProxyConfiguration.class);
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
            browserProfileConfiguration.put("browserTag", browserProfile);
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

    public static MultibrowserConfiguration getInstance()
    {
        if (CONFIGURATIONS.size() == 0)
        {
            LOGGER.debug(MessageFormat.format("No multi-browser configuration loaded. Load default configuration from ''{0}''",
                                              DEFAULT_BROWSER_PROFILE_FILE));
            getInstance(DEFAULT_BROWSER_PROFILE_FILE);
        }

        return CONFIGURATIONS.entrySet().iterator().next().getValue();
    }

    /**
     * Returns an {@link MultibrowserConfiguration} parsed from an properties file
     * 
     * @param configFile
     *            a relative path to the file containing browser configuration (properties)
     * @return {@link MultibrowserConfiguration}
     */
    public static MultibrowserConfiguration getInstance(String configFile)
    {
        MultibrowserConfiguration configuration = CONFIGURATIONS.get(configFile);

        if (configuration == null)
        {
            configuration = new MultibrowserConfiguration(configFile);
            CONFIGURATIONS.put(configFile, configuration);
        }
        return configuration;
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
