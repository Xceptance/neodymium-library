package com.xceptance.neodymium.tests;

import org.junit.Test;

import com.xceptance.neodymium.testclasses.multiplication.TwoMethods;
import com.xceptance.neodymium.testclasses.multiplication.TwoMethodsAndOneIgnored;
import com.xceptance.neodymium.testclasses.multiplication.TwoMethodsOneIgnored;

public class MultiplicationTest extends NeodymiumTest
{
    @Test
    public void testTwoMethods() throws Throwable
    {
        String[] expected = new String[]
            {
                "first", "second"
            };
        checkDescription(TwoMethods.class, expected);
    }

    @Test
    public void testTwoMethodsAndOneIgnored() throws Throwable
    {
        String[] expected = new String[]
            {
                "first", "second"
            };
        checkDescription(TwoMethodsAndOneIgnored.class, expected);
    }

    @Test
    public void testTwoMethodsOneIgnored() throws Throwable
    {
        String[] expected = new String[]
            {
                "first", "second"
            };
        checkDescription(TwoMethodsOneIgnored.class, expected);
    }

}
