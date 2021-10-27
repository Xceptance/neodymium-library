package com.xceptance.neodymium.junit5.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.xceptance.neodymium.junit5.testclasses.data.file.json.CanReadDataSetJson;
import com.xceptance.neodymium.junit5.testclasses.data.file.xml.CanNotReadDataSetXml;
import com.xceptance.neodymium.junit5.testclasses.data.inheritance.child.PackageTestDataInheritance;
import com.xceptance.neodymium.junit5.testclasses.data.inheritance.child.grandchild.GrandChildPackageTestDataInheritance;
import com.xceptance.neodymium.junit5.testclasses.data.inheritance.child.grandchild.set.DataSetOverridesPackageData;
import com.xceptance.neodymium.junit5.testclasses.data.override.classonly.ClassDefaultValueEmptyDataSets;
import com.xceptance.neodymium.junit5.testclasses.data.override.classonly.ClassDefaultValueNoDataSets;
import com.xceptance.neodymium.junit5.testclasses.data.override.classonly.ClassDefaultValueOneDataSet;
import com.xceptance.neodymium.junit5.testclasses.data.override.classonly.ClassDefaultValueTwoDataSets;
import com.xceptance.neodymium.junit5.testclasses.data.override.classonly.ClassExplicitDefaultValueTwoDataSets;
import com.xceptance.neodymium.junit5.testclasses.data.override.classonly.ClassMultipleSameDataSet;
import com.xceptance.neodymium.junit5.testclasses.data.override.methodonly.MethodDefaultEmptyDataSets;
import com.xceptance.neodymium.junit5.testclasses.data.override.methodonly.MethodDefaultNoDataSets;
import com.xceptance.neodymium.junit5.testclasses.data.override.methodonly.MethodDefaultOneDataSet;
import com.xceptance.neodymium.junit5.testclasses.data.override.methodonly.MethodDefaultTwoDataSet;
import com.xceptance.neodymium.junit5.testclasses.data.override.methodonly.MethodExplicitDefaultTwoDataSets;
import com.xceptance.neodymium.junit5.testclasses.data.override.methodonly.MethodMultipleSameDataSet;
import com.xceptance.neodymium.junit5.testclasses.data.override.mixed.ClassWithoutTwoMethodsOneForced;
import com.xceptance.neodymium.junit5.testclasses.data.override.mixed.ForceOfNoneDataSets;
import com.xceptance.neodymium.junit5.testclasses.data.override.mixed.OneDataSetTwoMethodsOneWithout;
import com.xceptance.neodymium.junit5.testclasses.data.override.mixed.OnlyImplicitOneDataSet;
import com.xceptance.neodymium.junit5.testclasses.data.override.mixed.TwoDataSetsTwoMethodsOneForced;
import com.xceptance.neodymium.junit5.testclasses.data.override.mixed.TwoDataSetsTwoMethodsOneWithout;
import com.xceptance.neodymium.junit5.testclasses.data.pkg.csv.CanReadPackageDataCSV;
import com.xceptance.neodymium.junit5.testclasses.data.pkg.json.CanReadPackageDataJson;
import com.xceptance.neodymium.junit5.testclasses.data.pkg.properties.CanReadPackageDataProperties;
import com.xceptance.neodymium.junit5.testclasses.data.pkg.xml.CanReadPackageDataXML;
import com.xceptance.neodymium.junit5.testclasses.data.set.csv.CanReadDataSetCSV;
import com.xceptance.neodymium.junit5.testclasses.data.set.testid.DuplicateTestId;
import com.xceptance.neodymium.junit5.testclasses.data.set.testid.SpecialCharacterTestId;
import com.xceptance.neodymium.junit5.testclasses.data.set.xml.CanReadDataSetXML;
import com.xceptance.neodymium.junit5.tests.utils.NeodymiumTestExecutionSummary;
import com.xceptance.neodymium.util.Neodymium;

public class TestDataStatementTest extends AbstractNeodymiumTest
{
    @BeforeEach
    public void setJUnitViewModeFlat()
    {
        Neodymium.configuration().setProperty("neodymium.junit.viewmode", "flat");
    }

    @Test
    public void testCanReadPackageDataCSV()
    {
        // test package test data csv is read
        NeodymiumTestExecutionSummary result = run(CanReadPackageDataCSV.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testCanReadPackageDataJson()
    {
        // test package test data json is read
        NeodymiumTestExecutionSummary result = run(CanReadPackageDataJson.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testCanReadPackageDataProperties()
    {
        // test package test data properties is read
        NeodymiumTestExecutionSummary result = run(CanReadPackageDataProperties.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testCanReadPackageDataXML()
    {
        // test package test data xml is read
        NeodymiumTestExecutionSummary result = run(CanReadPackageDataXML.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testCanReadDataSetCSV()
    {
        // test data set csv is read
        NeodymiumTestExecutionSummary result = run(CanReadDataSetCSV.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testCanReadDataSetJson()
    {
        // test data set json is read
        NeodymiumTestExecutionSummary result = run(CanReadDataSetJson.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testCanReadDataSetXML()
    {
        // test data set xml is read
        NeodymiumTestExecutionSummary result = run(CanReadDataSetXML.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testPackageTestDataInheritance()
    {
        // test inheritance of package test data
        NeodymiumTestExecutionSummary result = run(PackageTestDataInheritance.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testGrandChildPackageTestDataInheritance()
    {
        // test multiple inheritance of package test data
        NeodymiumTestExecutionSummary result = run(GrandChildPackageTestDataInheritance.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testDataSetOverridesPackageData()
    {
        // test that data set overrides package test data
        NeodymiumTestExecutionSummary result = run(DataSetOverridesPackageData.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testDuplicateTestId() throws Exception
    {
        // more than one entry with the same "testId"
        NeodymiumTestExecutionSummary result = run(DuplicateTestId.class);
        checkPass(result, 6, 0);
    }

    @Test
    public void testSpecialCharacterTestId() throws Throwable
    {
        // special characters in testId
        // parenthesis will be converted to to brackets
        NeodymiumTestExecutionSummary result = run(SpecialCharacterTestId.class);
        checkPass(result, 7, 0);
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
          "test1 :: Data set 1 / 2",
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
          "test1 :: Data set 1 / 2",
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
          "test1 :: Data set 1 / 1, run #1",
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
          "test1 :: Data set 1 / 2",
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
          "test1 :: Data set 1 / 2",
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
          "test1 :: Data set 1 / 1, run #1",
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
          "test1 :: Data set 1 / 1",
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
          "test1 :: Data set 1 / 2",
          "test1 :: Data set 2 / 2",
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
          "test1 :: Data set 1 / 2",
          "test1 :: Data set 2 / 2",
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
          "test1 :: Data set 1 / 1",
          "test2"
        };
        checkDescription(ClassWithoutTwoMethodsOneForced.class, expected);
    }

    @Test
    public void testForceOfNoneDataSets() throws Exception
    {
        NeodymiumTestExecutionSummary summary = run(ForceOfNoneDataSets.class);
        checkFail(summary, 1, 0, 1,
                  "java.lang.IllegalArgumentException: Method 'test1' is marked to be run with data set index 2, but there are only 0 available");
    }

    @Test
    public void testOnlyImplicitOneDataSet() throws Throwable
    {
        String[] expected = new String[]
        {
          "test1 :: Data set 1 / 1",
        };
        checkDescription(OnlyImplicitOneDataSet.class, expected);
    }

    @Test
    public void testDataFileAnnotation() throws Exception
    {
        // test package test data csv is read
        NeodymiumTestExecutionSummary result = run(com.xceptance.neodymium.junit5.testclasses.data.file.json.CanReadDataSetJson.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testDataFileAnnotationException() throws Exception
    {
        NeodymiumTestExecutionSummary summary = run(CanNotReadDataSetXml.class);
        checkFail(summary, 1, 0, 1,
                  "java.lang.RuntimeException: The data file:\"can/not/read/data/set/xml/DoesNotExist.xml\" provided within the test class:\"CanNotReadDataSetXml\" can't be read.");
    }
}
