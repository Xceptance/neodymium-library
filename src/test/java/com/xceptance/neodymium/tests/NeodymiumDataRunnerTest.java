package com.xceptance.neodymium.tests;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.data.inheritance.child.PackageTestDataInheritance;
import com.xceptance.neodymium.testclasses.data.inheritance.child.grandchild.GrandChildPackageTestDataInheritance;
import com.xceptance.neodymium.testclasses.data.inheritance.child.grandchild.set.DataSetOverridesPackageData;
import com.xceptance.neodymium.testclasses.data.override.classonly.ClassDefaultValueEmptyDataSets;
import com.xceptance.neodymium.testclasses.data.override.classonly.ClassDefaultValueNoDataSets;
import com.xceptance.neodymium.testclasses.data.override.classonly.ClassDefaultValueOneDataSet;
import com.xceptance.neodymium.testclasses.data.override.classonly.ClassDefaultValueTwoDataSets;
import com.xceptance.neodymium.testclasses.data.override.classonly.ClassExplicitDefaultValueTwoDataSets;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodDefaultEmptyDataSets;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodDefaultNoDataSets;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodDefaultOneDataSet;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodDefaultTwoDataSet;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodExplicitDefaultTwoDataSets;
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

    /**
     * This test is ignored because property files are a fairly bad data set storage. We first need to decide what a
     * propper <key> layout would be. Maybe property files will be not supported for data sets but yaml instead.
     */
    @Test
    @Ignore
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

    ///////////////////////
    // Testdata override //
    ///////////////////////

    // Class level override

    @Test
    public void testClassDefaultValueNoDataSets() throws Throwable
    {
        // No data set file and @Testdata() on class
        String[] expected = new String[]
            {
                "test1"
            };
        checkDescription(ClassDefaultValueNoDataSets.class, expected);
    }

    @Test
    public void testClassDefaultValueEmptyDataSets() throws Throwable
    {
        // Empty data sets (only one key but no value) and @Testdata() on class
        String[] expected = new String[]
            {
                "test1"
            };
        checkDescription(ClassDefaultValueEmptyDataSets.class, expected);
    }

    @Test
    public void testClassDefaultValueOneDataSet() throws Throwable
    {
        // One data set and @Testdata() on class
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 1"
            };
        checkDescription(ClassDefaultValueOneDataSet.class, expected);
    }

    @Test
    public void testClassDefaultValueTwoDataSets() throws Throwable
    {
        // Two data sets and @Testdata() on class
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 2", "test1 :: Data set 2 / 2"
            };
        checkDescription(ClassDefaultValueTwoDataSets.class, expected);
    }

    @Test
    public void testClassExplicitDefaultValueTwoDataSets() throws Throwable
    {
        // Two data sets and explicit @Testdata(-1) on class
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 2", "test1 :: Data set 2 / 2"
            };
        checkDescription(ClassExplicitDefaultValueTwoDataSets.class, expected);
    }

    // Method level override

    @Test
    public void testMethodDefaultEmptyDataSets() throws Throwable
    {
        // Empty data sets (only one key but no value) and @Testdata() on method
        String[] expected = new String[]
            {
                "test1"
            };
        checkDescription(MethodDefaultEmptyDataSets.class, expected);
    }

    @Test
    public void testMethodDefaultNoDataSets() throws Throwable
    {
        // No data set file and @Testdata on method
        String[] expected = new String[]
            {
                "test1"
            };
        checkDescription(MethodDefaultNoDataSets.class, expected);
    }

    @Test
    public void testMethodDefaultOneDataSet() throws Throwable
    {
        // One data set and @Testdata on method
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 1"
            };
        checkDescription(MethodDefaultOneDataSet.class, expected);
    }

    @Test
    public void testMethodDefaultTwoDataSet() throws Throwable
    {
        // Two data sets and @Testdata on method
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 2", "test1 :: Data set 2 / 2"
            };
        checkDescription(MethodDefaultTwoDataSet.class, expected);
    }

    @Test
    public void testMethodExplicitDefaultTwoDataSets() throws Throwable
    {
        // Two data sets and explicit @Testdata(-1) on method
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 2", "test1 :: Data set 2 / 2"
            };
        checkDescription(MethodExplicitDefaultTwoDataSets.class, expected);
    }
}
