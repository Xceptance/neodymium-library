package com.xceptance.neodymium.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_headless")
public class PlaygroundTest
{
    private static WebDriver webDriver1;

    private static WebDriver webDriver2;

    private static WebDriver webDriver3;

    private static boolean alive;

    @BeforeClass
    public static void beforeClass()
    {
        // Assert.assertNull(webDriver1);
        // Assert.assertNull(Neodymium.getDriver());
    }

    @SuppressBrowsers
    @Test
    public void test1()
    {
        // webDriver1 = Neodymium.getDriver();
        // Assert.assertEquals(webDriver1, Neodymium.getDriver());
        // NeodymiumWebDriverTest.assertWebDriverAlive(webDriver1);
        //
        // Selenide.open("https://www.xceptance.com/");
        Assert.assertNull(Neodymium.getDriver());
        alive = false;
    }

    @Test
    public void test2()
    {
        // webDriver1 = Neodymium.getDriver();
        // Assert.assertEquals(webDriver1, Neodymium.getDriver());
        NeodymiumWebDriverTest.assertWebDriverAlive(Neodymium.getDriver());

        Selenide.open("https://www.xceptance.com/");
        alive = true;
    }

    @After
    public void after()
    {
        // Assert.assertNotEquals(webDriver1, Neodymium.getDriver());
        // webDriver2 = Neodymium.getDriver();
        // Selenide.open("https://xtc.xceptance.com/");
        if (alive)
        {
            Selenide.open("https://xtc.xceptance.com/");
        }
        else
        {
            Assert.assertNull(Neodymium.getDriver());
        }
    }

    @After
    public void after1()
    {
        // Assert.assertNotEquals(webDriver2, Neodymium.getDriver());
        // webDriver3 = Neodymium.getDriver();
        if (alive)
        {
            Selenide.open("https://github.com/Xceptance/neodymium-library/pull/194/files");
        }
        else
        {
            Assert.assertNull(Neodymium.getDriver());
        }
    }

    @AfterClass
    public static void afterClass()
    {
        // NeodymiumWebDriverTest.assertWebDriverClosed(webDriver1);
        // NeodymiumWebDriverTest.assertWebDriverClosed(webDriver2);
        // NeodymiumWebDriverTest.assertWebDriverClosed(webDriver3);
    }
}
