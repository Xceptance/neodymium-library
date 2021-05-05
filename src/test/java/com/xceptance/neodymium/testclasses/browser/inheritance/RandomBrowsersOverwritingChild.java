package com.xceptance.neodymium.testclasses.browser.inheritance;

import org.junit.Test;

import com.xceptance.neodymium.module.statement.browser.multibrowser.RandomBrowsers;

@RandomBrowsers(3)
public class RandomBrowsersOverwritingChild extends RandomBrowsersParent
{
    @Test
    public void test()
    {
    }
}
