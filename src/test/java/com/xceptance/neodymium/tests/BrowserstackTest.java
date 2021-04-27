package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import com.xceptance.neodymium.testclasses.multibrowser.BrowserstackHomePageTest;
import com.xceptance.neodymium.util.TestConfiguration;

public class BrowserstackTest extends NeodymiumTest
{
    private static final TestConfiguration CONFIGURATION = ConfigFactory.create(TestConfiguration.class);

    @BeforeClass
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties1 = new HashMap<>();
        properties1.put("browserprofile.testEnvironment.browserstack.url", "https://hub-cloud.browserstack.com/wd/hub");
        properties1.put("browserprofile.testEnvironment.browserstack.username", CONFIGURATION.browserstackUsername());
        properties1.put("browserprofile.testEnvironment.browserstack.password", CONFIGURATION.browserstackApiKey());
        File tempConfigFile1 = new File("./config/credentials.properties");
        writeMapToPropertiesFile(properties1, tempConfigFile1);
        tempFiles.add(tempConfigFile1);

        Map<String, String> properties3 = new HashMap<>();
        properties3.put("browserprofile.Safari_Browserstack.name", "Safari Browserstack");
        properties3.put("browserprofile.Safari_Browserstack.platform", "OS X");
        properties3.put("browserprofile.Safari_Browserstack.platformVersion", "Big Sur");
        properties3.put("browserprofile.Safari_Browserstack.browserName", "Safari");
        properties3.put("browserprofile.Safari_Browserstack.version", "14.0");
        properties3.put("browserprofile.Safari_Browserstack.testEnvironment", "browserstack");
        File tempConfigFile3 = new File("./config/dev-browser.properties");
        writeMapToPropertiesFile(properties3, tempConfigFile3);
        tempFiles.add(tempConfigFile3);
    }

    @Test
    public void testBrowserstack()
    {
        Result result = JUnitCore.runClasses(BrowserstackHomePageTest.class);
        checkPass(result, 1, 0);
    }
}
