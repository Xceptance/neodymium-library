package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.browser.FixedRandomnessOfRandomBrowser;
import com.xceptance.neodymium.util.Neodymium;
import com.xceptance.neodymium.util.NeodymiumRandom;

public class FixedRandomnessOfRandomBrowserTest extends NeodymiumTest
{
    @BeforeClass
    public static void setUpNeodymiumConfiguration() throws IOException
    {
        // set up a temp-neodymium.properties
        final String fileLocation = "config/temp-neodymium-FixedRandomnessOfRandomBrowserTest.properties";
        File tempConfigFile = new File("./" + fileLocation);
        tempFiles.add(tempConfigFile);
        Map<String, String> properties = new HashMap<>();

        properties.put("neodymium.context.random.initialValue", "1323");

        writeMapToPropertiesFile(properties, tempConfigFile);
        ConfigFactory.setProperty(Neodymium.TEMPORARY_CONFIG_FILE_PROPERTY_NAME, "file:" + fileLocation);
    }

    @Before
    public void checkPreconditions()
    {
        Assert.assertEquals(1323, NeodymiumRandom.getSeed());
    }

    @Test
    public void testFixedRandomnessOfRandomBrowser()
    {
        // test fixed random browser support
        Result result = JUnitCore.runClasses(FixedRandomnessOfRandomBrowser.class);
        checkPass(result, 3, 0);
    }
}
