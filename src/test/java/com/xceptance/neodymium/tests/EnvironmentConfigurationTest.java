package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.multibrowser.EnvironmentsConfiguration;

public class EnvironmentConfigurationTest extends NeodymiumTest
{
    public static final String URL = "urltomylittleselenium.grid";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String URL2 = "urltomysecondselenium.grid";

    public static final String USERNAME2 = "anotherUsername";

    public static final String PASSWORD2 = "anotherPassword";

    @BeforeClass
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("browserprofile.testEnvironment.unittest.url", URL);
        properties.put("browserprofile.testEnvironment.unittest.username", USERNAME);
        properties.put("browserprofile.testEnvironment.unittest.password", PASSWORD);

        properties.put("browserprofile.testEnvironment.override.url", URL);
        properties.put("browserprofile.testEnvironment.override.username", USERNAME);
        properties.put("browserprofile.testEnvironment.override.password", PASSWORD);

        File tempConfigFile1 = new File("./config/credentials.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);

        Map<String, String> properties2 = new HashMap<>();
        properties2.put("browserprofile.testEnvironment.override.url", URL2);
        properties2.put("browserprofile.testEnvironment.override.username", USERNAME2);
        properties2.put("browserprofile.testEnvironment.override.password", PASSWORD2);

        File tempConfigFile2 = new File("./config/dev-credentials.properties");
        writeMapToPropertiesFile(properties2, tempConfigFile2);
        tempFiles.add(tempConfigFile2);
    }

    @Test
    public void testEnvironments()
    {
        // test environment configuration
        Result result = JUnitCore.runClasses(EnvironmentsConfiguration.class);
        checkPass(result, 3, 0, 0);
    }
}
