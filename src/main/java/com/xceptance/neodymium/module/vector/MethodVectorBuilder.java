package com.xceptance.neodymium.module.vector;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public class MethodVectorBuilder implements RunVectorBuilder
{
    private List<RunVector> runner = new LinkedList<>();

    @Override
    public void create(TestClass testClass, FrameworkMethod frameworkMethod)
    {
        List<FrameworkMethod> beforeMethodMethods = testClass.getAnnotatedMethods(Before.class);
        List<FrameworkMethod> afterMethodMethods = testClass.getAnnotatedMethods(After.class);
        runner.add(new MethodVector(frameworkMethod, beforeMethodMethods, afterMethodMethods, frameworkMethod.hashCode()));
    }

    @Override
    public List<RunVector> buildRunVectors()
    {
        return runner;
    }

}
