package com.xceptance.xrunner;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;

import com.xceptance.multibrowser.Browser;

@Browser(
    {
        "Chrome_1024x768", "FF_1024x768"
    })
@RunWith(XCRunner.class)
public class TestClass1
{
    @Parameter
    public Object parameter0;

    // @Parameters(name = "{0}")
    public static List<Object[]> getData()
    {
        List<Object[]> data = new LinkedList<>();

        data.add(new Object[]
            {
                'a'
            });

        data.add(new Object[]
            {
                'b'
            });
        data.add(new Object[]
            {
                'c'
            });

        return data;
    }

    @Test
    public void test0()
    {
        System.out.println("test0: " + parameter0);
    }

    @Test
    public void test1()
    {
        System.out.println("test1: " + parameter0);
    }
}
