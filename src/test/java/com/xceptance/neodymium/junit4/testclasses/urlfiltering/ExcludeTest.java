package com.xceptance.neodymium.junit4.testclasses.urlfiltering;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.junit4.NeodymiumRunner;
import com.xceptance.neodymium.junit4.tests.NeodymiumTest;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_1024x768")
public class ExcludeTest extends NeodymiumTest
{
    @BeforeClass
    public static void beforeClass() throws IOException
    {
        File tempConfigFile = new File("./config/neodymium.temp");
        File configFile = new File("./config/neodymium.properties");
        Map<String, String> properties1 = new HashMap<>();
        properties1.put("neodymium.url.excludeList", "https://www.google.com/ https://github.com");
        FileUtils.copyFile(configFile, tempConfigFile);
        writeMapToPropertiesFile(properties1, configFile);
    }

    @Test
    public void testPostersIsForbidden()
    {
        Selenide.open("https://www.xceptance.com/en/");
        Selenide.sleep(100);
        Selenide.open("https://www.google.com/");
    }

    @Test
    public void testWikipediaIsForbidden()
    {
        Selenide.open("https://www.xceptance.com/en/");
        Selenide.sleep(100);
        Selenide.open("https://github.com");
    }

    @AfterClass
    public static void afterClass() throws IOException
    {
        File tempConfigFile = new File("./config/neodymium.temp");
        File configFile = new File("./config/neodymium.properties");
        FileUtils.copyFile(tempConfigFile, configFile);
    }
}
