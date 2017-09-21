package com.xceptance.xrunner;

import org.junit.Test;
import org.junit.experimental.categories.Category;

//@RunWith(XCRunner.class)
public class TestClass2
{
    @Test
    // @TestGroup(group = Fast.class, ordinal = 1)
    @Category(
        {
            Slow.class, Fast.class
        })
    public void fastNSlow()
    {
        System.out.println("fastNSlow");
    }

    @Test
    @Category(Fast.class)
    public void fast()
    {
        System.out.println("fast");
    }

    @Test
    @Category(Slow.class)
    public void slow()
    {
        System.out.println("slow");
    }

    @Test
    public void justATest()
    {
        System.out.println("justATest");
    }
}
