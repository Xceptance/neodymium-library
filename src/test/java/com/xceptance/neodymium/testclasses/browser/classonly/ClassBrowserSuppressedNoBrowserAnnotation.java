package com.xceptance.neodymium.testclasses.browser.classonly;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;

@SuppressBrowsers
@RunWith(NeodymiumRunner.class)
public class ClassBrowserSuppressedNoBrowserAnnotation
{
    @Test
    public void first() throws Exception
    {
    }
}
