package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.testclasses.context.ContextGetsCleared;
import com.xceptance.neodymium.testclasses.context.DefaultSelenideTimeoutCheck;
import com.xceptance.neodymium.testclasses.context.cucumbercontextclear.CucumberContextGetsCleared;

public class ContextTest extends NeodymiumTest
{
    private static File tempConfigFile;

    @BeforeClass
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties = new HashMap<>();

        properties.put("browserprofile.headless_chrome.name", "Headless Google Chrome");
        properties.put("browserprofile.headless_chrome.browser", "chrome");
        properties.put("browserprofile.headless_chrome.headless", "true");

        tempConfigFile = File.createTempFile("browser", "", new File("./config/"));
        tempFiles.add(tempConfigFile);
        writeMapToPropertiesFile(properties, tempConfigFile);

        MultibrowserConfiguration.clearAllInstances();
        MultibrowserConfiguration.getInstance(tempConfigFile.getPath());
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
        checkPass(result, 4, 0, 0);
    }

    @Test
    public void testDefaultSelenideTimeoutCheck() throws Exception
    {
        Result result = JUnitCore.runClasses(DefaultSelenideTimeoutCheck.class);
        checkPass(result, 2, 0, 0);
    }

}
