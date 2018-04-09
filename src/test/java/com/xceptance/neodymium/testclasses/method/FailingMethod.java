package com.xceptance.neodymium.testclasses.method;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class FailingMethod
{
    @Test
    public void fail()
    {
        Assert.fail();
    }
}
