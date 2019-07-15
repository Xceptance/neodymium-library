package com.xceptance.neodymium.testclasses.context;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;
import com.xceptance.neodymium.module.statement.testdata.SuppressDataSets;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContextGetsCleared
{
    @Test
    @DataSet
    public void test1() throws Exception
    {
        // there is one data set -> key1 = val1
        Assert.assertEquals("val1", Neodymium.dataValue("key1"));
    }

    @Test
    @SuppressDataSets
    public void test2() throws Exception
    {
        // suppressing data sets makes sure we don't
        Assert.assertNull(Neodymium.dataValue("key1"));
    }
}
