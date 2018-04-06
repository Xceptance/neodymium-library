package com.xceptance.neodymium.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.xceptance.neodymium.testclasses.parameter.GeneratorIterableReturnOne;
import com.xceptance.neodymium.testclasses.parameter.GeneratorObjectReturn;
import com.xceptance.neodymium.testclasses.parameter.GeneratorToFewElements;
import com.xceptance.neodymium.testclasses.parameter.GeneratorToMuchElements;
import com.xceptance.neodymium.testclasses.parameter.GeneratorVoidReturn;
import com.xceptance.neodymium.testclasses.parameter.NonStaticGeneratorVoidReturn;
import com.xceptance.neodymium.testclasses.parameter.ParameterFieldButNoGenerator;

public class NeodymiumParameterRunnerTest
{
    @Test
    public void testParameterFieldWithoutGenerator()
    {
        // test parameter annotated class members without an generator function (@Parameters)
        Result result = JUnitCore.runClasses(ParameterFieldButNoGenerator.class);

        Assert.assertTrue(result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(0, result.getIgnoreCount());
        Assert.assertEquals(0, result.getFailureCount());
    }

    @Test
    public void testNonStaticGeneratorVoidReturn()
    {
        // test a non static generator
        Result result = JUnitCore.runClasses(NonStaticGeneratorVoidReturn.class);

        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(0, result.getIgnoreCount());
        Assert.assertEquals(1, result.getFailureCount());

        Failure failure = result.getFailures().get(0);
        Assert.assertEquals("No public static parameters method on class " + NonStaticGeneratorVoidReturn.class.getCanonicalName(),
                            failure.getMessage());
    }

    @Test
    public void testGeneratorVoidReturn()
    {
        // test generator void return type
        Result result = JUnitCore.runClasses(GeneratorVoidReturn.class);

        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(0, result.getIgnoreCount());
        Assert.assertEquals(1, result.getFailureCount());

        Failure failure = result.getFailures().get(0);
        Assert.assertEquals(GeneratorVoidReturn.class.getCanonicalName() + ".createData() must return an Iterable of arrays.",
                            failure.getMessage());
    }

    @Test
    public void testGeneratorObjectReturn()
    {
        Result result = JUnitCore.runClasses(GeneratorObjectReturn.class);

        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(0, result.getIgnoreCount());

        Failure failure = result.getFailures().get(0);
        Assert.assertEquals(GeneratorObjectReturn.class.getCanonicalName() + ".createData() must return an Iterable of arrays.",
                            failure.getMessage());
    }

    @Test
    public void testGeneratorIterableReturnOne()
    {
        Result result = JUnitCore.runClasses(GeneratorIterableReturnOne.class);

        Assert.assertTrue(result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(0, result.getFailureCount());
        Assert.assertEquals(0, result.getIgnoreCount());
    }

    @Test
    public void testGeneratorToFewElements()
    {
        Result result = JUnitCore.runClasses(GeneratorToFewElements.class);

        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(0, result.getIgnoreCount());

        Failure failure = result.getFailures().get(0);
        Assert.assertEquals("java.lang.Exception: Number of parameters (1) and fields (2) annotated with @Parameter must match!",
                            failure.getMessage());
    }

    @Test
    public void testGeneratorToMuchElements()
    {
        Result result = JUnitCore.runClasses(GeneratorToMuchElements.class);

        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(0, result.getIgnoreCount());

        Failure failure = result.getFailures().get(0);
        Assert.assertEquals("java.lang.Exception: Number of parameters (2) and fields (1) annotated with @Parameter must match!",
                            failure.getMessage());
    }

}
