package com.xceptance.neodymium.module.statement;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public abstract class StatementBuilder extends Statement
{
    public abstract List<Object> createIterationData(TestClass testClass, FrameworkMethod method) throws Throwable;

    public abstract StatementBuilder createStatement(Statement next, Object parameter);

    public abstract String getTestName(Object data);
}
