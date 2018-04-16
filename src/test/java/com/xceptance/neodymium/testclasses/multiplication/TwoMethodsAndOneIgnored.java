package com.xceptance.neodymium.testclasses.multiplication;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class TwoMethodsAndOneIgnored
{
    @Test
    public void first()
    {

    }

    @Test
    public void second()
    {
    }

    @Ignore
    public void third()
    {

    }
}
