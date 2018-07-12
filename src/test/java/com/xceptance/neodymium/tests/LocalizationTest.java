package com.xceptance.neodymium.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.util.Context;

@RunWith(NeodymiumRunner.class)
public class LocalizationTest extends NeodymiumTest
{
    private static File tempConfigFile;

    @BeforeClass
    public static void createLocalizationFile() throws IOException
    {
        tempConfigFile = File.createTempFile("localization", ".yaml", new File("./config/"));
        tempFiles.add(tempConfigFile);

        // set system property to change default localization file to the new created
        System.setProperty("neodymium.localization.file", tempConfigFile.getPath());

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempConfigFile)));
        bw.write("default:");
        bw.newLine();
        bw.write(" key1: default");
        bw.newLine();
        bw.write(" key2:");
        bw.newLine();
        bw.write("en_US:");
        bw.newLine();
        bw.write(" key1: en_US");
        bw.newLine();
        bw.write("en:");
        bw.newLine();
        bw.write(" key1: en");
        bw.newLine();
        bw.write("fr_FR:");
        bw.newLine();
        bw.write(" key1: fr_FR");
        bw.newLine();
        bw.close();
    }

    @Test
    public void testDefault()
    {
        Context.get().configuration.setProperty("neodymium.locale", "default");
        String key = "key1";
        Assert.assertEquals("default", Context.localizedText(key));
    }

    @Test
    public void testUnsetLocale()
    {
        // set locale null and check locale fallback to "default"
        Context.get().configuration.setProperty("neodymium.locale", null);
        String key = "key1";
        Assert.assertEquals("default", Context.localizedText(key));
    }

    @Test
    public void testEnUS()
    {
        // en_US is the default locale which is defined in interface Configuration
        String key = "key1";
        Assert.assertEquals("en_US", Context.localizedText(key));
    }

    @Test
    public void testLanguageFallback()
    {
        // we do not have a locale en_CA and expect to fallback to "en"
        Context.get().configuration.setProperty("neodymium.locale", "en_CA");
        String key = "key1";
        Assert.assertEquals("en", Context.localizedText(key));
    }

    @Test
    public void testFallbackToDefault()
    {
        // we do not have a locale en_CA and expect to fallback to "en"
        Context.get().configuration.setProperty("neodymium.locale", "fr_CA");
        String key = "key1";
        Assert.assertEquals("default", Context.localizedText(key));
    }

    @Test
    public void testEmptyKeyIsConvertedToEmptyString()
    {
        Context.get().configuration.setProperty("neodymium.locale", "default");
        Assert.assertEquals("", Context.localizedText("key2"));
    }

    @Test(expected = AssertionError.class)
    public void testAssertionErrorWhenKeyIsUnknown()
    {
        Context.get().configuration.setProperty("neodymium.locale", "default");
        Context.localizedText("key3");
    }
}
