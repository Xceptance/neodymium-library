package com.xceptance.neodymium.testclasses.data.override.mixed;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;
import com.xceptance.neodymium.module.statement.testdata.SkipDataSet;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
@SkipDataSet(1)
public class MixedSkipDataSetAnnotations
{
    @Test
    @SkipDataSet(2)
    public void testSomething4()
    {
        String key = DataUtils.asString("key1");
        Assert.assertTrue("Test should only run for 1 and 3 data set", key.equals("val1") || key.equals("val3"));
        // should run 1 and 3
    }

    @Test
    @DataSet(2)
    @SkipDataSet(2)
    public void testSomething5()
    {
        String key = DataUtils.asString("key1", null);
        Assert.assertNull("Test should run with supressed data sets", key);
        // should run without data set (same as SuppressDataSets) ???
    }
}
