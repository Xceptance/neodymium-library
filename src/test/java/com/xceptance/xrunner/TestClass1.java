package com.xceptance.xrunner;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

import com.xceptance.multibrowser.Browser;

@Browser(
    {
        "Chrome_1024x768", "FF_1024x768"
    })
@RunWith(XCRunner.class)
@UseParametersRunnerFactory(XCParameterRunnerFactory.class)
public class TestClass1
{
    @Parameter
    public Object parameter0;

    @Parameter(1)
    public Object parameter1;

    @Parameters(name = "{0}")
    public static List<Object[]> getData()
    {
        List<Object[]> data = new LinkedList<>();

        data.add(new Object[]
            {
                'a', 0
            });
        data.add(new Object[]
            {
                'b', 1
            });
        data.add(new Object[]
            {
                'c', 2
            });

        return data;
    }

    public TestClass1()
    {
        System.out.println("construct");
    }

    @Test
    public void test0()
    {
        System.out.println("test0: " + parameter0 + ", " + parameter1);
    }

    @Test
    public void test1()
    {
        System.out.println("test1: " + parameter0 + ", " + parameter1);
    }
}
