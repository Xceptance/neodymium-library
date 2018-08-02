package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.testclasses.proxy.RunWithProxy;
import com.xceptance.neodymium.testclasses.proxy.SetProxForWebDriver;

public class ProxyConfiguratioTest extends NeodymiumTest
{
    private static final String HOST = "bemylittleproxydarling.se";

    private static final String PORT = "0815";

    private static final String BYPASS = "www.xceptance.com";

    private static final String SOCKET_USERNAME = "username";

    private static final String SOCKET_PASSWORD = "password";

    private static final String SOCKET_VERSION = "4";

    private static File tempConfigFile;

    @BeforeClass
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.proxy", "true");
        properties.put("neodymium.proxy.host", HOST);
        properties.put("neodymium.proxy.port", PORT);
        properties.put("neodymium.proxy.bypassForHosts", BYPASS);
        properties.put("neodymium.proxy.socket.userName", SOCKET_USERNAME);
        properties.put("neodymium.proxy.socket.password", SOCKET_PASSWORD);
        properties.put("neodymium.proxy.socket.version", SOCKET_VERSION);

        tempConfigFile = new File("./config/proxy.properties");
        writeMapToPropertiesFile(properties, tempConfigFile);

        MultibrowserConfiguration.getInstance().getProxyConfiguration();
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
        Result result = JUnitCore.runClasses(SetProxForWebDriver.class);
        checkPass(result, 4, 0, 0);
    }

    @AfterClass
    public static void afterClass()
    {
        if (tempConfigFile.exists())
        {
            try
            {
                Files.delete(tempConfigFile.toPath());
            }
            catch (Exception e)
            {
                System.out.println(MessageFormat.format("couldn''t delete temporary file: ''{0}'' caused by {1}",
                                                        tempConfigFile.getAbsolutePath(), e));
            }
        }
    }
}
