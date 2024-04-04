package com.xceptance.neodymium.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_headless")
@Browser("FF_headless")
public class DriverArgumentsTest extends NeodymiumTest
{
    private static String randomLogFileName = "target/" + UUID.randomUUID().toString() + ".log";

    @BeforeClass
    public static void createSettings()
    {
        Map<String, String> properties1 = new HashMap<>();
        properties1.put("neodymium.webDriver.chrome.driverArguments", "--silent ; --log-path=" + randomLogFileName);
        properties1.put("neodymium.webDriver.firefox.driverArguments", "--log ; info ; --log-path=" + randomLogFileName);
        File tempConfigFile1 = new File("./config/dev-neodymium.properties");
        tempFiles.add(tempConfigFile1);
        writeMapToPropertiesFile(properties1, tempConfigFile1);
    }

    @Test
    public void test()
    {
        Selenide.open("https://www.xceptance.com/en/");
        Assert.assertTrue("No log file found", new File(randomLogFileName).exists());
    }

    @After
    public void cleanup()
    {
        new File(randomLogFileName).delete();
    }
}
