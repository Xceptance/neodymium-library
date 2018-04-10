package com.xceptance.neodymium.testclasses.data.pkg.json;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.util.Context;

@RunWith(NeodymiumRunner.class)
public class CanReadPackageDataJson
{
    @Test
    public void test()
    {
        Map<String, String> data = Context.get().data;
        Assert.assertEquals(2, data.size());
        Assert.assertEquals("Json Value1", data.get("pkgParam1"));
        Assert.assertEquals("Json Value2", data.get("pkgParam2"));
    }
}
