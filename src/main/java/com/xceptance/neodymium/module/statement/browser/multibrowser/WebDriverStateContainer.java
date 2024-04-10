package com.xceptance.neodymium.module.statement.browser.multibrowser;

import org.openqa.selenium.WebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import com.browserup.bup.BrowserUpProxy;

public class WebDriverStateContainer
{
    private int usedCount = 0;

    private WebDriver webDriver;

    private BrowserUpProxy proxy;

    private BrowserWebDriverContainer testcontainer;

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

    public BrowserWebDriverContainer getTestcontainer()
    {
        return testcontainer;
    }

    public void setTestcontainer(BrowserWebDriverContainer testcontainer)
    {
        this.testcontainer = testcontainer;
    }
}
