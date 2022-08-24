package com.xceptance.neodymium.testclasses.repeat.classlevel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.repeat.RepeatOnFailure;

@RepeatOnFailure(3)
@RunWith(NeodymiumRunner.class)
public class ClassRepeatOnFailureTestdataCombinationTest
{
    int i = 0;

    int j = 0;

    @Test
    public void test()
    {
        i++;
        System.out.println(i);
        Assert.assertFalse("Produce test failure number " + i, i < 5);
    }

    @Test
    public void test1()
    {
        j++;
        System.out.println(j);
        Assert.assertFalse("Produce test failure number " + j, j < 3);
    }
}
