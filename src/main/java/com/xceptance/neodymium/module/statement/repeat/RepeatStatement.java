package com.xceptance.neodymium.module.statement.repeat;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import com.xceptance.neodymium.module.StatementBuilder;

public class RepeatStatement extends StatementBuilder
{
    private Statement next;

    private int maxRepetitionNumber;

    public RepeatStatement(Statement next, Integer data)
    {
        this.next = next;
        this.maxRepetitionNumber = data;
    }

    public RepeatStatement()
    {
    }

    @Override
    public List<Object> createIterationData(TestClass testClass, FrameworkMethod method) throws Throwable
    {
        RepeatOnFailure classRepeatOnFailure = testClass.getAnnotation(RepeatOnFailure.class);
        List<RepeatOnFailure> methodRepeatOnFailures = getAnnotations(method.getMethod(), RepeatOnFailure.class);
        int numberOfRepetitions = 0;
        if (classRepeatOnFailure != null)
        {
            numberOfRepetitions = classRepeatOnFailure.value();

        }
        if (!methodRepeatOnFailures.isEmpty())
        {
            numberOfRepetitions = methodRepeatOnFailures.get(0).value();
        }
        return List.of(numberOfRepetitions);
    }

    @Override
    public StatementBuilder createStatement(Object testClassInstance, Statement next, Object parameter)
    {
        return new RepeatStatement(next, (Integer) parameter);
    }

    @Override
    public String getTestName(Object data)
    {
        return "Repeating test, max number " + data;
    }

    @Override
    public String getCategoryName(Object data)
    {
        return "Repeat on failure test";
    }

    @Override
    public void evaluate() throws Throwable
    {
        Throwable caughtThrowable = null;
        for (int i = 0; i < maxRepetitionNumber; i++)
        {
            try
            {
                next.evaluate();
                return;
            }
            catch (Throwable t)
            {
                caughtThrowable = t;
            }
        }
        throw caughtThrowable;
    }
}
