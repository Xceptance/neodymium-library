package com.xceptance.neodymium.testclasses.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;
import com.xceptance.neodymium.module.statement.testdata.SuppressDataSets;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
public class FixedRandomnessOfDataSets
{
    private static List<String> datasets = new ArrayList<String>();

    @Test
    @DataSet(randomSets = 4)
    public void test1()
    {
        // assert test data is available for the test
        Assert.assertTrue(DataUtils.asString("key1").contains("val"));
        datasets.add(DataUtils.asString("key1"));
    }

    @Test
    @SuppressDataSets
    public void test2()
    {
        Assert.assertEquals("val2", datasets.get(0));
        Assert.assertEquals("val4", datasets.get(1));
        Assert.assertEquals("val1", datasets.get(2));
        Assert.assertEquals("val3", datasets.get(3));
    }
}
