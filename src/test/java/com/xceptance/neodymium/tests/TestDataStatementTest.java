package com.xceptance.neodymium.tests;

import org.junit.Before;
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
import com.xceptance.neodymium.testclasses.data.override.classonly.ClassMultipleSameDataSet;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodDefaultEmptyDataSets;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodDefaultNoDataSets;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodDefaultOneDataSet;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodDefaultTwoDataSet;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodExplicitDefaultTwoDataSets;
import com.xceptance.neodymium.testclasses.data.override.methodonly.MethodMultipleSameDataSet;
import com.xceptance.neodymium.testclasses.data.override.mixed.ClassWithoutTwoMethodsOneForced;
import com.xceptance.neodymium.testclasses.data.override.mixed.ForceOfNoneDataSets;
import com.xceptance.neodymium.testclasses.data.override.mixed.OneDataSetTwoMethodsOneWithout;
import com.xceptance.neodymium.testclasses.data.override.mixed.OnlyImplicitOneDataSet;
import com.xceptance.neodymium.testclasses.data.override.mixed.TwoDataSetsTwoMethodsOneForced;
import com.xceptance.neodymium.testclasses.data.override.mixed.TwoDataSetsTwoMethodsOneWithout;
import com.xceptance.neodymium.testclasses.data.pkg.csv.CanReadPackageDataCSV;
import com.xceptance.neodymium.testclasses.data.pkg.json.CanReadPackageDataJson;
import com.xceptance.neodymium.testclasses.data.pkg.properties.CanReadPackageDataProperties;
import com.xceptance.neodymium.testclasses.data.pkg.xml.CanReadPackageDataXML;
import com.xceptance.neodymium.testclasses.data.set.csv.CanReadDataSetCSV;
import com.xceptance.neodymium.testclasses.data.set.json.CanReadDataSetJson;
import com.xceptance.neodymium.testclasses.data.set.testid.DuplicateTestId;
import com.xceptance.neodymium.testclasses.data.set.testid.SpecialCharacterTestId;
import com.xceptance.neodymium.testclasses.data.set.xml.CanReadDataSetXML;
import com.xceptance.neodymium.util.Context;

public class TestDataStatementTest extends NeodymiumTest
{
    @Before
    public void setJUnitViewModeFlat()
    {
        Context.get().configuration.setProperty("junit.viewmode", "flat");
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

    @Test
    public void testDuplicateTestId() throws Exception
    {
        // more than one entry with the same "testId"
        Result result = JUnitCore.runClasses(DuplicateTestId.class);
        checkPass(result, 6, 0, 0);
    }

    @Test
    public void testSpecialCharacterTestId() throws Throwable
    {
        // special characters in testId
        // parenthesis will be converted to to brackets
        Result result = JUnitCore.runClasses(SpecialCharacterTestId.class);
        checkPass(result, 7, 0, 0);
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
                "test1 :: Data set 1 / 2", //
                "test1 :: Data set 2 / 2"
            };
        checkDescription(ClassDefaultValueTwoDataSets.class, expected);
    }

    @Test
    public void testClassExplicitDefaultValueTwoDataSets() throws Throwable
    {
        // Two data sets and explicit @Testdata(-1) on class
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 2", //
                "test1 :: Data set 2 / 2"
            };
        checkDescription(ClassExplicitDefaultValueTwoDataSets.class, expected);
    }

    @Test
    public void testClassMultipleSameDataSet() throws Throwable
    {
        // One data set which is enforced on the class to perform two executions
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 1, run #1", //
                "test1 :: Data set 1 / 1, run #2"
            };
        checkDescription(ClassMultipleSameDataSet.class, expected);
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
                "test1 :: Data set 1 / 2", //
                "test1 :: Data set 2 / 2"
            };
        checkDescription(MethodDefaultTwoDataSet.class, expected);
    }

    @Test
    public void testMethodExplicitDefaultTwoDataSets() throws Throwable
    {
        // Two data sets and explicit @Testdata(-1) on method
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 2", //
                "test1 :: Data set 2 / 2"
            };
        checkDescription(MethodExplicitDefaultTwoDataSets.class, expected);
    }

    @Test
    public void testMethodMultipleSameDataSet() throws Throwable
    {
        // One data set, one method, method enforced to run data set twice
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 1, run #1", //
                "test1 :: Data set 1 / 1, run #2"
            };
        checkDescription(MethodMultipleSameDataSet.class, expected);
    }

    // mixed tests

    @Test
    public void testOneDataSetTwoMethodsOneWithout() throws Throwable
    {
        // One data set, two methods, one method with @Testdata(0)
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 1", //
                "test2"
            };
        checkDescription(OneDataSetTwoMethodsOneWithout.class, expected);
    }

    @Test
    public void testTwoDataSetsTwoMethodsOneWithout() throws Throwable
    {
        // One data set, two methods, one method with @Testdata(0)
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 2", //
                "test1 :: Data set 2 / 2", //
                "test2"
            };
        checkDescription(TwoDataSetsTwoMethodsOneWithout.class, expected);
    }

    @Test
    public void testTwoDataSetsTwoMethodsOneForced() throws Throwable
    {
        // One data set, two methods, one method with @Testdata(0)
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 2", //
                "test1 :: Data set 2 / 2", //
                "test2 :: Data set 1 / 2"
            };
        checkDescription(TwoDataSetsTwoMethodsOneForced.class, expected);
    }

    @Test
    public void testClassWithoutTwoMethodsOneForced() throws Throwable
    {
        // One data set, two methods, one method with @Testdata(0)
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 1", //
                "test2"
            };
        checkDescription(ClassWithoutTwoMethodsOneForced.class, expected);
    }

    @Test
    public void testForceOfNoneDataSets() throws Exception
    {
        Result result = JUnitCore.runClasses(ForceOfNoneDataSets.class);
        checkFail(result, 1, 0, 1,
                  "java.lang.IllegalArgumentException: Method 'test1' is marked to be run with data set index 2, but there are only 0");
    }

    @Test
    public void testOnlyImplicitOneDataSet() throws Throwable
    {
        String[] expected = new String[]
            {
                "test1 :: Data set 1 / 1", //
            };
        checkDescription(OnlyImplicitOneDataSet.class, expected);
    }

}
