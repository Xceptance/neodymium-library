package com.xceptance.neodymium.testclasses.data.override.methodonly;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;

@RunWith(NeodymiumRunner.class)
public class MethodRandomDataSets
{
    @Test
    @DataSet(randomSets = 4)
    public void test()
    {
    }
}
