package com.xceptance.neodymium.testclasses.multiplication.browser;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("first_browser")
@Browser("second_browser")
public class TwoBrowserOneMethod
{
    @Test
    public void first()
    {
    }
}
