package com.xceptance.neodymium.testclasses.multiplication.parameteranddataset;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class TwoDataSetsOneParameterSetOneMethod
{
    @Parameters
    public static List<Object[]> createData()
    {
        List<Object[]> iterations = new LinkedList<>();

        iterations.add(new Object[]
            {
                "data"
            });

        return iterations;
    }

    @Parameter
    public String parameter;

    @Test
    public void first()
    {
    }
}
