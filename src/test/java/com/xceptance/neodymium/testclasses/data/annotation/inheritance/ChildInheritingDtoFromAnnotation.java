package com.xceptance.neodymium.testclasses.data.annotation.inheritance;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.neodymium.util.DataUtils;

public class ChildInheritingDtoFromAnnotation extends ParentClassWithDtoFromAnnotation
{
    @Test
    public void test1()
    {
        Assert.assertEquals("john" + DataUtils.asString("testId") + "@varmail.de", user.getEmail());
        Assert.assertEquals("neodymium" + DataUtils.asString("testId"), user.getPassword());
    }
}
