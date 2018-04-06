package com.xceptance.neodymium.testclasses.parameter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class GeneratorIterableReturnOne
{
    @Parameters
    public static Iterable<Integer> createData()
    {
        List<Integer> list = new ArrayList<>(1);
        list.add(new Integer(5));
        return list;
    }

    @Parameter
    public Integer testInt;

    @Test
    public void test()
    {
        Assert.assertEquals(5, testInt.intValue());
    }
}
