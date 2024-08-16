package com.xceptance.neodymium.junit4.testclasses.config;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.junit4.NeodymiumRunner;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_1024x768")
public class SystemPropertyTest
{
    @Test
    public void testSystemProperty()
    {
        // Goto the home page
        Selenide.open("https://www.xceptance.com/en/");
        Assertions.assertEquals(Neodymium.configuration().getWindowHeight(), 1000);
    }
}
