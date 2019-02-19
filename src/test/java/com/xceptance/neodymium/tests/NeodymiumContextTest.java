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

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.testclasses.context.ContextGetsCleared;
import com.xceptance.neodymium.testclasses.context.DefaultSelenideConfiguration;
import com.xceptance.neodymium.testclasses.context.OverrideNeodymiumConfiguration;
import com.xceptance.neodymium.testclasses.context.SelenideConfigurationShortcuts;
import com.xceptance.neodymium.testclasses.context.WindowSizeTests;
import com.xceptance.neodymium.testclasses.context.cucumbercontextclear.CucumberContextGetsCleared;
import com.xceptance.neodymium.util.Neodymium;

public class NeodymiumContextTest extends NeodymiumTest
{
    @BeforeClass
    public static void setUpNeodymiumConfiguration() throws IOException
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.webDriver.opera.pathToDriverServer", "/some/opera/path/just/for/test/purpose");
        File tempConfigFile1 = File.createTempFile("dev-neodymium", "", new File("./config/"));
        tempFiles.add(tempConfigFile1);
        writeMapToPropertiesFile(properties, tempConfigFile1);

        properties.put("neodymium.webDriver.phantomjs.pathToDriverServer", "/some/phantomjs/path/just/for/test/purpose");

        final String fileLocation = "config/temp-neodymium.properties";
        File tempConfigFile2 = new File("./" + fileLocation);
        tempFiles.add(tempConfigFile2);
        writeMapToPropertiesFile(properties, tempConfigFile2);

        ConfigFactory.setProperty(Neodymium.TEMPORARY_CONFIG_FILE_PROPTERY_NAME, "file:" + fileLocation);
    }

    @Test
    public void testContextGetCleared() throws Exception
    {
        // test that NeodymiumRunner clears the context before each run
        Result result = JUnitCore.runClasses(ContextGetsCleared.class);
        checkPass(result, 2, 0, 0);
    }

    @Test
    public void testCucumberContextGetsCleared() throws Exception
    {
        // test that NeodymiumCucumberRunListener clears the context before each run
        Result result = JUnitCore.runClasses(CucumberContextGetsCleared.class);
        checkPass(result, 2, 0, 0);
    }

    @Test
    public void testDefaultSelenideConfigurationCheck() throws Exception
    {
        Result result = JUnitCore.runClasses(DefaultSelenideConfiguration.class);
        checkPass(result, 2, 0, 0);
    }

    @Test
    public void testSelenideConfigurationShortcuts() throws Exception
    {
        Result result = JUnitCore.runClasses(SelenideConfigurationShortcuts.class);
        checkPass(result, 4, 0, 0);
    }

    @Test
    public void testOverridingNeodymiumConfiguration() throws Exception
    {
        Result result = JUnitCore.runClasses(OverrideNeodymiumConfiguration.class);
        checkPass(result, 2, 0, 0);
    }

    @Test
    public void testContextWindowSize() throws Exception
    {
        Map<String, String> properties = new HashMap<>();

        properties.put("browserprofile.chrome500.name", "Google Chrome, Headless, 500x768");
        properties.put("browserprofile.chrome500.browser", "chrome");
        properties.put("browserprofile.chrome500.arguments", "headless");
        properties.put("browserprofile.chrome500.browserResolution", "500x768");

        properties.put("browserprofile.chrome576.name", "Google Chrome, Headless, 576x768");
        properties.put("browserprofile.chrome576.browser", "chrome");
        properties.put("browserprofile.chrome576.arguments", "headless");
        properties.put("browserprofile.chrome576.browserResolution", "576x768");

        properties.put("browserprofile.chrome768.name", "Google Chrome, Headless, 768x768");
        properties.put("browserprofile.chrome768.browser", "chrome");
        properties.put("browserprofile.chrome768.arguments", "headless");
        properties.put("browserprofile.chrome768.browserResolution", "768x768");

        properties.put("browserprofile.chrome992.name", "Google Chrome, Headless, 992x768");
        properties.put("browserprofile.chrome992.browser", "chrome");
        properties.put("browserprofile.chrome992.arguments", "headless");
        properties.put("browserprofile.chrome992.browserResolution", "992x768");

        properties.put("browserprofile.chrome1200.name", "Google Chrome, Headless, 1200x768");
        properties.put("browserprofile.chrome1200.browser", "chrome");
        properties.put("browserprofile.chrome1200.arguments", "headless");
        properties.put("browserprofile.chrome1200.browserResolution", "1200x768");

        File tempConfigFile = File.createTempFile("browser", "", new File("./config/"));
        tempFiles.add(tempConfigFile);
        writeMapToPropertiesFile(properties, tempConfigFile);

        // this line is important as we initialize the config from the temporary file we created above
        MultibrowserConfiguration.clearAllInstances();
        MultibrowserConfiguration.getInstance(tempConfigFile.getPath());

        // checks Neodymium functions for different browser sizes
        Result result = JUnitCore.runClasses(WindowSizeTests.class);
        checkPass(result, 5, 0, 0);
    }
}
