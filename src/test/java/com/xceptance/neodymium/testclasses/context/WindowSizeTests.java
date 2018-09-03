package com.xceptance.neodymium.testclasses.context;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
public class WindowSizeTests
{
    @Test
    @Browser("chrome500")
    public void testIsMobile()
    {
        Assert.assertTrue("should be mobile", Neodymium.isMobile());
        Assert.assertFalse("shouldn't be tablet", Neodymium.isTablet());
        Assert.assertFalse("shouldn't be small desktop", Neodymium.isSmallDesktop());
        Assert.assertFalse("shouldn't be desktop", Neodymium.isDesktop());
        Assert.assertFalse("shouldn't be large desktop", Neodymium.isLargeDesktop());
        Assert.assertFalse("shouldn't be extra large desktop", Neodymium.isExtraLargeDesktop());
    }

    @Test
    @Browser("chrome544")
    public void testIsTablet()
    {
        Assert.assertFalse("shouldn't be mobile", Neodymium.isMobile());
        Assert.assertTrue("should be tablet", Neodymium.isTablet());
        Assert.assertFalse("shouldn't be small desktop", Neodymium.isSmallDesktop());
        Assert.assertFalse("shouldn't be desktop", Neodymium.isDesktop());
        Assert.assertFalse("shouldn't be large desktop", Neodymium.isLargeDesktop());
        Assert.assertFalse("shouldn't be extra large desktop", Neodymium.isExtraLargeDesktop());
    }

    @Test
    @Browser("chrome769")
    public void testIsSmallDesktop()
    {
        Assert.assertFalse("shouldn't be mobile", Neodymium.isMobile());
        Assert.assertFalse("shouldn't be tablet", Neodymium.isTablet());
        Assert.assertTrue("should be small desktop", Neodymium.isSmallDesktop());
        Assert.assertTrue("should be desktop", Neodymium.isDesktop());
        Assert.assertFalse("shouldn't be large desktop", Neodymium.isLargeDesktop());
        Assert.assertFalse("shouldn't be extra large desktop", Neodymium.isExtraLargeDesktop());
    }

    @Test
    @Browser("chrome992")
    public void testIsLargeDesktop()
    {
        Assert.assertFalse("shouldn't be mobile", Neodymium.isMobile());
        Assert.assertFalse("shouldn't be tablet", Neodymium.isTablet());
        Assert.assertFalse("shouldn't be small desktop", Neodymium.isSmallDesktop());
        Assert.assertTrue("should be desktop", Neodymium.isDesktop());
        Assert.assertTrue("should be large desktop", Neodymium.isLargeDesktop());
        Assert.assertFalse("shouldn't be extra large desktop", Neodymium.isExtraLargeDesktop());
    }

    @Test
    @Browser("chrome1200")
    public void testIsExtraLargeDesktop()
    {
        Assert.assertFalse("shouldn't be mobile", Neodymium.isMobile());
        Assert.assertFalse("shouldn't be tablet", Neodymium.isTablet());
        Assert.assertFalse("shouldn't be small desktop", Neodymium.isSmallDesktop());
        Assert.assertTrue("should be desktop", Neodymium.isDesktop());
        Assert.assertFalse("shouldn't  be large desktop", Neodymium.isLargeDesktop());
        Assert.assertTrue("should be extra large desktop", Neodymium.isExtraLargeDesktop());
    }
}
