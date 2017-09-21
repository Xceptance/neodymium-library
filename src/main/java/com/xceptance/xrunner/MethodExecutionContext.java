package com.xceptance.xrunner;

import org.junit.runner.Description;

public class MethodExecutionContext
{
    private boolean runBeforeClass;

    private boolean runAfterClass;

    private Description runnerDescription;

    private Object testClassInstance;

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

    public Description getRunnerDescription()
    {
        return runnerDescription;
    }

    public void setRunnerDescription(Description runnerDescription)
    {
        this.runnerDescription = runnerDescription;
    }

    public Object getTestClassInstance()
    {
        return testClassInstance;
    }

    public void setTestClassInstance(Object testClassInstance)
    {
        this.testClassInstance = testClassInstance;
    }
}
