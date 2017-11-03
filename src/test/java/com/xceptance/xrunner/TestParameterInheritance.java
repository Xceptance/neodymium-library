package com.xceptance.xrunner;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.TestData;

@RunWith(NeodymiumRunner.class)
// @Browser({"Chrome_1024x768", "FF_1024x768"})
// @Browser({"Chrome_1024x768"})
public class TestParameterInheritance extends TestParameterBase
{
    @TestData
    public Map<String, String> data = new HashMap<String, String>();

    @Test
    public void testMethod0()
    {
        System.out.println("d0: " + data.get("d0"));
        System.out.println("d1: " + data.get("d1"));
        System.out.println("d2: " + data.get("d2"));
    }

    @Test
    public void t0()
    {

    }
}
