package com.xceptance.neodymium.testclasses.method;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class TestAndIgnoreAnnotation
{
    // only methods annotated with test should be called
    // ignoring a class or method should override any test annotation

    public void noTestMethod()
    {
        Assert.fail();
    }

    @Test
    public void testMethod()
    {
    }

    @Ignore
    public void ignoredMethod()
    {
        Assert.fail();
    }

    @Test
    @Ignore
    public void ignoredTestMethod()
    {
        Assert.fail();
    }

}
