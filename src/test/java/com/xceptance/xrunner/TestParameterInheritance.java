package com.xceptance.xrunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
public class TestParameterInheritance extends TestParameterBase
{

    @Test
    public void testMethod0()
    {
        System.out.println("d0: " + data.get("d0"));
        System.out.println("d1: " + data.get("d1"));
        System.out.println("d2: " + data.get("d2"));
    }

    @Test
    public void testMethod1()
    {
    }

    @Test
    public void testMethod2()
    {
    }

    @Test
    public void testMethod3()
    {
    }

    @Test
    public void testMethod4()
    {
    }

    @Test
    public void testMethod5()
    {
    }

    @Test
    public void testMethod6()
    {
    }

    @Test
    public void testMethod7()
    {
    }

    @Test
    public void testMethod8()
    {
    }

}
