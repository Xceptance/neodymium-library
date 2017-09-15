package com.xceptance.xrunner;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

//@Browser(
//    {
//        "Chrome_1024x768", "FF_1024x768"
//    })
@RunWith(XCRunner.class)
// @RunWith(Parameterized.class)
// @UseParametersRunnerFactory(XCParameterRunnerFactory.class)
public class TestClass1
{
    @Parameter
    public Object parameter0;

    @Parameter(1)
    public Object parameter1;

    public Object initMemberVariable = initMemberVariable();

    static
    {
        System.out.println("static initialization");
    }

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
        // data.add(new Object[]
        // {
        // 'c', 2
        // });

        return data;
    }

    private Object initMemberVariable()
    {
        System.out.println("initMemberVariable");
        return null;
    }

    public TestClass1()
    {
        System.out.println("construct");
    }

    @BeforeClass
    public static void beforeClass()
    {
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass()
    {
        System.out.println("afterClass\n");
    }

    @Before
    public void beforeMethod()
    {
        System.out.println("beforeMethod");
    }

    @After
    public void afterMethod()
    {
        System.out.println("afterMethod");
    }

    @Test
    public void testMethod0()
    {
        System.out.println("testMethod0: Parameter[" + parameter0 + ", " + parameter1 + "]");

        int a = 1;
        int b = 0;
        int c = a / b;

        System.out.println(c);
    }

    @Test
    public void testMethod1()
    {
        System.out.println("testMethod1: Parameter[" + parameter0 + ", " + parameter1 + "]");
        Assert.assertTrue(false);
    }
}
