package com.xceptance.neodymium.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.browser.EmptyBrowser;
import com.xceptance.neodymium.testclasses.browser.NoBrowserTag;

public class NeodymiumBrowserRunnerTest
{
    @Test
    public void testEmptyBrowserTag()
    {
        // an empty @Browser({}) annotation shouldn't raise an error and shouldn't invoke a method
        Result result = JUnitCore.runClasses(NoBrowserTag.class);
        Assert.assertEquals(0, result.getRunCount());
        Assert.assertEquals(0, result.getFailureCount());
        Assert.assertEquals(0, result.getIgnoreCount());
    }

    @Test
    public void testEmptyBrowser()
    {
        // an empty browser tag (@Browser({""})) should raise an error
        Result result = JUnitCore.runClasses(EmptyBrowser.class);

        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(0, result.getIgnoreCount());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals("Can not find browser configuration with tag: ", result.getFailures().get(0).getMessage());
    }
}
