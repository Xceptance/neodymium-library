package com.xceptance.neodymium.testclasses.data.override.classonly;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;

@RunWith(NeodymiumRunner.class)
@DataSet(randomSets = 4)
public class ClassRandomDataSets
{
    @Test
    public void test()
    {
    }
}
