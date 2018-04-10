package com.xceptance.neodymium.tests;

import org.junit.Assert;
import org.junit.runner.Result;

public abstract class NeodymiumTest
{
    public void check(Result result, boolean expectedSuccessful, int expectedRunCount, int expectedIgnoreCount, int expectedFailCount,
                      String expectedFailureMessage)
    {
        Assert.assertEquals("Test successful", expectedSuccessful, result.wasSuccessful());
        Assert.assertEquals("Method run count", expectedRunCount, result.getRunCount());
        Assert.assertEquals("Method ignore count", expectedIgnoreCount, result.getIgnoreCount());
        Assert.assertEquals("Method fail count", expectedFailCount, result.getFailureCount());

        if (expectedFailureMessage != null)
        {
            Assert.assertTrue("Failure count", expectedFailCount == 1);
            Assert.assertEquals("Failure message", expectedFailureMessage, result.getFailures().get(0).getMessage());
        }
    }

    public void checkPass(Result result, int expectedRunCount, int expectedIgnoreCount, int expectedFailCount)
    {
        check(result, true, expectedRunCount, expectedIgnoreCount, expectedFailCount, null);
    }

    public void checkFail(Result result, int expectedRunCount, int expectedIgnoreCount, int expectedFailCount,
                          String expectedFailureMessage)
    {
        check(result, false, expectedRunCount, expectedIgnoreCount, expectedFailCount, expectedFailureMessage);
    }
}
