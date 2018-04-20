package com.xceptance.neodymium.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.parameter.GeneratorAutoTypeConversion;
import com.xceptance.neodymium.testclasses.parameter.GeneratorAutoTypeConversionCanNotHandleArbitraryTypes;
import com.xceptance.neodymium.testclasses.parameter.GeneratorAutoTypeConversionFailsOnWrongInputData;
import com.xceptance.neodymium.testclasses.parameter.GeneratorCanNotSetFinalField;
import com.xceptance.neodymium.testclasses.parameter.GeneratorCanNotSetPrivateField;
import com.xceptance.neodymium.testclasses.parameter.GeneratorCanSetStaticField;
import com.xceptance.neodymium.testclasses.parameter.GeneratorIterableReturnOne;
import com.xceptance.neodymium.testclasses.parameter.GeneratorObjectReturn;
import com.xceptance.neodymium.testclasses.parameter.GeneratorTooFewElements;
import com.xceptance.neodymium.testclasses.parameter.GeneratorTooMuchElements;
import com.xceptance.neodymium.testclasses.parameter.GeneratorVoidReturn;
import com.xceptance.neodymium.testclasses.parameter.NonStaticGeneratorVoidReturn;
import com.xceptance.neodymium.testclasses.parameter.ParameterFieldButNoGenerator;

public class NeodymiumParameterRunnerTest extends NeodymiumTest
{
    @Test
    public void testParameterFieldWithoutGenerator()
    {
        // test parameter annotated class members without an generator function (@Parameters)
        Result result = JUnitCore.runClasses(ParameterFieldButNoGenerator.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testNonStaticGeneratorVoidReturn()
    {
        // test a non static generator
        Result result = JUnitCore.runClasses(NonStaticGeneratorVoidReturn.class);
        checkFail(result, 1, 0, 1, "No public static parameters method on class " + NonStaticGeneratorVoidReturn.class.getCanonicalName());
    }

    @Test
    public void testGeneratorVoidReturn()
    {
        // test generator void return type
        Result result = JUnitCore.runClasses(GeneratorVoidReturn.class);
        checkFail(result, 1, 0, 1, GeneratorVoidReturn.class.getCanonicalName() + ".createData() must return an Iterable of arrays.");
    }

    @Test
    public void testGeneratorObjectReturn()
    {
        // test generator returning object is not accidently castet to correct type
        Result result = JUnitCore.runClasses(GeneratorObjectReturn.class);
        checkFail(result, 1, 0, 1, GeneratorObjectReturn.class.getCanonicalName() + ".createData() must return an Iterable of arrays.");
    }

    @Test
    public void testGeneratorIterableReturnOne()
    {
        // one test data element, one test iteration
        Result result = JUnitCore.runClasses(GeneratorIterableReturnOne.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testGeneratorTooFewElements()
    {
        // one test iteration with two parameter fields, but just one data set
        Result result = JUnitCore.runClasses(GeneratorTooFewElements.class);
        checkFail(result, 1, 0, 1, "java.lang.Exception: Number of parameters (1) and fields (2) annotated with @Parameter must match!");
    }

    @Test
    public void testGeneratorTooMuchElements()
    {
        // one test iteration with one data field, but two data sets
        Result result = JUnitCore.runClasses(GeneratorTooMuchElements.class);
        checkFail(result, 1, 0, 1, "java.lang.Exception: Number of parameters (2) and fields (1) annotated with @Parameter must match!");
    }

    @Test
    public void testGeneratorAutoTypeConversion()
    {
        // test auto type conversion from string to various data types, aswell as arbitrary type injection
        Result result = JUnitCore.runClasses(GeneratorAutoTypeConversion.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testGeneratorAutoTypeConversionCanNotHandleArbitraryTypes()
    {
        // test that auto type conversion from string to an arbitrary type fails
        Result result = JUnitCore.runClasses(GeneratorAutoTypeConversionCanNotHandleArbitraryTypes.class);
        checkFail(result, 1, 0, 1,
                  "java.lang.RuntimeException: Could not set parameter of type class java.lang.String to field \"browser\" of type class com.xceptance.neodymium.multibrowser.configuration.BrowserConfiguration. Value: a string can not be parsed to an arbitrary type");
    }

    @Test
    public void testGeneratorCanSetStaticField()
    {
        // test that a static field can be set
        Result result = JUnitCore.runClasses(GeneratorCanSetStaticField.class);
        checkPass(result, 1, 0, 0);
    }

    @Test
    public void testGeneratorCanNotSetFinalField()
    {
        // test that a final field can not be set
        Result result = JUnitCore.runClasses(GeneratorCanNotSetFinalField.class);
        checkFail(result, 1, 0, 1, "java.lang.RuntimeException: Could not set parameter due to it is not public or it is final");
    }

    @Test
    public void testGeneratorCanNotSetPrivateSField()
    {
        // test that a private field can not be set
        Result result = JUnitCore.runClasses(GeneratorCanNotSetPrivateField.class);
        checkFail(result, 1, 0, 1, "java.lang.RuntimeException: Could not set parameter due to it is not public or it is final");
    }

    @Test
    public void testGeneratorAutoTypeConversionFailsOnWrongInputData()
    {
        // test that auto type conversion from string fails if string content can not match
        Result result = JUnitCore.runClasses(GeneratorAutoTypeConversionFailsOnWrongInputData.class);
        checkFail(result, 1, 0, 1,
                  "java.lang.RuntimeException: An error occured during conversion of input string \"true\" to type double for field \"aDouble\"");
    }
}
