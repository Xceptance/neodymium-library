package com.xceptance.xrunner;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

//@Browser(
//    {
//        "Chrome_1024x768", "FF_1024x768"
//    })
//@RunWith(XCRunner.class)
//@RunWith(Categories.class)
// @Category(Integer.class)
// @Category(Object.class)
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

    @Parameters(name = "{index} - {0}, {1}")
    public static List<Object[]> getData()
    {
        List<Object[]> data = new LinkedList<>();

        for (int i = 97; i < 98; i++)
        {
            data.add(new Object[]
                {
                    Character.valueOf((char) i), i - 97
                });
        }

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
    @Category(Integer.class)
    public void testMethod0()
    {
        System.out.println("testMethod0: Parameter[" + parameter0 + ", " + parameter1 + "]");

        // Iterator<Entry<Object, Object>> sysprops = System.getProperties().entrySet().iterator();
        // while (sysprops.hasNext())
        // {
        // Entry<Object, Object> entry = sysprops.next();
        // System.out.println(entry.getKey() + " === " + entry.getValue());
        // }

        int a = 1;
        int b = 0;
        // int c = a / b;

        // System.out.println(c);
    }

    @Test
    public void testMethod1()
    {
        System.out.println("testMethod1: Parameter[" + parameter0 + ", " + parameter1 + "]");
        Assert.assertTrue(false);
    }

    @Test
    public void testMethod2()
    {
        System.out.println("testMethod1: Parameter[" + parameter0 + ", " + parameter1 + "]");
        // Assert.assertTrue(false);
    }
}
