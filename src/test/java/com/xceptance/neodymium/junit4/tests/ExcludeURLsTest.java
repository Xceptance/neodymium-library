package com.xceptance.neodymium.junit4.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.junit4.testclasses.urlfiltering.ExcludeTest;

public class ExcludeURLsTest extends NeodymiumTest
{
    @Test
    public void testURLsExcluded()
    {
        Result result = JUnitCore.runClasses(ExcludeTest.class);
        checkFail(result, 2, 0, 2);
    }
}
