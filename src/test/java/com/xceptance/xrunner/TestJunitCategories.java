package com.xceptance.xrunner;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
// @RunWith(Categories.class)
// @Category(Double.class)
public class TestJunitCategories
{
    public TestJunitCategories()
    {
        System.out.println(this.getClass().getName() + " - constructor");
    }

    @Test
    @Category(Number.class)
    public void test0()
    {
        System.out.println(this.getClass().getName() + " - test0");
    }

    @Test
    @Category(Long.class)
    public void test1()
    {
        System.out.println(this.getClass().getName() + " - test1");
    }

    @Test
    @Category(Double.class)
    public void test2()
    {
        System.out.println(this.getClass().getName() + " - test2");
    }
}
