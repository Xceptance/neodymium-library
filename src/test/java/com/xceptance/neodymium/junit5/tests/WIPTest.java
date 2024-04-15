package com.xceptance.neodymium.junit5.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.xceptance.neodymium.junit5.testclasses.wip.WIPAnnotationChildTest;
import com.xceptance.neodymium.junit5.testclasses.wip.WIPAnnotationTest;
import com.xceptance.neodymium.junit5.testclasses.wip.WIPMultiplicationTest;
import com.xceptance.neodymium.junit5.tests.utils.NeodymiumTestExecutionSummary;
import com.xceptance.neodymium.util.Neodymium;

public class WIPTest extends AbstractNeodymiumTest
{
    @BeforeEach
    public void setJUnitViewModeFlat()
    {
        Neodymium.configuration().setProperty("neodymium.junit.viewmode", "flat");
        Neodymium.configuration().setProperty("neodymium.workInProgress", "true");
    }

    @Test
    public void testWIPAnnotation() throws Throwable
    {
        String[] expected = new String[]
        {
          "first"
        };
        checkDescription(WIPAnnotationTest.class, expected);

        NeodymiumTestExecutionSummary result = run(WIPAnnotationTest.class);
        checkPass(result, 1, 0);
    }

    @Test
    public void testWIPAnnotationChild() throws Throwable
    {
        String[] expected = new String[]
        {
          "first",
          "third"
        };
        checkDescription(WIPAnnotationChildTest.class, expected);

        NeodymiumTestExecutionSummary result = run(WIPAnnotationChildTest.class);
        checkPass(result, 2, 0);
    }

    @Test
    public void testWIPMultiplication() throws Throwable
    {
        String[] expected = new String[]
        {
          "first :: 1 1 / 2 :: Browser Chrome_headless",
          "first :: 2 2 / 2 :: Browser Chrome_headless",
          "first :: 1 1 / 2 :: Browser Chrome_1500x1000_headless",
          "first :: 2 2 / 2 :: Browser Chrome_1500x1000_headless",
        };
        checkDescription(WIPMultiplicationTest.class, expected);
        NeodymiumTestExecutionSummary result = run(WIPMultiplicationTest.class);
        checkPass(result, 4, 0);
    }
}
