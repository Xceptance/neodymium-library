package com.xceptance.neodymium.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.method.BeforeAndAfter;
import com.xceptance.neodymium.testclasses.method.ConstructorThrowsCheckedException;
import com.xceptance.neodymium.testclasses.method.ConstructorThrowsError;
import com.xceptance.neodymium.testclasses.method.FailingMethod;
import com.xceptance.neodymium.testclasses.method.IgnoredClass;
import com.xceptance.neodymium.testclasses.method.NoTestMethods;
import com.xceptance.neodymium.testclasses.method.TestAndIgnoreAnnotation;

public class NeodymiumMethodRunnerTest extends NeodymiumTest
{
    @Test
    public void testNoTestMethod()
    {
        // test there is nothing when no test method was found
        Result result = JUnitCore.runClasses(NoTestMethods.class);
        checkFail(result, 1, 0, 1, "No runnable methods");
    }

    @Test
    public void testTestAndIgnoreAnnotation()
    {
        // test that NeodymiumRunner handles @Test and @Ignore correctly
        Result result = JUnitCore.runClasses(TestAndIgnoreAnnotation.class);
        checkPass(result, 1, 1, 0);
    }

    @Test
    public void testIgnoredClass()
    {
        // no method should be invoked in an ignored class
        Result result = JUnitCore.runClasses(IgnoredClass.class);
        checkPass(result, 0, 1, 0);
    }

    @Test
    public void testMethodFailing()
    {
        // test that a failing method fails the test
        Result result = JUnitCore.runClasses(FailingMethod.class);
        checkFail(result, 1, 0, 1, null);
    }

    @Test
    public void testConstructorThrowsCheckedException()
    {
        // test that the test fails if the constructor throws an exception
        Result result = JUnitCore.runClasses(ConstructorThrowsCheckedException.class);
        checkFail(result, 1, 0, 1, "construction failed");
    }

    @Test
    public void testConstructorThrowsError()
    {
        // test that the test fails if the constructor throws an error
        Result result = JUnitCore.runClasses(ConstructorThrowsError.class);
        checkFail(result, 1, 0, 1, null);
    }

    @Test
    public void testBeforeAndAfter()
    {
        // test @Before, @After, @BeforeClass, @AfterClass
        Result result = JUnitCore.runClasses(BeforeAndAfter.class);
        checkPass(result, 2, 0, 0);
        String[] expected = new String[]
            {
                "beforeClass", //
                "beforeMethod", //
                "first", //
                "afterMethod", //
                "beforeMethod", //
                "second", //
                "afterMethod", //
                "afterClass"
            };
        String[] trace = BeforeAndAfter.TRACE.toArray(new String[0]);
        Assert.assertArrayEquals(expected, trace);
    }

}
