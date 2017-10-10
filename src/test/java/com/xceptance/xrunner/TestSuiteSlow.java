package com.xceptance.xrunner;

import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

//@RunWith(Categories.class)
@RunWith(NeodymiumRunner.class)
@SuiteClasses(
    {
        TestClass2.class
    })
@IncludeCategory(matchAny = true, value = Slow.class)
public class TestSuiteSlow
{
    static
    {
        System.out.println("TestSuiteSlow");
    }
}
