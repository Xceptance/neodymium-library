package com.xceptance.neodymium.module.statement.softassert;

import static com.codeborne.selenide.logevents.ErrorsCollector.LISTENER_SOFT_ASSERT;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import com.codeborne.selenide.logevents.ErrorsCollector;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.xceptance.neodymium.module.StatementBuilder;

public class SoftAssertStatement extends StatementBuilder
{
    // TODO refacture StatementBuilder to provide a smaller interface that does not create a special JUnit output folder
    // for SoftAssertions (could also be provides by passing null as Category or Test name)
    private Statement next;

    private Object testClassInstance;

    public SoftAssertStatement()
    {
    }

    public SoftAssertStatement(Statement next, Object testClassInstance)
    {
        this.next = next;
        this.testClassInstance = testClassInstance;
    }

    @Override
    public List<Object> createIterationData(TestClass testClass, FrameworkMethod method) throws Throwable
    {
        List<Object> list = new ArrayList<>();
        list.add("SoftAssertion");
        return list;
    }

    @Override
    public StatementBuilder createStatement(Object testClassInstance, Statement next, Object parameter)
    {
        return new SoftAssertStatement(next, testClassInstance);
    }

    @Override
    public String getTestName(Object data)
    {
        return (String) data;
    }

    @Override
    public String getCategoryName(Object data)
    {
        return (String) data;
    }

    @Override
    public void evaluate() throws Throwable
    {
        SelenideLogger.addListener(LISTENER_SOFT_ASSERT, new ErrorsCollector());
        try
        {
            next.evaluate();
        }
        catch (Throwable t)
        {
            throw t;
        }
        finally
        {
            ErrorsCollector errorsCollector = SelenideLogger.removeListener(LISTENER_SOFT_ASSERT);
            errorsCollector.failIfErrors(testClassInstance.getClass().getSimpleName());
        }
    }
}
