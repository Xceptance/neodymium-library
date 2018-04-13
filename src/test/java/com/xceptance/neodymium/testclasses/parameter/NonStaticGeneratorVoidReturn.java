package com.xceptance.neodymium.testclasses.parameter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class NonStaticGeneratorVoidReturn
{
    @Parameters
    public void createData()
    {
    }

    @Parameter
    private String testString;

    @Test
    public void test()
    {

    }
}
