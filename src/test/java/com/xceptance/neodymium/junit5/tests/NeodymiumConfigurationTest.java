package com.xceptance.neodymium.junit5.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.neodymium.junit5.testclasses.config.SystemPropertyTest;
import com.xceptance.neodymium.junit5.tests.utils.NeodymiumTestExecutionSummary;

public class NeodymiumConfigurationTest extends AbstractNeodymiumTest
{
    @BeforeClass
    public static void beforeClass()
    {
        System.setProperty("neodymium.webDriver.window.height", "1000");
        Map<String, String> properties1 = new HashMap<>();
        properties1.put("neodymium.webDriver.window.height", "500");
        File tempConfigFile1 = new File("./config/neodymium.properties");
        writeMapToPropertiesFile(properties1, tempConfigFile1);
    }

    @Test
    public void testNeoConfig()
    {
        NeodymiumTestExecutionSummary summary = run(SystemPropertyTest.class);
        checkPass(summary, 1, 0);
    }
}
