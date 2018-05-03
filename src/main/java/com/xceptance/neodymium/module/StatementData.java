package com.xceptance.neodymium.module;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;

public class StatementData
{
    private StatementBuilder statementBuilder;

    private List<Object> iterationData;

    private FrameworkMethod testMethod;

    public List<Object> getIterationData()
    {
        return iterationData;
    }

    public void setIterationData(List<Object> iterationData)
    {
        this.iterationData = iterationData;
    }

    public StatementBuilder getStatementBuilder()
    {
        return statementBuilder;
    }

    public void setStatementBuilder(StatementBuilder statementBuilder)
    {
        this.statementBuilder = statementBuilder;
    }

    public FrameworkMethod getTestMethod()
    {
        return testMethod;
    }

    public void setTestMethod(FrameworkMethod testMethod)
    {
        this.testMethod = testMethod;
    }
}
