package com.xceptance.neodymium.testclasses.data.override.mixed;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
public class MixRandomAndValueDataSets
{
    @DataSet(
             randomSets = 1)
    @Test
    public void testWithRandomDataSet()
    {
    }

    @DataSet(2)
    @Test
    public void testWithExplicitDataSet()
    {
        Assert.assertEquals("val2", DataUtils.asString("key1"));
    }
}
