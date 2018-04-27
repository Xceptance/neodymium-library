package com.xceptance.neodymium.module.statement;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import com.xceptance.neodymium.multibrowser.Browser;

public class BrowserStatement extends StatementBuilder
{
    private Statement next;

    private String browserTag;

    public BrowserStatement()
    {
    }

    public BrowserStatement(Statement next, String parameter)
    {
        this.next = next;
        this.browserTag = parameter;
    }

    @Override
    public void evaluate() throws Throwable
    {
        System.out.println("setup browser: " + browserTag);
        next.evaluate();
        System.out.println("teardown browser: " + browserTag);
    }

    @Override
    public List<Object> createIterationData(TestClass testClass, FrameworkMethod method)
    {
        List<Object> iterations = new LinkedList<>();

        // prefer method annotation before class annotation
        Browser browserAnnotation = method.getAnnotation(Browser.class);
        if (browserAnnotation == null)
        {
            browserAnnotation = testClass.getAnnotation(Browser.class);
        }

        if (browserAnnotation != null)
        {
            for (String browserTag : browserAnnotation.value())
            {
                iterations.add(browserTag);

            }
        }

        return iterations;
    }

    @Override
    public StatementBuilder createStatement(Statement next, Object parameter)
    {
        return new BrowserStatement(next, (String) parameter);
    }

    @Override
    public String getTestName(Object data)
    {
        return MessageFormat.format("[Browser {0}]", (String) data);
    }

}
