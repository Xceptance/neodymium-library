package com.xceptance.neodymium.junit5.tests;


import org.junit.jupiter.api.Test;

import com.xceptance.neodymium.junit5.testclasses.softassertion.UseSoftAssertions;
import com.xceptance.neodymium.junit5.tests.utils.NeodymiumTestExecutionSummary;

public class SoftAssertionTest extends AbstractNeodymiumTest
{
    @Test
    public void validateSoftAssertion()
    {
        NeodymiumTestExecutionSummary summary = run(UseSoftAssertions.class);
        checkFail(summary, 1, 0, 1);
    }
}
