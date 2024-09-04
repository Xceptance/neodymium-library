package com.xceptance.neodymium.junit4.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.junit4.testclasses.webDriver.DriverArgumentsConfigTest;

public class DriverArgumentsTest extends NeodymiumTest
{
    public static String logFileNameChrome = "target/" + UUID.randomUUID().toString() + "chrome.log";

    public static String logFileNameFirefox = "target/" + UUID.randomUUID().toString() + "firefox.log";

    @BeforeClass
    public static void createSettings()
    {
        Map<String, String> properties3 = new HashMap<>();

        properties3.put("browserprofile.FF_with_args.name", "FF with args");
        properties3.put("browserprofile.FF_with_args.headless", "true");
        properties3.put("browserprofile.FF_with_args.browserResolution", "1024x768");
        properties3.put("browserprofile.FF_with_args.browser", "firefox");
        properties3.put("browserprofile.FF_with_args.driverArgs",
                        "--log ; info ;--allow-hosts; xceptance.com; google.com; --port=8525; --log-no-truncate ; --websocket-port=8785; --allow-origins; http://xceptance.com:8785 ; --log-path="
                                                                  + logFileNameFirefox);

        properties3.put("browserprofile.Chrome_with_args.name", "Chrome with args");
        properties3.put("browserprofile.Chrome_with_args.headless", "true");
        properties3.put("browserprofile.Chrome_with_args.browserResolution", "1024x768");
        properties3.put("browserprofile.Chrome_with_args.browser", "chrome");
        properties3.put("browserprofile.Chrome_with_args.driverArgs",
                        "--port=7100 ; --allowed-origins=localhost, xceptance.com; --log-level=INFO ; --log-path=" + logFileNameChrome);
        File tempConfigFile3 = new File("./config/dev-browser.properties");
        writeMapToPropertiesFile(properties3, tempConfigFile3);
        tempFiles.add(tempConfigFile3);
    }

    @Test
    public void test()
    {
        Result result = JUnitCore.runClasses(DriverArgumentsConfigTest.class);
        checkPass(result, 2, 0);
    }
}
