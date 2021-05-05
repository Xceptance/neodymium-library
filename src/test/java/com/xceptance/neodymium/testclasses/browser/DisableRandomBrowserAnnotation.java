package com.xceptance.neodymium.testclasses.browser;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.RandomBrowsers;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_1024x768")
@Browser("Chrome_1500x1000")
@RandomBrowsers(2)
public class DisableRandomBrowserAnnotation
{
    @RandomBrowsers(0)
    @Test
    public void test1()
    {
    }
}
