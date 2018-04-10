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
        // test package test data csv is read
        Result result = JUnitCore.runClasses(CanReadPackageDataCSV.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadPackageDataJson()
    {
        // test package test data json is read
        Result result = JUnitCore.runClasses(CanReadPackageDataJson.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadPackageDataProperties()
    {
        // test package test data properties is read
        Result result = JUnitCore.runClasses(CanReadPackageDataProperties.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadPackageDataXML()
    {
        // test package test data xml is read
        Result result = JUnitCore.runClasses(CanReadPackageDataXML.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadDataSetCSV()
    {
        // test data set csv is read
        Result result = JUnitCore.runClasses(CanReadDataSetCSV.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadDataSetJson()
    {
        // test data set json is read
        Result result = JUnitCore.runClasses(CanReadDataSetJson.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadDataSetProperties()
    {
        // test data set properties is read
        Result result = JUnitCore.runClasses(CanReadDataSetProperties.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testCanReadDataSetXML()
    {
        // test data set xml is read
        Result result = JUnitCore.runClasses(CanReadDataSetXML.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testPackageTestDataInheritance()
    {
        // test inheritacne of package test data
        Result result = JUnitCore.runClasses(PackageTestDataInheritance.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testGrandChildPackageTestDataInheritance()
    {
        // test multiple inheritance of package test data
        Result result = JUnitCore.runClasses(GrandChildPackageTestDataInheritance.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testDataSetOverridesPackageData()
    {
        // test that data set overrides package test data
        Result result = JUnitCore.runClasses(DataSetOverridesPackageData.class);
        checkPass(result, 1, 0, 0);
    }
}
