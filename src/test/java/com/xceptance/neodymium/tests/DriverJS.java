package com.xceptance.neodymium.tests;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
public class DriverJS
{
    @Test
    @Browser("Chrome_1024x768")
    public void test1() throws Exception
    {
        Selenide.open("https://blog.xceptance.com/");
        Selenide.$("#text-3").exists();
    }
}
