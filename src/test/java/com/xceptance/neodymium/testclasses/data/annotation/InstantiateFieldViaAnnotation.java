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
    protected String testId;

    @DataItem
    private String stringDefaultValue;

    @DataItem
    private int intDefaultValue;

    @DataItem
    private long longDefaultValue;

    @DataItem
    private float floatDefaultValue;

    @DataItem
    private double doubleDefaultValue;

    @DataItem
    private boolean booleanDefaultValue;

    @Test
    public void test1()
    {
        Assert.assertEquals("Data injection doesn't work as expected", "John" + testId, name);
        validateDataItemDefaultValues();
    }

    protected void validateDataItemDefaultValues()
    {
        Assert.assertNull("Default value for string data item should be null", stringDefaultValue);
        Assert.assertEquals("Default value for integer data item should be 0", intDefaultValue, 0);
        Assert.assertEquals("Default value for long data item should be 0", longDefaultValue, 0);
        Assert.assertEquals("Default value for float data item should be 0.0f", floatDefaultValue, 0.0f, 0.001f);
        Assert.assertEquals("Default value for double data item should be 0.0", doubleDefaultValue, 0.0, 0.001);
        Assert.assertFalse("Default value for boolean data item should be false", booleanDefaultValue);
    }
}
