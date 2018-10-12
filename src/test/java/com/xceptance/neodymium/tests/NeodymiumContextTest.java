package com.xceptance.neodymium.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.testclasses.context.ContextGetsCleared;
import com.xceptance.neodymium.testclasses.context.DefaultSelenideTimeoutCheck;
import com.xceptance.neodymium.testclasses.context.SelenideConfigurationShortcuts;
import com.xceptance.neodymium.testclasses.context.WindowSizeTests;
import com.xceptance.neodymium.testclasses.context.cucumbercontextclear.CucumberContextGetsCleared;

public class NeodymiumContextTest extends NeodymiumTest
{
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

    @Test
    public void testSelenideConfigurationShortcuts() throws Exception
    {
        Result result = JUnitCore.runClasses(SelenideConfigurationShortcuts.class);
        checkPass(result, 5, 0, 0);
    }

    @Test
    public void testContextWindowSize() throws Exception
    {
        Map<String, String> properties = new HashMap<>();

        properties.put("browserprofile.chrome500.name", "Google Chrome, Headless, 500x768");
        properties.put("browserprofile.chrome500.browser", "chrome");
        properties.put("browserprofile.chrome500.arguments", "headless");
        properties.put("browserprofile.chrome500.browserResolution", "500x768");

        properties.put("browserprofile.chrome544.name", "Google Chrome, Headless, 544x768");
        properties.put("browserprofile.chrome544.browser", "chrome");
        properties.put("browserprofile.chrome544.arguments", "headless");
        properties.put("browserprofile.chrome544.browserResolution", "544x768");

        properties.put("browserprofile.chrome769.name", "Google Chrome, Headless, 769x768");
        properties.put("browserprofile.chrome769.browser", "chrome");
        properties.put("browserprofile.chrome769.arguments", "headless");
        properties.put("browserprofile.chrome769.browserResolution", "769x768");

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
        // isMobile()
        // isTablet()
        // isSmallDesktop()
        // isDesktop()
        // isLargeDesktop()
        // isExtraLargeDesktop()

        Result result = JUnitCore.runClasses(WindowSizeTests.class);
        checkPass(result, 5, 0, 0);
    }
}
