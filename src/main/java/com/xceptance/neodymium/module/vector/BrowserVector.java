package com.xceptance.neodymium.module.vector;

public class BrowserVector implements Vector
{
    private String browserTag;

    public BrowserVector(String browserTag)
    {
        this.browserTag = browserTag;
    }

    @Override
    public void beforeMethod()
    {
    }

    @Override
    public void afterMethod()
    {
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
