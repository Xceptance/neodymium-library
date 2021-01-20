package com.xceptance.neodymium.testclasses.browser.mixed;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.RandomBrowser;

@Browser("Chrome_1024x768")
@Browser("Chrome_1500x1000")
@Browser("Firefox_1024x768")
@Browser("Firefox_1500x1000")
@RandomBrowser(2)
@RunWith(NeodymiumRunner.class)

public class MethodBrowserAnnotationOverwritesClassRandomBrowser
{
    @Browser("Chrome_1500x1000")
    @Browser("Firefox_1024x768")
    @Browser("Firefox_1500x1000")
    @Test
    public void test1()
    {
    }
}
