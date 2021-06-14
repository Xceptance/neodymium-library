package com.xceptance.neodymium.testclasses.data.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataItem;

@RunWith(NeodymiumRunner.class)
public class InstantiateFieldViaAnnotation
{
    @DataItem
    private String name;

    @DataItem
    private String testId;

    @Test
    public void test1()
    {
        Assert.assertEquals("John" + testId, name);
    }
}
