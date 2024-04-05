package com.xceptance.neodymium.module.statement.browser;

public class BrowserMethodData
{
    private String browserTag;

    private boolean keepBrowserOpen;

    private boolean keepBrowserOpenOnFailure;

    public BrowserMethodData(String browserTag, boolean keepBrowserOpen, boolean keepBrowserOpenOnFailure)
    {
        this.browserTag = browserTag;
        this.keepBrowserOpen = keepBrowserOpen;
        this.keepBrowserOpenOnFailure = keepBrowserOpenOnFailure;
    }

    public String getBrowserTag()
    {
        return browserTag;
    }

    public boolean isKeepBrowserOpen()
    {
        return keepBrowserOpen;
    }

    public boolean isKeepBrowserOpenOnFailure()
    {
        return keepBrowserOpenOnFailure;
    }
}
