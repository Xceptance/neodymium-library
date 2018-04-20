package com.xceptance.neodymium.testclasses.method;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
@Ignore
public class IgnoredClass
{
    @Test
    public void test()
    {
        Assert.fail();
    }

    @Test
    @Ignore
    public void ignoredTestMethod()
    {
        Assert.fail();
    }

    public void foo()
    {

    }
}
