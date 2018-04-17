package com.xceptance.neodymium.tests;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DeleteMeTest
{
    @Parameters
    public static List<Object[]> createData()
    {
        List<Object[]> iterations = new LinkedList<>();
        iterations.add(new Object[]
            {
                1
            });
        // iterations.add(new Object[]
        // {
        // 2
        // });

        return iterations;
    }

    @Parameter
    public int anInteger;

    @Test
    public void test1()
    {
        System.out.println("DeleteMeTest.test1()");
    }

    @Test
    public void test2() throws Exception
    {
        System.out.println("DeleteMeTest.test2()");
    }

    @BeforeClass
    public static void beforeClass1()
    {
        System.out.println("DeleteMeTest.beforeClass1()");
    }

    @BeforeClass
    public static void beforeClass2()
    {
        System.out.println("DeleteMeTest.beforeClass2()");
    }

    @Before
    public void before1()
    {
        System.out.println("DeleteMeTest.before1()");
    }

    @Before
    public void before2()
    {
        System.out.println("DeleteMeTest.before2()");
    }

    @After
    public void after1()
    {
        System.out.println("DeleteMeTest.after1()");
    }

    @After
    public void after2()
    {
        System.out.println("DeleteMeTest.after2()");
    }

    @AfterClass
    public static void afterClass1()
    {
        System.out.println("DeleteMeTest.afterClass1()");
    }

    @AfterClass
    public static void afterClass2()
    {
        System.out.println("DeleteMeTest.afterClass2()");
    }

    public static void sleep(int timeMs)
    {
        try
        {
            Thread.sleep(timeMs);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
