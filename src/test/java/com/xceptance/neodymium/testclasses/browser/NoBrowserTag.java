package com.xceptance.neodymium.testclasses.browser;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser({})
public class NoBrowserTag
{
    @Test
    public void test()
    {

    }
}
