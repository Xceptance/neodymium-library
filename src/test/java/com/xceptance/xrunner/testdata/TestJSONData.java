package com.xceptance.xrunner.testdata;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.TestData;

@RunWith(NeodymiumRunner.class)
public class TestJSONData
{
    @TestData
    public Map<String, String> data;

    @Test
    public void test()
    {
        Assert.assertEquals("name", data.get("field1"));
        Assert.assertEquals("1234", data.get("field2"));
        Assert.assertEquals("12.3", data.get("field3"));
        Assert.assertEquals("ßäüö", data.get("field4"));
    }
}
