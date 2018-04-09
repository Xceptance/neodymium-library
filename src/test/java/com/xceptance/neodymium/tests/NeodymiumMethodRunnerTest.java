package com.xceptance.neodymium.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.xceptance.neodymium.testclasses.method.FailingMethod;
import com.xceptance.neodymium.testclasses.method.IgnoredClass;
import com.xceptance.neodymium.testclasses.method.NoTestMethods;
import com.xceptance.neodymium.testclasses.method.TestAndIgnoreAnnotation;

public class NeodymiumMethodRunnerTest
{
    @Test
    public void testNoTestMethod()
    {
        // test there is nothing when no test method was found
        Result result = JUnitCore.runClasses(NoTestMethods.class);

        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(0, result.getIgnoreCount());

        Failure failure = result.getFailures().get(0);
        Assert.assertEquals("No runnable methods", failure.getMessage());
    }

    @Test
    public void testTestAndIgnoreAnnotation()
    {
        // test that NeodymiumRunner handles @Test and @Ignore correctly
        Result result = JUnitCore.runClasses(TestAndIgnoreAnnotation.class);

        Assert.assertTrue(result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(0, result.getFailureCount());
        Assert.assertEquals(1, result.getIgnoreCount());
    }

    @Test
    public void testIgnoredClass()
    {
        // no method should be invoked in an ignored class
        Result result = JUnitCore.runClasses(IgnoredClass.class);

        Assert.assertEquals(0, result.getRunCount());
        Assert.assertEquals(0, result.getFailureCount());
        Assert.assertEquals(1, result.getIgnoreCount());
    }

    @Test
    public void testMethodFailing()
    {
        // test that a failing method fails the test
        Result result = JUnitCore.runClasses(FailingMethod.class);

        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(0, result.getIgnoreCount());
    }

}
