package com.xceptance.neodymium.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.softassertion.UseSoftAssertions;

public class SoftAssertionTest extends NeodymiumTest
{
    @Test
    public void validateSoftAssertion()
    {
        Result result = JUnitCore.runClasses(UseSoftAssertions.class);
        checkFail(result, 1, 0, 1);
    }
}
