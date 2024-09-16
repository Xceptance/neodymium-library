package com.xceptance.neodymium.junit5.tests;

import org.junit.jupiter.api.AfterEach;

import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.junit5.NeodymiumTest;

@Browser("Chrome_headless")
public class Playground
{
    @NeodymiumTest
    public void test()
    {
        System.out.println("Running test body");
    }

    @AfterEach
    public void after1()
    {
        System.out.println("after1");
    }

    @AfterEach
    public void after2()
    {
        System.out.println("after2");
    }
}
