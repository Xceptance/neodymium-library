package com.xceptance.neodymium.testclasses.multiplication.browser;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("first_browser")
public class OneBrowserOneMethod
{
    @Test
    public void first()
    {
    }
}
