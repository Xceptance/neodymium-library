package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.proxy.RunWithProxy;
import com.xceptance.neodymium.testclasses.proxy.SetProxyForWebDriver;
import com.xceptance.neodymium.util.Neodymium;

public class ProxyConfigurationTest extends NeodymiumTest
{
    private static final String HOST = "bemylittleproxydarling.se";

    private static final String PORT = "0815";

    private static final String BYPASS = "www.xceptance.com";

    private static final String SOCKET_USERNAME = "username";

    private static final String SOCKET_PASSWORD = "password";

    private static final String SOCKET_VERSION = "4";

    @BeforeClass
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.proxy.host", HOST);
        properties.put("neodymium.proxy.port", PORT);
        properties.put("neodymium.proxy.bypassForHosts", BYPASS);
        properties.put("neodymium.proxy.socket.userName", SOCKET_USERNAME);
        properties.put("neodymium.proxy.socket.password", SOCKET_PASSWORD);
        properties.put("neodymium.proxy.socket.version", SOCKET_VERSION);

        File tempConfigFile = new File("./config/dev-neodymium.properties");
        writeMapToPropertiesFile(properties, tempConfigFile);
        tempFiles.add(tempConfigFile);
    }

    @Before
    public void before()
    {
        Neodymium.configuration().setProperty("neodymium.proxy", "true");
    }

    @Test
    public void testExecutionWithProxy()
    {
        // test proxy configuration as far as possible without setting up a proxy
        Result result = JUnitCore.runClasses(RunWithProxy.class);
        checkPass(result, 2, 0, 0);
    }

    @Test
    public void testSettingOfProxyForWebdriver()
    {
        // test adding proxy configuration to different WebDriver options and validate them
        Result result = JUnitCore.runClasses(SetProxyForWebDriver.class);
        checkPass(result, 4, 0, 0);
    }
}
