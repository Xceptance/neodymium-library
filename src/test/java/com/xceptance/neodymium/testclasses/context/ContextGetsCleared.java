package com.xceptance.neodymium.testclasses.context;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;
import com.xceptance.neodymium.module.statement.testdata.SuppressDataSets;
import com.xceptance.neodymium.util.Context;

@RunWith(NeodymiumRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContextGetsCleared
{
    @Test
    @DataSet
    public void test1() throws Exception
    {
        // there is one data set -> key1 = val1
        Assert.assertEquals("val1", Context.dataValue("key1"));
    }

    @Test
    @SuppressDataSets
    public void test2() throws Exception
    {
        // supressing data sets makes sure we dont
        Assert.assertNull(Context.dataValue("key1"));
    }
}
