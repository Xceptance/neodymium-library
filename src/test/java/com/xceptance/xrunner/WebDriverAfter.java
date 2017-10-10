package com.xceptance.xrunner;

import com.codeborne.selenide.Selenide;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.multibrowser.Browser;
import com.xceptance.neodymium.NeodymiumRunner;

@Browser(
    {
        "Chrome_1024x768"
    })
@RunWith(NeodymiumRunner.class)
public class WebDriverAfter
{
    @Test
    public void doStuff()
    {
        // WebDriver webDriver = WebDriverRunner.getWebDriver();
        // webDriver.navigate().to("http://google.de");
        // System.out.println("doStuff: " + webDriver.getTitle());
        Selenide.open("http://google.de");
        System.out.println("doStuff: " + Selenide.title());
    }

    @After
    public void cleanStuff()
    {
        // WebDriver webDriver = WebDriverRunner.getWebDriver();
        // webDriver.navigate().to("http://heise.de");
        Selenide.open("http://heise.de");
        System.out.println("cleanStuff: " + Selenide.title());
    }
}
