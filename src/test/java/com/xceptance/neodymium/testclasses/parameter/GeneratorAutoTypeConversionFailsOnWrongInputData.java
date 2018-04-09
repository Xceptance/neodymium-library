package com.xceptance.neodymium.testclasses.parameter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class GeneratorAutoTypeConversionFailsOnWrongInputData
{
    @Parameters
    public static Object[] createData()
    {
        return new Object[]
            {
                "true"
            };
    }

    @Parameter(0)
    public double aDouble;

    @Test
    public void test()
    {
        System.out.println(aDouble);
    }
}
