package com.xceptance.neodymium.testclasses.browser.methodonly;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowser;

@RunWith(NeodymiumRunner.class)
public class MethodBrowserSuppressNoBrowserAnnotation
{
    @Test
    @SuppressBrowser
    public void first() throws Exception
    {
    }
}
