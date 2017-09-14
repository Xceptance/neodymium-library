package com.xceptance.xrunner;

public class MethodExecutionContext
{
    private boolean runBeforeClass;

    private boolean runAfterClass;

    public boolean isRunBeforeClass()
    {
        return runBeforeClass;
    }

    public void setRunBeforeClass(boolean runBeforeClass)
    {
        this.runBeforeClass = runBeforeClass;
    }

    public boolean isRunAfterClass()
    {
        return runAfterClass;
    }

    public void setRunAfterClass(boolean runAfterClass)
    {
        this.runAfterClass = runAfterClass;
    }
}
