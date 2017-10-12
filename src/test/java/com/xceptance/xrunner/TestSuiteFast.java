package com.xceptance.xrunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
// @RunWith(XCRunner.class)
@SuiteClasses(
    {
        TestClass2.class
    })
// @ExcludeCategory(value =
// {
// Slow.class
// })
// @IncludeCategory(value =
// {
// Fast.class
// })
public class TestSuiteFast
{
}
