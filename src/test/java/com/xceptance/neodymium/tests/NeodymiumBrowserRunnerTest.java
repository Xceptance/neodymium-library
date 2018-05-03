package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.testclasses.browser.EmptyBrowser;
import com.xceptance.neodymium.testclasses.browser.NoBrowserTag;

public class NeodymiumBrowserRunnerTest extends NeodymiumTest
{
    // holds files that will be deleted in @After method
    static List<File> tempFiles = new LinkedList<>();

    private static MultibrowserConfiguration browserConfig;

    @BeforeClass
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties = new HashMap<>();

        properties.put("browserprofile.chrome.name", "Google Chrome");
        properties.put("browserprofile.chrome.browser", "chrome");
        properties.put("browserprofile.chrome.testEnvironment", "local");

        properties.put("browserprofile.firefox.name", "Mozilla Firefox");
        properties.put("browserprofile.firefox.browser", "firefox");

        File tempConfigFile = File.createTempFile("browser", "", new File("./config/"));
        tempFiles.add(tempConfigFile);
        writeMapToPropertiesFile(properties, tempConfigFile);

        browserConfig = MultibrowserConfiguration.getInstance(tempConfigFile.getPath());

    }

    @Test
    public void testEmptyBrowserTag()
    {
        // an empty @Browser({}) annotation shouldn't raise an error
        Result result = JUnitCore.runClasses(NoBrowserTag.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testEmptyBrowser()
    {
        // an empty browser tag (@Browser({""})) should raise an error
        Result result = JUnitCore.runClasses(EmptyBrowser.class);
        checkFail(result, 1, 0, 1, "java.lang.IllegalArgumentException: Can not find browser configuration with tag: ");
    }

    @Test
    public void testMultibrowserConfiguration() throws Throwable
    {
        // define one chrome browser then validate the parsed multi-browser configuration
        // TODO: we need more of this tests...
        Map<String, BrowserConfiguration> browserProfiles = browserConfig.getBrowserProfiles();
        // Assert.assertEquals(1, browserProfiles.size());

        checkChrome(browserProfiles.get("chrome"));
        checkFirefox(browserProfiles.get("firefox"));
    }

    public void checkChrome(BrowserConfiguration config)
    {
        Assert.assertNotNull(config);
        Assert.assertEquals("chrome", config.getConfigTag());
        Assert.assertEquals("Google Chrome", config.getName());
        Assert.assertEquals("local", config.getTestEnvironment());
        DesiredCapabilities testCapabilities = config.getCapabilities();
        Assert.assertEquals("chrome", testCapabilities.getBrowserName());
    }

    public void checkFirefox(BrowserConfiguration config)
    {
        Assert.assertNotNull(config);
        Assert.assertEquals("firefox", config.getConfigTag());
        Assert.assertEquals("Mozilla Firefox", config.getName());
        Assert.assertEquals(null, config.getTestEnvironment());
        DesiredCapabilities testCapabilities = config.getCapabilities();
        Assert.assertEquals("firefox", testCapabilities.getBrowserName());
    }

    @AfterClass
    public static void cleanUp()
    {
        for (File f : tempFiles)
        {
            if (f.exists())
            {
                try
                {
                    Files.delete(f.toPath());
                }
                catch (Exception e)
                {
                    System.out.println(MessageFormat.format("couldn''t delete temporary file: ''{0}'' caused by {1}", f.getAbsolutePath(),
                                                            e));
                }
            }
        }
    }
}
