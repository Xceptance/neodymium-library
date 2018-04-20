package com.xceptance.neodymium.testclasses.data.pkg.xml;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.util.Context;

@RunWith(NeodymiumRunner.class)
public class CanReadPackageDataXML
{
    @Test
    public void test()
    {
        Map<String, String> data = Context.get().data;
        Assert.assertEquals(2, data.size());
        Assert.assertEquals("XML Value1", data.get("pkgParam1"));
        Assert.assertEquals("XML Value2", data.get("pkgParam2"));
    }
}
