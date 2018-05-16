package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.testclasses.multiplication.browser.OneBrowserOneMethod;
import com.xceptance.neodymium.testclasses.multiplication.browser.OneBrowserTwoMethods;
import com.xceptance.neodymium.testclasses.multiplication.browser.TwoBrowserOneMethod;
import com.xceptance.neodymium.testclasses.multiplication.browser.TwoBrowserTwoMethods;
import com.xceptance.neodymium.util.Context;

public class BrowserMultiplicationTest extends NeodymiumTest
{
    private static File tempConfigFile;

    @BeforeClass
    public static void beforeClass() throws IOException
    {
        Map<String, String> properties = new HashMap<>();
        properties.put("browserprofile.first_browser.name", "first browser");
        properties.put("browserprofile.second_browser.name", "second browser");

        tempConfigFile = File.createTempFile("browser", "", new File("./config/"));
        writeMapToPropertiesFile(properties, tempConfigFile);

        // this line is important as we initialize the config from the temporary file we created above
        MultibrowserConfiguration.clearAllInstances();
        MultibrowserConfiguration.getInstance(tempConfigFile.getPath());
    }

    @Before
    public void setJUnitViewModeFlat()
    {
        Context.get().configuration.setProperty("junit.viewmode", "flat");
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

    @Test
    public void testOneBrowserOneMethod() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: Browser first_browser"
            };
        checkDescription(OneBrowserOneMethod.class, expected);
    }

    @Test
    public void testOneBrowserTwoMethods() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: Browser first_browser", //
                "second :: Browser first_browser"
            };
        checkDescription(OneBrowserTwoMethods.class, expected);
    }

    @Test
    public void testTwoBrowserOneMethod() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: Browser first_browser", //
                "first :: Browser second_browser"
            };
        checkDescription(TwoBrowserOneMethod.class, expected);
    }

    @Test
    public void testTwoBrowserTwoMethods() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: Browser first_browser", //
                "first :: Browser second_browser", //
                "second :: Browser first_browser", //
                "second :: Browser second_browser"
            };
        checkDescription(TwoBrowserTwoMethods.class, expected);
    }

}
