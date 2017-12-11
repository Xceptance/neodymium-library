package com.xceptance.xrunner.testdata.apackage;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.TestData;

@RunWith(NeodymiumRunner.class)
public class TestPackageData
{

    @TestData
    public Map<String, String> data;

    @Test
    public void testSamePackage()
    {
        Assert.assertEquals("The package data from this package", "6789", data.get("field3"));
    }

    @Test
    public void testParentPackage()
    {
        Assert.assertEquals("The package data from parent package", "abc", data.get("field1"));
    }

    @Test
    public void testPackageInheritance()
    {
        Assert.assertEquals("The package data from this package should overwrite the parenty data", "xyz", data.get("field2"));
    }
}
