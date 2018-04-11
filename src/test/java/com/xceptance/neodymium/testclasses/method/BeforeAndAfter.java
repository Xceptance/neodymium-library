package com.xceptance.neodymium.testclasses.method;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BeforeAndAfter
{
    public static List<String> TRACE = new LinkedList<>();

    @BeforeClass
    public static void beforeClass()
    {
        TRACE.add("beforeClass");
    }

    @Before
    public void beforeMethod()
    {
        TRACE.add("beforeMethod");
    }

    @After
    public void afterMethod()
    {
        TRACE.add("afterMethod");
    }

    @AfterClass
    public static void afterClass()
    {
        TRACE.add("afterClass");
    }

    @Test
    public void first()
    {
        TRACE.add("first");
    }
}
