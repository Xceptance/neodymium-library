package com.xceptance.multibrowser.configuration;

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

    public String getConfigTag()
    {
        return browserTag;
    }

    public void setConfigTag(String configTag)
    {
        this.browserTag = configTag;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public DesiredCapabilities getCapabilities()
    {
        return capabilities;
    }

    public void setCapabilities(DesiredCapabilities capabilities)
    {
        this.capabilities = capabilities;
    }

    public String getTestEnvironment()
    {
        return testEnvironment;
    }

    public void setTestEnvironment(String testEnvironment)
    {
        this.testEnvironment = testEnvironment;
    }

    public int getBrowserWidth()
    {
        return browserWidth;
    }

    public void setBrowserWidth(int browserWidth)
    {
        this.browserWidth = browserWidth;
    }

    public int getBrowserHeight()
    {
        return browserHeight;
    }

    public void setBrowserHeight(int browserHeight)
    {
        this.browserHeight = browserHeight;
    }
}
