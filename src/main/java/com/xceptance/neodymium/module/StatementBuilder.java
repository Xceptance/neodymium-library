package com.xceptance.neodymium.module;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

public abstract class StatementBuilder extends Statement
{
    public abstract List<Object> createIterationData(TestClass testClass, FrameworkMethod method) throws Throwable;

    public abstract StatementBuilder createStatement(Object testClassInstance, Statement next, Object parameter);

    public abstract String getTestName(Object data);

    public abstract String getCategoryName(Object data);

    public static <T extends StatementBuilder> T instantiate(Class<T> clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
