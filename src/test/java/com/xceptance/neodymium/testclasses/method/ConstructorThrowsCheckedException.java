package com.xceptance.neodymium.testclasses.method;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class ConstructorThrowsCheckedException
{
    public ConstructorThrowsCheckedException() throws Throwable
    {
        throw new Exception("construction failed");
    }

    @Test
    public void test()
    {

    }
}
