package com.xceptance.neodymium.testclasses.data.annotation.inheritance;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.neodymium.module.statement.testdata.DataItem;

public class ChildInheritingValuesFromAnnotation extends ParentClassWithValuesFromAnnotation
{
    @DataItem
    private String testId;

    @Test
    public void test1()
    {
        Assert.assertEquals("John" + testId, name);
    }
}
