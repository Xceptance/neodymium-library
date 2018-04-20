package com.xceptance.neodymium.testclasses.parameter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class GeneratorObjectReturn
{
    @Parameters
    public static Object createData()
    {
        return null;
    }

    @Parameter
    public Integer testInt;

    @Test
    public void test()
    {
        Assert.assertNull(testInt);
    }
}
