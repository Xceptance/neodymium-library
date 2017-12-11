package com.xceptance.xrunner.testdata;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.TestData;

@RunWith(NeodymiumRunner.class)
public class TestXMLData
{
    @TestData
    public Map<String, String> data;

    @Test
    public void testAmountOfAvailableData()
    {
        Assert.assertEquals("The data pool should have the correct number of items", 6, data.size());
    }

    @Test
    public void testXML()
    {
        Assert.assertEquals("The field1 should be available.", "name", data.get("field1"));
        Assert.assertEquals("The field2 should be available.", "1234", data.get("field2"));
        Assert.assertEquals("The field3 should be available.", "12.3", data.get("field3"));
        Assert.assertEquals("The field4 should be available.", "ßäüö", data.get("field4"));
    }

    @Test
    public void testFieldNotAvailable()
    {
        Assert.assertEquals("The field123 should not be available.", null, data.get("field123"));
    }

    @Test
    public void testPackageData()
    {
        Assert.assertEquals("The package data from this package should be available.", "abc", data.get("packageField1"));
        Assert.assertEquals("The package data from this package should be available.", "1234", data.get("packageField2"));
    }
}
