package com.xceptance.neodymium.testclasses.data.annotation.inheritance;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.neodymium.module.statement.testdata.DataItem;

public class ChildInheritingDtoFromAnnotation extends ParentClassWithDtoFromAnnotation
{
    @DataItem
    private String testId;

    @Test
    public void test1()
    {
        Assert.assertEquals("john" + testId + "@varmail.de", user.getEmail());
        Assert.assertEquals("neodymium" + testId, user.getPassword());
    }
}
