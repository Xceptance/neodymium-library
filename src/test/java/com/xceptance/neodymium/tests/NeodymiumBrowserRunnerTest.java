package com.xceptance.neodymium.tests;

import java.io.File;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.xceptance.neodymium.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.testclasses.browser.EmptyBrowser;
import com.xceptance.neodymium.testclasses.browser.NoBrowserTag;

public class NeodymiumBrowserRunnerTest extends NeodymiumTest
{
    // holds files that will be deleted in @After method
    static List<File> tempFiles = new LinkedList<>();

    @Test
    public void testEmptyBrowserTag()
    {
        // an empty @Browser({}) annotation shouldn't raise an error and shouldn't invoke a method
        Result result = JUnitCore.runClasses(NoBrowserTag.class);
        checkPass(result, 0, 0, 0);
    }

    @Test
    public void testEmptyBrowser()
    {
        // an empty browser tag (@Browser({""})) should raise an error
        Result result = JUnitCore.runClasses(EmptyBrowser.class);
        checkFail(result, 1, 0, 1, "Can not find browser configuration with tag: ");
    }

    @Test
    public void testMultibrowserConfiguration() throws Throwable
    {
        // define one chrome browser then validate the parsed multi-browser configuration
        // TODO: we need more of this tests...
        Map<String, String> properties = new HashMap<>();
        properties.put("browserprofile.test.name", "a test browser");
        properties.put("browserprofile.test.browser", "chrome");
        properties.put("browserprofile.test.browserResolution", "1024x768");

        File tempConfigFile = File.createTempFile("browser", "", new File("./config/"));
        tempFiles.add(tempConfigFile);
        writeMapToPropertiesFile(properties, tempConfigFile);

        MultibrowserConfiguration multibrowserConfiguration = MultibrowserConfiguration.getInstance(tempConfigFile.getPath());
        Map<String, BrowserConfiguration> browserProfiles = multibrowserConfiguration.getBrowserProfiles();
        Assert.assertEquals(1, browserProfiles.size());
        BrowserConfiguration testProfile = browserProfiles.get("test");
        Assert.assertNotNull(testProfile);
        Assert.assertEquals("test", testProfile.getConfigTag());
        Assert.assertEquals(1024, testProfile.getBrowserWidth());
        Assert.assertEquals(768, testProfile.getBrowserHeight());
        Assert.assertEquals("a test browser", testProfile.getName());
        Assert.assertEquals(null, testProfile.getTestEnvironment());
        DesiredCapabilities testCapabilities = testProfile.getCapabilities();
        Assert.assertEquals("chrome", testCapabilities.getBrowserName());
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
