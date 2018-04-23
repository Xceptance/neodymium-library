package com.xceptance.neodymium.module.vector;

public interface RunVector
{
    public void beforeMethod();

    public void afterMethod();

    public String getTestName();

    public int vectorHashCode();

    public void setTestClassInstance(Object testClassInstance);
}
