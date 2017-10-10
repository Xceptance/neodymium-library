package com.xceptance.xrunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.xceptance.neodymium.NeodymiumRunner;

//@RunWith(Categories.class)
@RunWith(NeodymiumRunner.class)
@SuiteClasses(
    {
        TestClass1.class
    })
// @ExcludeCategory(matchAny = true, value = Slow.class)
// @IncludeCategory(matchAny = true, value = DefaultGroup.class)
public class TestSuite
{
}
