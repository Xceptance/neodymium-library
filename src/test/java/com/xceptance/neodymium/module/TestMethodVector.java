package com.xceptance.neodymium.module;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class TestMethodVector
{
    public void noTestMethod()
    {

    }

    @Test
    public void testmethod()
    {

    }

    @Ignore
    public void ignoredMethod()
    {

    }

    @Test
    @Ignore
    public void ignoredTestMethod()
    {

    }
}
