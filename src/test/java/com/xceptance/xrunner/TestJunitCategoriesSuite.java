package com.xceptance.xrunner;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses(
    {
        TestJunitCategories.class
    })
@RunWith(Categories.class)
@IncludeCategory(
    {
        Number.class
    })
public class TestJunitCategoriesSuite
{

}
