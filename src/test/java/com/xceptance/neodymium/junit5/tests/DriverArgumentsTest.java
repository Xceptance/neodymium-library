package com.xceptance.neodymium.junit5.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.xceptance.neodymium.junit5.testclasses.webDriver.DriverArgumentsConfigTest;
import com.xceptance.neodymium.junit5.tests.utils.NeodymiumTestExecutionSummary;

public class DriverArgumentsTest extends AbstractNeodymiumTest
{
    public static String logFileNameChrome = "target/" + UUID.randomUUID().toString() + "_chrome.log";

    public static String logFileNameFirefox = "target/" + UUID.randomUUID().toString() + "_firefox.log";

    public static String chromePort = "7100";

    public static String firefoxPort = "8525";

    public static String firefoxWebsocketPort = "8785";

    @BeforeAll
    public static void createSettings()
    {
        Map<String, String> properties3 = new HashMap<>();

        properties3.put("browserprofile.FF_with_args.name", "FF with args");
        properties3.put("browserprofile.FF_with_args.headless", "true");
        properties3.put("browserprofile.FF_with_args.browserResolution", "1024x768");
        properties3.put("browserprofile.FF_with_args.browser", "firefox");
        properties3.put("browserprofile.FF_with_args.driverArgs",
                        "--log ; info ;--allow-hosts; xceptance.com; google.com; --port=" + firefoxPort
                                                                  + "; --log-no-truncate ; --websocket-port=" + firefoxWebsocketPort
                                                                  + "; --allow-origins; http://xceptance.com:8785 ; --log-path="
                                                                  + logFileNameFirefox);

        properties3.put("browserprofile.Chrome_with_args.name", "Chrome with args");
        properties3.put("browserprofile.Chrome_with_args.headless", "true");
        properties3.put("browserprofile.Chrome_with_args.browserResolution", "1024x768");
        properties3.put("browserprofile.Chrome_with_args.browser", "chrome");
        properties3.put("browserprofile.Chrome_with_args.driverArgs",
                        "--port=" + chromePort + "; --allowed-origins=localhost, xceptance.com; --log-level=INFO ; --log-path=" + logFileNameChrome);
        File tempConfigFile3 = new File("./config/dev-browser.properties");
        writeMapToPropertiesFile(properties3, tempConfigFile3);
        tempFiles.add(tempConfigFile3);
    }

    @Test
    public void test()
    {
        NeodymiumTestExecutionSummary result = run(DriverArgumentsConfigTest.class);
        checkPass(result, 2, 0);
    }
}
