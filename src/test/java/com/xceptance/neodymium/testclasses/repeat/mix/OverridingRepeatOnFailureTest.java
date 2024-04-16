package com.xceptance.neodymium.testclasses.repeat.mix;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.repeat.RepeatOnFailure;

@RunWith(NeodymiumRunner.class)
public class OverridingRepeatOnFailureTest
{
    public static AtomicInteger val = new AtomicInteger(0);

    @Test
    @RepeatOnFailure(10)
    public void testVisitingHomepage()
    {
        int i = val.getAndIncrement();
        if (val.get() == 6)
        {
            val.set(0);
        }
        Assert.assertFalse("Produce test failure number " + i, i < 5);
    }
}
