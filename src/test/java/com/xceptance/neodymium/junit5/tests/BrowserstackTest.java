package com.xceptance.neodymium.junit5.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.xceptance.neodymium.junit5.testclasses.multibrowser.BrowserstackHomePageTest;
import com.xceptance.neodymium.junit5.tests.utils.NeodymiumTestExecutionSummary;
import com.xceptance.neodymium.util.TestConfiguration;

public class BrowserstackTest extends AbstractNeodymiumTest
{
    private static final TestConfiguration CONFIGURATION = ConfigFactory.create(TestConfiguration.class);

    @BeforeAll
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties1 = new HashMap<>();
        properties1.put("browserprofile.testEnvironment.browserstack.url", "https://hub-cloud.browserstack.com/wd/hub");
        properties1.put("browserprofile.testEnvironment.browserstack.username", CONFIGURATION.browserstackUsername());
        properties1.put("browserprofile.testEnvironment.browserstack.password", CONFIGURATION.browserstackAccessKey());
        
        File tempConfigFile1 = new File("./config/credentials.properties");
        writeMapToPropertiesFile(properties1, tempConfigFile1);
        tempFiles.add(tempConfigFile1);

        Map<String, String> properties2 = new HashMap<>();
        properties2.put("browserprofile.Safari_Browserstack.name", "Safari Browserstack");
        properties2.put("browserprofile.Safari_Browserstack.platform", "OS X");
        properties2.put("browserprofile.Safari_Browserstack.platformVersion", "Big Sur");
        properties2.put("browserprofile.Safari_Browserstack.browserName", "Safari");
        properties2.put("browserprofile.Safari_Browserstack.version", "14.0");
        properties2.put("browserprofile.Safari_Browserstack.testEnvironment", "browserstack");
        File tempConfigFile2 = new File("./config/dev-browser.properties");
        writeMapToPropertiesFile(properties2, tempConfigFile2);
        tempFiles.add(tempConfigFile2);
    }

    @Test
    public void testBrowserstack()
    {
        NeodymiumTestExecutionSummary summary = run(BrowserstackHomePageTest.class);
        checkPass(summary, 1, 0);
    }
}
