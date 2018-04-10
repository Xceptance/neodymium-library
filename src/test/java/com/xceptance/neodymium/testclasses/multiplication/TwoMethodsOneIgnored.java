package com.xceptance.neodymium.testclasses.multiplication;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class TwoMethodsOneIgnored
{
    @Test
    public void first()
    {

    }

    @Test
    @Ignore
    public void second()
    {
    }
}
