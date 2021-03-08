package com.xceptance.neodymium.testclasses.data.override.methodonly;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.RandomDataSets;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
public class MethodRandomDataSets
{
    @Test
    @RandomDataSets(4)
    public void test()
    {
        // assert test data is available for the test
        Assert.assertTrue(DataUtils.asString("key1").contains("val"));
    }
}
