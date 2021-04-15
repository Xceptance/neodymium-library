package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import com.xceptance.neodymium.testclasses.multibrowser.BrowserstackHomePageTest;

public class BrowserstackTest extends NeodymiumTest
{
    @BeforeClass
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties1 = new HashMap<>();
        properties1.put("browserprofile.testEnvironment.browserstack.url", "https://hub-cloud.browserstack.com/wd/hub");
        properties1.put("browserprofile.testEnvironment.browserstack.username", System.getenv("BROWSERSTACK_USERNAME"));
        properties1.put("browserprofile.testEnvironment.browserstack.password",  System.getenv("BROWSERSTACK_PASSWORD"));
        File tempConfigFile1 = new File("./config/credentials.properties");
        writeMapToPropertiesFile(properties1, tempConfigFile1);
        tempFiles.add(tempConfigFile1);

        Map<String, String> properties3 = new HashMap<>();
        properties3.put("browserprofile.safari_remote.name", "safari_remote");
        properties3.put("browserprofile.safari_remote.platform", "OS X");
        properties3.put("browserprofile.safari_remote.platformVersion", "Big Sur");
        properties3.put("browserprofile.safari_remote.browserName", "Safari");
        properties3.put("browserprofile.safari_remote.version", "14.0");
        properties3.put("browserprofile.safari_remote.testEnvironment", "browserstack");
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
