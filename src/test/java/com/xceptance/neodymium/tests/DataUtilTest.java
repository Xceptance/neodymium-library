package com.xceptance.neodymium.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.datautils.DataUtilsTests;

public class DataUtilTest extends NeodymiumTest
{
    @Test
    public void testDataUtils() throws Exception
    {
        // test the data utils
        Result result = JUnitCore.runClasses(DataUtilsTests.class);
        checkPass(result, 6, 0, 0);
    }
}
