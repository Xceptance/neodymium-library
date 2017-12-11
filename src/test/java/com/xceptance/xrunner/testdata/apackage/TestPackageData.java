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
    public void testAmountOfAvailableData()
    {
        Assert.assertEquals("The data pool should have the correct number of items", 3, data.size());
    }

    @Test
    public void testSamePackage()
    {
        Assert.assertEquals("The package data from this package", "6789", data.get("packageField3"));
    }

    @Test
    public void testParentPackage()
    {
        Assert.assertEquals("The package data from parent package", "abc", data.get("packageField1"));
    }

    @Test
    public void testPackageInheritance()
    {
        Assert.assertEquals("The package data from this package should overwrite the parenty data", "xyz", data.get("packageField2"));
    }

    @Test
    public void testPackageFileEndingHierachy()
    {
        Assert.assertEquals("The package data from this package should overwrite the parenty data", null, data.get("packageField4"));
    }

}
