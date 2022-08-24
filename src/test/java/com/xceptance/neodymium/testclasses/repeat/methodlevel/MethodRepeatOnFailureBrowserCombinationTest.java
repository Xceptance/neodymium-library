package com.xceptance.neodymium.testclasses.repeat.methodlevel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.repeat.RepeatOnFailure;

@Browser("Chrome_headless")
@Browser("Chrome_1500x1000_headless")
@RunWith(NeodymiumRunner.class)
public class MethodRepeatOnFailureBrowserCombinationTest
{
    int i = 0;

    int j = 0;

    @Test
    @RepeatOnFailure(3)
    public void test()
    {
        i++;
        System.out.println(i);
        Assert.assertFalse("Produce test failure number " + i, i < 5);
    }

    @Test
    @RepeatOnFailure(3)
    public void test1()
    {
        j++;
        System.out.println(j);
        Assert.assertFalse("Produce test failure number " + j, j < 3);
    }
}
