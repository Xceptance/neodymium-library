package com.xceptance.neodymium.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.data.pkg.csv.CanReadPackageCSV;
import com.xceptance.neodymium.testclasses.data.pkg.json.CanReadPackageJson;

public class NeodymiumDataRunnerTest extends NeodymiumTest
{
    @Test
    public void testCanReadPackageCSV()
    {
        Result result = JUnitCore.runClasses(CanReadPackageCSV.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadPackageJson()
    {
        Result result = JUnitCore.runClasses(CanReadPackageJson.class);
        checkPass(result, 1, 0, 0);
    }
}
