package com.xceptance.neodymium.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.datautils.DataUtilsTests;
import com.xceptance.neodymium.testclasses.datautils.DataUtilsTestsXml;

public class DataUtilsTest extends NeodymiumTest
{
    @Test
    public void testDataUtils() throws Exception
    {
        // test the data utils
        Result result = JUnitCore.runClasses(DataUtilsTests.class);
        checkPass(result, 7, 0, 0);
    }

    @Test
    public void testDataUtilsXml() throws Exception
    {
        // test the data utils
        Result result = JUnitCore.runClasses(DataUtilsTestsXml.class);
        checkPass(result, 7, 0, 0);
    }
}
