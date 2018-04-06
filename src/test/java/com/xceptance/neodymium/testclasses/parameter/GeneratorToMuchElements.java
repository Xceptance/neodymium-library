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
public class GeneratorToMuchElements
{
    @Parameters
    public static List<Integer[]> createData()
    {
        List<Integer[]> iterations = new ArrayList<>(1);
        iterations.add(new Integer[]
            {
                5, 6
            });

        return iterations;
    }

    @Parameter
    public Integer testInt;

    @Test
    public void test()
    {
        Assert.assertEquals(5, testInt.intValue());
    }
}
