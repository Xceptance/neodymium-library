package com.xceptance.neodymium.testclasses.browser.methodonly;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;

@RunWith(NeodymiumRunner.class)
public class OneBrowserOneMethodBrowserSuppressed
{
    @Test
    @Browser("chrome")
    @SuppressBrowsers
    public void first() throws Exception
    {
    }
}
