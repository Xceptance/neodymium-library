package com.xceptance.neodymium.testclasses.data.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataItem;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
public class InstantiateFieldViaAnnotation
{
    @DataItem("$.name")
    private String name;

    @Test
    public void test1()
    {
        Assert.assertEquals("John" + DataUtils.asString("testId"), name);
    }
}
