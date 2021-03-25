package com.xceptance.neodymium.testclasses.data.override.mixed;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.RandomDataSets;

@RandomDataSets(4)
@RunWith(NeodymiumRunner.class)
public class OverrideClassRandomDataSetsOnMethodLevel
{
    @Test
    @RandomDataSets(0)
    public void test()
    {
    }
}
