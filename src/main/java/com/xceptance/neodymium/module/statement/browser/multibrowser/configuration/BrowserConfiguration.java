package com.xceptance.neodymium.module.statement.browser.multibrowser.configuration;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * POJO class to hold browser configurations
 * 
 * @author m.kaufmann
 */
public class BrowserConfiguration
{
    private String browserTag;

    private String name;

    private DesiredCapabilities capabilities;

    private String testEnvironment;

    private int browserWidth;

    private int browserHeight;

    private boolean headless;

    public String getConfigTag()
    {
        return browserTag;
    }

    protected void setConfigTag(String configTag)
    {
        this.browserTag = configTag;
    }

    public String getName()
    {
        return name;
    }

    protected void setName(String name)
    {
        this.name = name;
    }

    public DesiredCapabilities getCapabilities()
    {
        return capabilities;
    }

    protected void setCapabilities(DesiredCapabilities capabilities)
    {
        this.capabilities = capabilities;
    }

    public String getTestEnvironment()
    {
        return testEnvironment;
    }

    protected void setTestEnvironment(String testEnvironment)
    {
        this.testEnvironment = testEnvironment;
    }

    public int getBrowserWidth()
    {
        return browserWidth;
    }

    protected void setBrowserWidth(int browserWidth)
    {
        this.browserWidth = browserWidth;
    }

    public int getBrowserHeight()
    {
        return browserHeight;
    }

    protected void setBrowserHeight(int browserHeight)
    {
        this.browserHeight = browserHeight;
    }

    public boolean isHeadless()
    {
        return headless;
    }

    public void setHeadless(boolean headless)
    {
        this.headless = headless;
    }
}
