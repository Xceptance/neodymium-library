package com.xceptance.neodymium.testclasses.data.set.testid;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.util.Context;

@RunWith(NeodymiumRunner.class)
public class DuplicateTestId
{
    static int dataSet = 1;

    @Test
    public void testDuplicateTestId() throws Exception
    {
        if (dataSet == 1)
        {
            Assert.assertEquals("id1", Context.dataValue("testId"));
            Assert.assertEquals("value1", Context.dataValue("testParam1"));
        }
        else if (dataSet == 2)
        {
            Assert.assertEquals("id2", Context.dataValue("testId"));
            Assert.assertEquals("value2", Context.dataValue("testParam1"));
        }
        else if (dataSet == 3)
        {
            Assert.assertEquals("id1", Context.dataValue("testId"));
            Assert.assertEquals("value3", Context.dataValue("testParam1"));
        }
        dataSet++;
    }
}
