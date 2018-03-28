package com.xceptance.neodymium.module.vector;

import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.model.TestClass;

import com.xceptance.neodymium.MethodExecutionContext;

public abstract class RunnerVector
{
    protected List<Runner> runner = new LinkedList<>();

    public boolean init(TestClass testKlass, MethodExecutionContext methodExecutionContext)
    {
        throw new RuntimeException("Not implemented yet!");
    }

    public List<Runner> getRunnerList()
    {
        return runner;
    }
}
