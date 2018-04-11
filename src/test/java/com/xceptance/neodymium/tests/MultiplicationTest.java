package com.xceptance.neodymium.tests;

import org.junit.Test;

import com.xceptance.neodymium.testclasses.multiplication.TwoMethods;
import com.xceptance.neodymium.testclasses.multiplication.TwoMethodsAndOneIgnored;
import com.xceptance.neodymium.testclasses.multiplication.TwoMethodsOneIgnored;
import com.xceptance.neodymium.testclasses.multiplication.dataset.OneDataSetOneMethod;
import com.xceptance.neodymium.testclasses.multiplication.dataset.OneDataSetTwoMethods;
import com.xceptance.neodymium.testclasses.multiplication.dataset.TwoDataSetsOneMethod;
import com.xceptance.neodymium.testclasses.multiplication.dataset.TwoDataSetsTwoMethods;

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

    @Test
    public void testOneDataSetOneMethod() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: Data set 1 / 1"
            };
        checkDescription(OneDataSetOneMethod.class, expected);
    }

    @Test
    public void testOneDataSetTwoMethods() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: Data set 1 / 1", //
                "second :: Data set 1 / 1"
            };
        checkDescription(OneDataSetTwoMethods.class, expected);
    }

    @Test
    public void testTwoDataSetsOneMethod() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: Data set 1 / 2", //
                "first :: Data set 2 / 2"
            };
        checkDescription(TwoDataSetsOneMethod.class, expected);

    }

    @Test
    public void testTwoDataSetsTwoMethods() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: Data set 1 / 2", //
                "first :: Data set 2 / 2", //
                "second :: Data set 1 / 2", //
                "second :: Data set 2 / 2", //
            };
        checkDescription(TwoDataSetsTwoMethods.class, expected);

    }

}
