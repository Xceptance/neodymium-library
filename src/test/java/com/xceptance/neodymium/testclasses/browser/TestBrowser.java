package com.xceptance.neodymium.testclasses.browser;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser(
    {
        "test"
    })
public class TestBrowser
{
    @Test
    public void test()
    {

    }
}
