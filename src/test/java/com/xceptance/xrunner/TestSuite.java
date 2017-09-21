package com.xceptance.xrunner;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@SuiteClasses(
    {
        TestClass1.class
    })
@ExcludeCategory(matchAny = true, value = Slow.class)
// @IncludeCategory(matchAny = true, value = Slow.class)
public class TestSuite
{
    static
    {
        System.out.println("TestSuite");
    }
}
