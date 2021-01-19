package com.xceptance.neodymium.testclasses.data;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;

@RunWith(NeodymiumRunner.class)
@DataSet(randomSets = 4)
public class RandomDataSetsException
{
    @Test
    public void test()
    {
    }
}
