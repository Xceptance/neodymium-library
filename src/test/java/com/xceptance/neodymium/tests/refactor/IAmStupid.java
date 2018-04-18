package com.xceptance.neodymium.tests.refactor;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.multibrowser.Browser;

//@RunOrder({BrowserVector.class, TestdataVector.class, PrarameterVector.class})
@RunWith(NeodymiumRunner.class)
@Browser(
    {
        "A", "B"
    })
public class IAmStupid
{
    @Test
    public void test1_AB() throws Exception
    {

    }

    @Test
    public void test2_AB() throws Exception
    {

    }

    @Test
    @Browser("A")
    public void test3_A() throws Exception
    {

    }

    @Test
    @Browser("B")
    public void test4_B() throws Exception
    {

    }
}
