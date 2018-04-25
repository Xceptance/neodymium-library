package com.xceptance.neodymium.module.vector;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public interface RunVectorBuilder
{
    public void create(TestClass testClass, FrameworkMethod frameworkMethod) throws Throwable;

    public List<RunVector> buildRunVectors() throws Throwable;
}
