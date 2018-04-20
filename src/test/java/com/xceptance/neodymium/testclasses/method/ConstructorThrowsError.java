package com.xceptance.neodymium.testclasses.method;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class ConstructorThrowsError
{
    public ConstructorThrowsError() throws Exception
    {
        throw new Error();
    }

    @Test
    public void test()
    {

    }
}
