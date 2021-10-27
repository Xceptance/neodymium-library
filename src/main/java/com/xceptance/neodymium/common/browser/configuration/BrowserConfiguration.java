package com.xceptance.neodymium.common.browser.configuration;

import java.util.List;

import org.openqa.selenium.MutableCapabilities;

/**
 * POJO class to hold browser configurations
 * 
 * @author m.kaufmann
 */
public class BrowserConfiguration
{
    private String browserTag;

    private String name;

    private MutableCapabilities capabilities;

    private String testEnvironment;

    private int browserWidth;

    private int browserHeight;

    private boolean headless;

    private List<String> arguments;

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

    public MutableCapabilities getCapabilities()
    {
        return capabilities;
    }

    protected void setCapabilities(MutableCapabilities capabilities)
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

    public List<String> getArguments()
    {
        return arguments;
    }

    public void setArguments(List<String> arguments)
    {
        this.arguments = arguments;
    }
}
