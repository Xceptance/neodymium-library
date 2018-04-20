package com.xceptance.neodymium.testclasses.data.set.properties;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.util.Context;

@RunWith(NeodymiumRunner.class)
public class CanReadDataSetProperties
{
    @Test
    public void test()
    {
        Map<String, String> data = Context.get().data;
        Assert.assertEquals(2, data.size());
        Assert.assertEquals("Properties Value1", data.get("testParam1"));
        Assert.assertEquals("Properties Value2", data.get("testParam2"));
    }
}
