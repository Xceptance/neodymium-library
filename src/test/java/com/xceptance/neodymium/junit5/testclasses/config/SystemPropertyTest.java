package com.xceptance.neodymium.junit5.testclasses.config;

import org.junit.jupiter.api.Assertions;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.junit5.NeodymiumTest;
import com.xceptance.neodymium.util.Neodymium;

@Browser("Chrome_1024x768")
public class SystemPropertyTest
{
    @NeodymiumTest
    public void testSystemProperty()
    {
        // Goto the home page
        Selenide.open("https://www.xceptance.com/en/");
        Assertions.assertEquals(Neodymium.configuration().getWindowHeight(), 1000);
    }
}
