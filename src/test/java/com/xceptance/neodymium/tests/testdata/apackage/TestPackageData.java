package com.xceptance.neodymium.tests.testdata.apackage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.util.Context;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
public class TestPackageData
{
    @Test
    public void testAmountOfAvailableData()
    {
        Assert.assertEquals("The data pool should have the correct number of items", 3, Context.get().data.size());
    }

    @Test
    public void testSamePackage()
    {
        Assert.assertEquals("The package data from this package", "6789", DataUtils.asString("packageField3"));
    }

    @Test
    public void testParentPackage()
    {
        Assert.assertEquals("The package data from parent package", "abc", DataUtils.asString("packageField1"));
    }

    @Test
    public void testPackageInheritance()
    {
        Assert.assertEquals("The package data from this package should overwrite the parenty data", "xyz",
                            DataUtils.asString("packageField2"));
    }

    @Test
    public void testPackageFileEndingHierachy()
    {
        Assert.assertEquals("The package data from this package should overwrite the parenty data", null,
                            DataUtils.asString("packageField4"));
    }

}
