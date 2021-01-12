package com.xceptance.neodymium.testclasses.data.override.classonly;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
@DataSet(randomSets = 4)
public class ClassRandomDataSets
{
    @Test
    public void test()
    {
        // assert test data is available for the test
        Assert.assertTrue(DataUtils.asString("key1").contains("val"));
    }
}
