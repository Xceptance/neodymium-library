package com.xceptance.neodymium.testclasses.data.override.classonly;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;
import com.xceptance.neodymium.module.statement.testdata.SkipDataSet;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
@SkipDataSet(1)
public class ClassSkipDataSetAnnotations
{
    @Test
    public void testSomething1()
    {
        String key = DataUtils.asString("key1");
        Assert.assertTrue("Test should only run for 2 and 3 data set", key.equals("val2") || key.equals("val3"));
        // should run 2 and 3
    }

    @Test
    @DataSet(1)
    public void testSomething2()
    {
        String key = DataUtils.asString("key1");
        Assert.assertTrue("Test should only run for 1 data set", key.equals("val1"));
        // should run 1 ???
    }

    @Test
    @DataSet(2)
    public void testSomething3()
    {
        String key = DataUtils.asString("key1");
        Assert.assertTrue("Test should only run for 2 data set", key.equals("val2"));
        // should run 2
    }
}
