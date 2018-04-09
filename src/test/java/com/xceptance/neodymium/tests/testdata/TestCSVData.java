package com.xceptance.neodymium.tests.testdata;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.util.Context;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
public class TestCSVData
{
    @Test
    public void testAmountOfAvailableData()
    {
        Assert.assertEquals("The data pool should have the correct number of items", 6, Context.get().data.size());
    }

    @Test
    public void testCSV()
    {
        Assert.assertEquals("The field1 should be available.", "name", DataUtils.asString("field1"));
        Assert.assertEquals("The field2 should be available.", "1234", DataUtils.asString("field2"));
        Assert.assertEquals("The field3 should be available.", "12.3", DataUtils.asString("field3"));
        Assert.assertEquals("The field4 should be available.", "ßäüö", DataUtils.asString("field4"));
    }

    @Test
    public void testFieldNotAvailable()
    {
        Assert.assertEquals("The field123 should not be available.", null, DataUtils.asString("field123"));
    }

    @Test
    public void testPackageData()
    {
        Assert.assertEquals("The package data from this package should be available.", "abc", DataUtils.asString("packageField1"));
        Assert.assertEquals("The package data from this package should be available.", "1234", DataUtils.asString("packageField2"));
    }
}
