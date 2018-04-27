package com.xceptance.neodymium;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.xceptance.neodymium.module.order.DefaultStatementRunOrder;
import com.xceptance.neodymium.module.statement.MyFrameworkMethod;
import com.xceptance.neodymium.module.statement.StatementBuilder;

public class NeodymiumStatementRunner2 extends BlockJUnit4ClassRunner
{
    public NeodymiumStatementRunner2(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    Map<MyFrameworkMethod, Statement> methodStatements;

    List<FrameworkMethod> computedTestMethods;

    @Override
    protected Statement methodBlock(FrameworkMethod method)
    {
        Statement methodStatement = super.methodBlock(method);

        if (method instanceof MyFrameworkMethod)
        {
            MyFrameworkMethod m = (MyFrameworkMethod) method;
            for (int i = m.getBuilder().size() - 1; i >= 0; i--)
            {
                StatementBuilder statementBuilder = m.getBuilder().get(i);
                Object data = m.getData().get(i);
                methodStatement = statementBuilder.createStatement(methodStatement, data);
            }
        }

        return methodStatement;

    }

    @Override
    protected List<FrameworkMethod> computeTestMethods()
    {
        // since this method is called at least two times and is somewhat expensive, we cache the result
        if (computedTestMethods != null)
            return computedTestMethods;

        List<FrameworkMethod> testMethods = new LinkedList<>();

        List<Class<? extends StatementBuilder>> statementRunOrder = new DefaultStatementRunOrder().getRunOrder();
        for (FrameworkMethod testAnnotatedMethod : getTestClass().getAnnotatedMethods(Test.class))
        {
            List<StatementBuilder> builderList = new LinkedList<>();
            List<List<Object>> builderDataList = new LinkedList<>();

            // iterate over statements defined in the order
            for (Class<? extends StatementBuilder> statementClass : statementRunOrder)
            {
                // ask each statement builder if this method should be processed
                // results in a list of parameters for this statement for method multiplication
                // e.g. for @Browser({"A", "B"}) the data list will contain "A" and "B"
                StatementBuilder builder = (StatementBuilder) createClassInstance(statementClass);
                List<Object> iterationData = builder.createIterationData(getTestClass(), testAnnotatedMethod);

                if (!iterationData.isEmpty())
                {
                    builderList.add(builder);
                    builderDataList.add(iterationData);
                }
            }
            testMethods.addAll(buildCrossProduct(testAnnotatedMethod.getMethod(), builderList, builderDataList));
        }
        computedTestMethods = testMethods;

        return testMethods;
    }

    private List<FrameworkMethod> buildCrossProduct(Method method, List<StatementBuilder> builderList, List<List<Object>> builderDataList)
    {
        List<FrameworkMethod> resultingMethods = new LinkedList<>();
        recursiveBuildCrossProduct(method, builderList, builderDataList, 0, resultingMethods, null);
        return resultingMethods;
    }

    private void recursiveBuildCrossProduct(Method method, List<StatementBuilder> builderList, List<List<Object>> builderDataList,
                                            int currentIndex, List<FrameworkMethod> resultingMethods,
                                            MyFrameworkMethod actualFrameworkMethod)
    {
        StatementBuilder builder = builderList.get(currentIndex);
        List<Object> builderData = builderDataList.get(currentIndex);

        for (Object data : builderData)
        {
            MyFrameworkMethod newMethod = new MyFrameworkMethod(method);
            if (actualFrameworkMethod != null)
            {
                newMethod.getBuilder().addAll(actualFrameworkMethod.getBuilder());
                newMethod.getData().addAll(actualFrameworkMethod.getData());
            }
            newMethod.getBuilder().add(builder);
            newMethod.getData().add(data);

            if (currentIndex < builderList.size() - 1)
            {
                recursiveBuildCrossProduct(method, builderList, builderDataList, currentIndex + 1, resultingMethods, newMethod);
            }
            else
            {
                resultingMethods.add(newMethod);
            }

        }
    }

    private Object createClassInstance(Class<?> clazz)
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

    @Override
    protected Description describeChild(FrameworkMethod method)
    {
        return Description.createTestDescription(getTestClass().getJavaClass(), testName(method), method.getAnnotations());
    }

    @Override
    protected String testName(FrameworkMethod method)
    {
        if (method instanceof MyFrameworkMethod)
        {
            return ((MyFrameworkMethod) method).getTestName();
        }
        else
        {
            return super.testName(method);
        }
    }
}
