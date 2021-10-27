package com.xceptance.neodymium.junit4.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.junit4.testclasses.softassertion.UseSoftAssertions;

public class SoftAssertionTest extends NeodymiumTest
{
    @Test
    public void validateSoftAssertion()
    {
        Result result = JUnitCore.runClasses(UseSoftAssertions.class);
        checkFail(result, 1, 0, 1);
    }
}
