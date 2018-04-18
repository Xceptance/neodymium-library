package com.xceptance.neodymium.module.vector;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public interface RunVectorBuilder
{
    public void create(TestClass testClass, FrameworkMethod frameworkMethod);

    public List<RunVector> buildRunVectors();
}
