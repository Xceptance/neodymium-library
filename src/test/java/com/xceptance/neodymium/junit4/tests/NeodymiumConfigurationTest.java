package com.xceptance.neodymium.junit4.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.junit4.testclasses.config.SystemPropertyTest;

public class NeodymiumConfigurationTest extends NeodymiumTest
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
        Result result = JUnitCore.runClasses(SystemPropertyTest.class);
        checkPass(result, 1, 0);
    }
}
