package com.xceptance.neodymium.testclasses.data.inheritance.child.grandchild;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
public class GrandChildPackageTestDataInheritance
{
    @Test
    public void test()
    {
        Map<String, String> data = Neodymium.getData();
        Assert.assertEquals(4, data.size());
        Assert.assertEquals("grandChild", data.get("pkgInherited"));
        Assert.assertEquals("parentValue", data.get("parentKey"));
        Assert.assertEquals("childValue", data.get("childKey"));
        Assert.assertEquals("grandChildValue", data.get("grandChildKey"));
    }
}
