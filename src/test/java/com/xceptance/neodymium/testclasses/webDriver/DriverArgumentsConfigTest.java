package com.xceptance.neodymium.testclasses.webDriver;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.tests.DriverArgumentsTest;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("FF_with_args")
@Browser("Chrome_with_args")
public class DriverArgumentsConfigTest
{
    @Test
    public void test()
    {
        Selenide.open("https://www.xceptance.com/en/");
        System.out.println(Neodymium.getBrowserName());
        if (Neodymium.getBrowserName().equals("chrome"))
        {
            Assert.assertTrue("No log file found for chrome", new File(DriverArgumentsTest.logFileNameChrome).exists());
        }
        else
        {
            Assert.assertTrue("No log file found for firefox", new File(DriverArgumentsTest.logFileNameFirefox).exists());
        }
    }

    @After
    public void cleanup()
    {
        if (Neodymium.getBrowserName().contains("Chrome"))
        {
            new File(DriverArgumentsTest.logFileNameChrome).delete();
        }
        else
        {
            new File(DriverArgumentsTest.logFileNameFirefox).delete();
        }
    }
}
