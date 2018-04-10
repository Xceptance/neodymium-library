package com.xceptance.neodymium.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.data.inheritance.child.PackageTestDataInheritance;
import com.xceptance.neodymium.testclasses.data.inheritance.child.grandchild.GrandChildPackageTestDataInheritance;
import com.xceptance.neodymium.testclasses.data.inheritance.child.grandchild.set.DataSetOverridesPackageData;
import com.xceptance.neodymium.testclasses.data.pkg.csv.CanReadPackageDataCSV;
import com.xceptance.neodymium.testclasses.data.pkg.json.CanReadPackageDataJson;
import com.xceptance.neodymium.testclasses.data.pkg.properties.CanReadPackageDataProperties;
import com.xceptance.neodymium.testclasses.data.pkg.xml.CanReadPackageDataXML;
import com.xceptance.neodymium.testclasses.data.set.csv.CanReadDataSetCSV;
import com.xceptance.neodymium.testclasses.data.set.json.CanReadDataSetJson;
import com.xceptance.neodymium.testclasses.data.set.properties.CanReadDataSetProperties;
import com.xceptance.neodymium.testclasses.data.set.xml.CanReadDataSetXML;
import com.xceptance.neodymium.util.Context;

public class NeodymiumDataRunnerTest extends NeodymiumTest
{

    @Before
    public void beforeTest()
    {
        // we need to clear the context before test starts because all of the tests are running in the same thread and
        // the context holds data based on the current thread. since data sets and package data is also stored in the
        // context this data would never be updated
        Context.clearThreadContext();
    }

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

    @Test
    public void testCanReadDataSetCSV()
    {
        Result result = JUnitCore.runClasses(CanReadDataSetCSV.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadDataSetJson()
    {
        Result result = JUnitCore.runClasses(CanReadDataSetJson.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadDataSetProperties()
    {
        Result result = JUnitCore.runClasses(CanReadDataSetProperties.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadDataSetXML()
    {
        Result result = JUnitCore.runClasses(CanReadDataSetXML.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testPackageTestDataInheritance()
    {
        Result result = JUnitCore.runClasses(PackageTestDataInheritance.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testGrandChildPackageTestDataInheritance()
    {
        Result result = JUnitCore.runClasses(GrandChildPackageTestDataInheritance.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testDataSetOverridesPackageData()
    {
        Result result = JUnitCore.runClasses(DataSetOverridesPackageData.class);
        checkPass(result, 1, 0, 0);
    }
}
