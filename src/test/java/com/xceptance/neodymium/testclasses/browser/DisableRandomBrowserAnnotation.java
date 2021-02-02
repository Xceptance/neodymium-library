package com.xceptance.neodymium.testclasses.browser;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.RandomBrowsers;

@RunWith(NeodymiumRunner.class)
public class DisableRandomBrowserAnnotation
{
    @RandomBrowsers(0)
    @Test
    public void test1()
    {

    }
}
