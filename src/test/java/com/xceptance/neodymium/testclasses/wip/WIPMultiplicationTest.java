package com.xceptance.neodymium.testclasses.wip;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.WIP;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@Browser("Chrome_headless")
@Browser("Chrome_1500x1000_headless")
@RunWith(NeodymiumRunner.class)
public class WIPMultiplicationTest
{
    @WIP
    @Test
    public void first()
    {
    }

    @Test
    public void second()
    {
    }
}
