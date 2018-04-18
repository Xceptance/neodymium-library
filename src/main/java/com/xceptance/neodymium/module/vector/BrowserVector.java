package com.xceptance.neodymium.module.vector;

import java.text.MessageFormat;

public class BrowserVector implements RunVector
{
    private String browserTag;

    private int vectorHashCode;

    public BrowserVector(String browserTag, int vectorHashCode)
    {
        this.browserTag = browserTag;
        this.vectorHashCode = vectorHashCode;
        System.out.println(MessageFormat.format("{0} {1} {2}", vectorHashCode, hashCode(), browserTag));
    }

    @Override
    public String getTestName()
    {
        return MessageFormat.format("[Browser {0}]", browserTag);
    }

    @Override
    public void beforeMethod()
    {
        System.out.println("Setup web driver");
    }

    @Override
    public void afterMethod()
    {
        System.out.println("Teardown web driver");
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((browserTag == null) ? 0 : browserTag.hashCode());
        return result;
    }

    @Override
    public int vectorHashCode()
    {
        return vectorHashCode;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof BrowserVector))
        {
            return false;
        }
        BrowserVector other = (BrowserVector) obj;
        if (browserTag == null)
        {
            if (other.browserTag != null)
            {
                return false;
            }
        }
        else if (!browserTag.equals(other.browserTag))
        {
            return false;
        }
        return true;
    }
}
