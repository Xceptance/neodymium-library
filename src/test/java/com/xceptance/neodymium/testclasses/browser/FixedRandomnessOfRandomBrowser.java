package com.xceptance.neodymium.testclasses.browser;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.RandomBrowsers;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;
import com.xceptance.neodymium.util.Neodymium;

@Browser("Chrome_1024x768")
@Browser("Chrome_1500x1000")
@Browser("FF_1024x768")
@Browser("FF_1500x1000")
@RandomBrowsers(2)
@RunWith(NeodymiumRunner.class)
public class FixedRandomnessOfRandomBrowser
{
    private static ArrayList<String> browsers = new ArrayList<>();

    @Test
    public void test1()
    {
        browsers.add(Neodymium.getBrowserProfileName());
    }

    @Test
    @SuppressBrowsers
    public void test2()
    {
        Assert.assertEquals(2, browsers.size());
        Assert.assertEquals("Chrome_1500x100", browsers.get(0));
        Assert.assertEquals("FF_1500x1000", browsers.get(1));
    }
}
