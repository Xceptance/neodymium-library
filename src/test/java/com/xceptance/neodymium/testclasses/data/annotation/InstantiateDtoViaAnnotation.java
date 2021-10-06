package com.xceptance.neodymium.testclasses.data.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataItem;

@RunWith(NeodymiumRunner.class)
public class InstantiateDtoViaAnnotation extends InstantiateFieldViaAnnotation
{
    @DataItem
    private User user;

    @Test
    public void test1()
    {
        Assert.assertEquals("john" + testId + "@varmail.de", user.getEmail());
        Assert.assertEquals("neodymium" + testId, user.getPassword());
        validateDataItemDefaultValues();
    }
}
