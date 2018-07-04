package com.xceptance.neodymium.testclasses.data.set.testid;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.util.Context;

@RunWith(NeodymiumRunner.class)
public class SpecialCharacterTestId
{
    static int dataSet = 1;

    @Test
    public void testParanthesis() throws Exception
    {
        switch (dataSet)
        {
            case 1:
                Assert.assertEquals("'", Context.dataValue("testId"));
                break;

            case 2:
                Assert.assertEquals("''", Context.dataValue("testId"));
                break;

            case 3:
                Assert.assertEquals("(", Context.dataValue("testId"));
                break;

            case 4:
                Assert.assertEquals(")", Context.dataValue("testId"));
                break;

            case 5:
                Assert.assertEquals("()", Context.dataValue("testId"));
                break;

            case 6:
                Assert.assertEquals(")(", Context.dataValue("testId"));
                break;

            case 7:
                Assert.assertEquals("(a)", Context.dataValue("testId"));
                break;

            default:
                Assert.fail("uncovered test");
                break;
        }
        dataSet++;
    }
}
