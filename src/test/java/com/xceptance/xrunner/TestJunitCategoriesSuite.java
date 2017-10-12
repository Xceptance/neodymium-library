package com.xceptance.xrunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses(
    {
        TestClass1.class
    })
@RunWith(Categories.class)
public class TestJunitCategoriesSuite
{

}
