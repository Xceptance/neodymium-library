package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.testclasses.multiplication.browser.OneBrowserOneMethod;
import com.xceptance.neodymium.testclasses.multiplication.browser.OneBrowserTwoMethods;
import com.xceptance.neodymium.testclasses.multiplication.browser.TwoBrowserOneMethod;
import com.xceptance.neodymium.testclasses.multiplication.browser.TwoBrowserTwoMethods;

@Ignore
// TODO: Currently ignored because we lack of providing a custom browser.properties for these tests.
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

        MultibrowserConfiguration multibrowserConfiguration = MultibrowserConfiguration.getInstance(tempConfigFile.getPath());
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
                "first :: (first browser)"
            };
        checkDescription(OneBrowserOneMethod.class, expected);
    }

    @Test
    public void testOneBrowserTwoMethods() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: (first browser)", //
                "second :: (first browser)"
            };
        checkDescription(OneBrowserTwoMethods.class, expected);
    }

    @Test
    public void testTwoBrowserOneMethod() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: (first browser)", //
                "first :: (second browser)"
            };
        checkDescription(TwoBrowserOneMethod.class, expected);
    }

    @Test
    public void testTwoBrowserTwoMethods() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: (first browser)", //
                "first :: (second browser)", //
                "second :: (first browser)", //
                "second :: (second browser)"
            };
        checkDescription(TwoBrowserTwoMethods.class, expected);
    }

}
