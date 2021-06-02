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

import com.xceptance.neodymium.testclasses.filtering.TestCaseFiltering;
import com.xceptance.neodymium.util.Neodymium;

public class TestCaseFilteringTest extends NeodymiumTest
{
    @BeforeClass
    public static void beforeClass() throws IOException
    {
        final String fileLocation = "config/test-filtering-neodymium.properties";

        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.testNameFilter", ".*#(shouldBeExecuted|shouldBeExecutedForDataSetWithExecutableId) :: executable.*");

        File tempConfigFile = new File("./" + fileLocation);
        writeMapToPropertiesFile(properties, tempConfigFile);
        tempFiles.add(tempConfigFile);

        ConfigFactory.setProperty(Neodymium.TEMPORARY_CONFIG_FILE_PROPERTY_NAME, "file:" + fileLocation);
    }

    @Test
    public void testTestCaseFiltering()
    {
        // the test from RandomBrowserChild should be run 2 times, as the corresponding annotations should be inherited
        // from the RandomBrowserParent class
        Result result = JUnitCore.runClasses(TestCaseFiltering.class);
        checkPass(result, 2, 0);
    }
}
