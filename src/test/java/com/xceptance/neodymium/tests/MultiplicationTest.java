package com.xceptance.neodymium.tests;

import org.junit.Test;

import com.xceptance.neodymium.testclasses.multiplication.TwoMethods;
import com.xceptance.neodymium.testclasses.multiplication.TwoMethodsAndOneIgnored;
import com.xceptance.neodymium.testclasses.multiplication.TwoMethodsOneIgnored;
import com.xceptance.neodymium.testclasses.multiplication.dataset.OneDataSetOneMethod;
import com.xceptance.neodymium.testclasses.multiplication.dataset.OneDataSetTwoMethods;
import com.xceptance.neodymium.testclasses.multiplication.dataset.TwoDataSetsOneMethod;
import com.xceptance.neodymium.testclasses.multiplication.dataset.TwoDataSetsTwoMethods;
import com.xceptance.neodymium.testclasses.multiplication.parameter.OneParameterSetOneMethod;
import com.xceptance.neodymium.testclasses.multiplication.parameter.OneParameterSetTwoMethods;
import com.xceptance.neodymium.testclasses.multiplication.parameter.TwoParameterSetsOneMethod;
import com.xceptance.neodymium.testclasses.multiplication.parameter.TwoParameterSetsTwoMethods;
import com.xceptance.neodymium.testclasses.multiplication.pkgdata.PackageDataDoNotAffectMethodMultiplication;

public class MultiplicationTest extends NeodymiumTest
{
    //////////////////
    // Methods only //
    //////////////////

    @Test
    public void testTwoMethods() throws Throwable
    {
        // two test methods, no data
        String[] expected = new String[]
            {
                "first", "second"
            };
        checkDescription(TwoMethods.class, expected);
    }

    @Test
    public void testTwoMethodsAndOneIgnored() throws Throwable
    {
        // two test methods and one ignored, no data
        String[] expected = new String[]
            {
                "first", "second"
            };
        checkDescription(TwoMethodsAndOneIgnored.class, expected);
    }

    @Test
    public void testTwoMethodsOneIgnored() throws Throwable
    {
        // two methods one of them is ignored, no data
        String[] expected = new String[]
            {
                "first", "second"
            };
        checkDescription(TwoMethodsOneIgnored.class, expected);
    }

    //////////////////////////////
    // Methods and package data //
    //////////////////////////////

    @Test
    public void testPackageDataDoNotAffectMethodMultiplication() throws Throwable
    {
        // two methods, package test data do not affect multiplication
        String[] expected = new String[]
            {
                "first", "second"
            };
        checkDescription(PackageDataDoNotAffectMethodMultiplication.class, expected);

    }

    ///////////////////////////
    // Methods and data sets //
    ///////////////////////////

    @Test
    public void testOneDataSetOneMethod() throws Throwable
    {
        // one test method, one data set
        String[] expected = new String[]
            {
                "first :: Data set 1 / 1"
            };
        checkDescription(OneDataSetOneMethod.class, expected);
    }

    @Test
    public void testOneDataSetTwoMethods() throws Throwable
    {
        // two test methods, one data set
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
        // one test method but two data sets
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
        // two methods, two data sets
        String[] expected = new String[]
            {
                "first :: Data set 1 / 2", //
                "first :: Data set 2 / 2", //
                "second :: Data set 1 / 2", //
                "second :: Data set 2 / 2", //
            };
        checkDescription(TwoDataSetsTwoMethods.class, expected);
    }

    ///////////////////////////
    // Methods and parameter //
    ///////////////////////////

    @Test
    public void testOneParameterSetOneMethod() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: [0]"
            };
        checkDescription(OneParameterSetOneMethod.class, expected);
    }

    @Test
    public void testOneParameterSetTwoMethods() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: [0]", //
                "second :: [0]"
            };
        checkDescription(OneParameterSetTwoMethods.class, expected);
    }

    @Test
    public void testTwoParameterSetsOneMethod() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: [0]", //
                "first :: [1]"
            };
        checkDescription(TwoParameterSetsOneMethod.class, expected);
    }

    @Test
    public void testTwoParameterSetsTwoMethods() throws Throwable
    {
        String[] expected = new String[]
            {
                "first :: [0]", //
                "first :: [1]", //
                "second :: [0]", //
                "second :: [1]"
            };
        checkDescription(TwoParameterSetsTwoMethods.class, expected);
    }

}
