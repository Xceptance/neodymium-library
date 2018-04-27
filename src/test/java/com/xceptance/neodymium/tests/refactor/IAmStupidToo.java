package com.xceptance.neodymium.tests.refactor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumStatementRunner2;
import com.xceptance.neodymium.multibrowser.Browser;

//@RunOrder({BrowserVector.class, TestdataVector.class, PrarameterVector.class})
@RunWith(NeodymiumStatementRunner2.class)
@Browser(
    {
        "A", "B"
    })
public class IAmStupidToo
{
    @BeforeClass
    public static void beforeClass()
    {
        System.out.println("IAmStupidToo.beforeClass()");
    }

    @AfterClass
    public static void afterClass()
    {
        System.out.println("IAmStupidToo.afterClass()");
    }

    @Before
    public void before()
    {
        System.out.println("IAmStupidToo.before()");
    }

    @After
    public void after()
    {
        System.out.println("IAmStupidToo.after()");
    }

    @Test
    public void test7_EMPTY() throws Exception
    {
        System.out.println("bla");
    }
}
