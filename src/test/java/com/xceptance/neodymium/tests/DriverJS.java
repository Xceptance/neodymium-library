package com.xceptance.neodymium.tests;

import static com.codeborne.selenide.Selenide.*;

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
        // $x("//*[@id=\"text-3\"]").is(visible);
        $(".entry-title").exists();
        // $(".entry-title").exists();
        // Selenide.$(".menu-toggle").exists();
        // $("head > title").exists();
    }
}
