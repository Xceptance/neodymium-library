package com.xceptance.neodymium.testclasses.browser.classonly;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;

@RunWith(NeodymiumRunner.class)
@Browser("chrome")
@SuppressBrowsers
public class ClassBrowserSuppressed
{
    @Test
    public void first() throws Exception
    {
    }
}
