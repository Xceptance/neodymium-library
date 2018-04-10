package com.xceptance.neodymium.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.data.pkg.csv.CanReadPackageDataCSV;
import com.xceptance.neodymium.testclasses.data.pkg.json.CanReadPackageDataJson;
import com.xceptance.neodymium.testclasses.data.pkg.properties.CanReadPackageDataProperties;
import com.xceptance.neodymium.testclasses.data.pkg.xml.CanReadPackageDataXML;

public class NeodymiumDataRunnerTest extends NeodymiumTest
{
    @Test
    public void testCanReadPackageDataCSV()
    {
        Result result = JUnitCore.runClasses(CanReadPackageDataCSV.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadPackageDataJson()
    {
        Result result = JUnitCore.runClasses(CanReadPackageDataJson.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadPackageDataProperties()
    {
        Result result = JUnitCore.runClasses(CanReadPackageDataProperties.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadPackageDataXML()
    {
        Result result = JUnitCore.runClasses(CanReadPackageDataXML.class);
        checkPass(result, 1, 0, 0);
    }
}
