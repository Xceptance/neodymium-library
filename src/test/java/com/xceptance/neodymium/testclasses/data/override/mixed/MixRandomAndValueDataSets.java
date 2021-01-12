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
    @Test
    @DataSet(randomSets = 1)
    public void testWithRandomDataSet()
    {
        // assert test data is available for the test
        Assert.assertTrue(DataUtils.asString("key1").contains("val"));
    }

    @Test
    @DataSet(2)
    public void testWithExplicitDataSet()
    {
        Assert.assertEquals("val2", DataUtils.asString("key1"));
    }
}
