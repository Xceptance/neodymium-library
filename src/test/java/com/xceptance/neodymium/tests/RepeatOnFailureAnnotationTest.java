package com.xceptance.neodymium.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.repeat.classlevel.ClassRepeatOnFailureBrowserCombinationTest;
import com.xceptance.neodymium.testclasses.repeat.classlevel.ClassRepeatOnFailureTest;
import com.xceptance.neodymium.testclasses.repeat.classlevel.ClassRepeatOnFailureTestdataCombinationTest;
import com.xceptance.neodymium.testclasses.repeat.methodlevel.MethodRepeatOnFailureBrowserCombinationTest;
import com.xceptance.neodymium.testclasses.repeat.methodlevel.MethodRepeatOnFailureTest;
import com.xceptance.neodymium.testclasses.repeat.methodlevel.MethodRepeatOnFailureTestdataCombinationTest;
import com.xceptance.neodymium.testclasses.repeat.mix.OverridingRepeatOnFailureTest;

public class RepeatOnFailureAnnotationTest extends NeodymiumTest
{
    @Test
    public void testClassRepeatOnFailure()
    {
        Result result = JUnitCore.runClasses(ClassRepeatOnFailureTest.class);
        checkFail(result, 2, 0, 1, "Produce test failure number 3");
    }

    @Test
    public void testClassRepeatOnFailureBrowserCombination()
    {
        Result result = JUnitCore.runClasses(ClassRepeatOnFailureBrowserCombinationTest.class);
        checkFail(result, 4, 0, 2, "Produce test failure number 3");
    }

    @Test
    public void testClassRepeatOnFailureTestdataCombination()
    {
        Result result = JUnitCore.runClasses(ClassRepeatOnFailureTestdataCombinationTest.class);
        checkFail(result, 6, 0, 3, "Produce test failure number 3");
    }

    @Test
    public void testMethodRepeatOnFailure()
    {
        Result result = JUnitCore.runClasses(MethodRepeatOnFailureTest.class);
        checkFail(result, 2, 0, 1, "Produce test failure number 3");
    }

    @Test
    public void testMethodRepeatOnFailureBrowserCombination()
    {
        Result result = JUnitCore.runClasses(MethodRepeatOnFailureBrowserCombinationTest.class);
        checkFail(result, 4, 0, 2, "Produce test failure number 3");
    }

    @Test
    public void testMethodRepeatOnFailureTestdataCombination()
    {
        Result result = JUnitCore.runClasses(MethodRepeatOnFailureTestdataCombinationTest.class);
        checkFail(result, 6, 0, 3, "Produce test failure number 3");
    }

    @Test
    public void testOverridingRepeatOnFailure()
    {
        Result result = JUnitCore.runClasses(OverridingRepeatOnFailureTest.class);
        checkFail(result, 2, 0, 1, "Produce test failure number 3");
    }
}
