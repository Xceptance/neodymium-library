package com.xceptance.neodymium.testclasses.browser.mixed;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@Browser("chrome")
@RunWith(NeodymiumRunner.class)
public class ClassAndMethodSameBrowserOneMethod
{
    @Browser("chrome")
    @Test
    public void first() throws Exception
    {
    }
}
