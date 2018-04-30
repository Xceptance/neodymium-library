package com.xceptance.neodymium.testclasses.multiplication.browser;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser(
    {
        "first_browser", "second_browser"
    })
public class TwoBrowserTwoMethods
{
    @Test
    public void first()
    {
    }

    @Test
    public void second()
    {
    }
}
