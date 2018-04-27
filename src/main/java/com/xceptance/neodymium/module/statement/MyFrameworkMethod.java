package com.xceptance.neodymium.module.statement;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;

public class MyFrameworkMethod extends FrameworkMethod
{
    private List<Object> data = new LinkedList<>();

    private List<StatementBuilder> builder = new LinkedList<>();

    public MyFrameworkMethod(Method method)
    {
        super(method);
    }

    public List<Object> getData()
    {
        return data;
    }

    public void setData(List<Object> data)
    {
        this.data = data;
    }

    public List<StatementBuilder> getBuilder()
    {
        return builder;
    }

    public void setBuilder(List<StatementBuilder> builder)
    {
        this.builder = builder;
    }

    public String getTestName()
    {
        StringBuilder nameBuilder = new StringBuilder(250);
        nameBuilder.append(super.getName());
        for (int i = builder.size() - 1; i >= 0; i--)
        {
            StatementBuilder statementBuilder = builder.get(i);
            nameBuilder.append(" :: ");
            nameBuilder.append(statementBuilder.getTestName());
        }

        return nameBuilder.toString();
    }
}
