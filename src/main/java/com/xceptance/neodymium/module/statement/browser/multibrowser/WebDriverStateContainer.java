package com.xceptance.neodymium.module.statement.browser.multibrowser;

import org.openqa.selenium.WebDriver;

import com.browserup.bup.BrowserUpProxy;

public class WebDriverStateContainer
{
    private int usedCount = 0;

    private WebDriver webDriver;

    private BrowserUpProxy proxy;

    public WebDriver getWebDriver()
    {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver)
    {
        this.webDriver = webDriver;
    }

    public void setProxy(BrowserUpProxy proxy)
    {
        this.proxy = proxy;
    }

    public BrowserUpProxy getProxy()
    {
        return proxy;
    }

    public int getUsedCount()
    {
        return usedCount;
    }

    public void incrementUsedCount()
    {
        usedCount++;
    }
}
