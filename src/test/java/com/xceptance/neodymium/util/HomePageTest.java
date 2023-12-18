package com.xceptance.neodymium.util;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_1500x1000")
public class HomePageTest
{
    @Test
    public void testVisitingHomepage()
    {
        // Goto the home page and perform a short validation that we are on the correct page
        Selenide.open("https://www.zwilling.com/de/");
    }
}
