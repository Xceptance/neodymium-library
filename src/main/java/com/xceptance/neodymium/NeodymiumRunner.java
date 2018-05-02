package com.xceptance.neodymium;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.xceptance.neodymium.module.EnhancedMethod;
import com.xceptance.neodymium.module.StatementBuilder;
import com.xceptance.neodymium.module.order.DefaultStatementRunOrder;

public class NeodymiumRunner extends BlockJUnit4ClassRunner
{
    public NeodymiumRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    private enum DescriptionMode
    {
     flat,
     hierarchical,
     extrawurst
    };

    private DescriptionMode descriptionMode = DescriptionMode.flat;

    Map<EnhancedMethod, Statement> methodStatements;

    List<FrameworkMethod> computedTestMethods;

    Map<FrameworkMethod, Description> childDescriptions = new HashMap<>();

    private Description globalTestDescription = null;

    private Object testClassInstance;

    @Override
    protected Statement methodBlock(FrameworkMethod method)
    {
        Statement methodStatement = super.methodBlock(method);

        if (method instanceof EnhancedMethod)
        {
            EnhancedMethod m = (EnhancedMethod) method;
            for (int i = m.getBuilder().size() - 1; i >= 0; i--)
            {
                StatementBuilder statementBuilder = m.getBuilder().get(i);
                Object data = m.getData().get(i);
                methodStatement = statementBuilder.createStatement(testClassInstance, methodStatement, data);
            }
        }

        return methodStatement;
    }

    @Override
    protected Object createTest() throws Exception
    {
        this.testClassInstance = super.createTest();
        return testClassInstance;
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods()
    {
        // since this method is called at least two times and is somewhat expensive, we cache the result
        if (computedTestMethods != null)
            return computedTestMethods;

        List<FrameworkMethod> testMethods = new LinkedList<>();

        List<Class<? extends StatementBuilder>> statementRunOrder = new DefaultStatementRunOrder().getRunOrder();
        for (FrameworkMethod testAnnotatedMethod : super.computeTestMethods())
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

                List<Object> iterationData = null;
                try
                {
                    iterationData = builder.createIterationData(getTestClass(), testAnnotatedMethod);
                }
                catch (Throwable e)
                {
                    throw new RuntimeException(e);
                }

                // new InitializationError(error)
                if (iterationData != null && !iterationData.isEmpty())
                {
                    builderList.add(builder);
                    builderDataList.add(iterationData);
                }
            }
            testMethods.addAll(buildCrossProduct(testAnnotatedMethod.getMethod(), builderList, builderDataList));
        }
         computedTestMethods = Collections.unmodifiableList(testMethods);

        return computedTestMethods;
    }

    private List<FrameworkMethod> buildCrossProduct(Method method, List<StatementBuilder> builderList, List<List<Object>> builderDataList)
    {
        List<FrameworkMethod> resultingMethods = new LinkedList<>();
        recursiveBuildCrossProduct(method, builderList, builderDataList, 0, resultingMethods, null);
        return resultingMethods;
    }

    private void recursiveBuildCrossProduct(Method method, List<StatementBuilder> builderList, List<List<Object>> builderDataList,
                                            int currentIndex, List<FrameworkMethod> resultingMethods, EnhancedMethod actualFrameworkMethod)
    {
        if (builderList.isEmpty())
        {
            // if there is no enclosing statement involved we handle it as single method call
            // resultingMethods.add(new MyFrameworkMethod(method));
            resultingMethods.add(new FrameworkMethod(method));
        }
        else
        {
            StatementBuilder builder = builderList.get(currentIndex);
            List<Object> builderData = builderDataList.get(currentIndex);

            for (Object data : builderData)
            {
                EnhancedMethod newMethod = new EnhancedMethod(method);
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
        // cache child descriptions

        Description childDescription = childDescriptions.computeIfAbsent(method, (m) -> {
            return describeChildWithMode(method);
        });

        System.out.println(MessageFormat.format("Method {0}; Description {1} {2}", method.getName(), childDescription,
                                                childDescription.hashCode()));

        return childDescription;
    }

    private Description describeChildWithMode(FrameworkMethod method)
    {
        switch (descriptionMode)
        {
            case flat:
        return Description.createTestDescription(getTestClass().getJavaClass(), testName(method), method.getAnnotations());

            case hierarchical:
                if (method instanceof EnhancedMethod)
                {
                    return describeEnhancedMethod((EnhancedMethod) method);
                }
                else
                {
                    return Description.createTestDescription(getTestClass().getJavaClass().getName(), method.getName(), testName(method));
                }

            case extrawurst:
                throw new NotImplementedException("extrawurst");
        }
        return null;
    }

    private Description describeEnhancedMethod(EnhancedMethod method)
    {
        Description childDescription = null;
        for (int i = 0; i < method.getBuilder().size(); i++)
        {
            StatementBuilder statementBuilder = method.getBuilder().get(i);
            String testName = statementBuilder.getTestName(method.getData().get(i));

            if (childDescription == null)
            {
                childDescription = Description.createSuiteDescription(testName, method.getTestName());
            }
            else
            {
                childDescription.addChild(Description.createSuiteDescription(testName, method.getTestName()));
            }
        }
        childDescription.addChild(Description.createTestDescription(getTestClass().getJavaClass().getName(), method.getName(),
                                                                    method.getTestName()));

        return childDescription;
    }

    @Override
    public Description getDescription()
    {
        if (globalTestDescription == null)
        {
            switch (descriptionMode)
            {
                case flat:
                    globalTestDescription = getFlatTestDescription();
                    break;

                case hierarchical:
                    globalTestDescription = getHierarchicalDescription();
                    break;

                case extrawurst:
                    throw new NotImplementedException("extrawurst");
            }
        }
        return globalTestDescription;
    }

    private Description getFlatTestDescription()
    {
        Class<?> testClass = getTestClass().getJavaClass();
        Description suiteDescription = Description.createSuiteDescription(testClass);

        for (FrameworkMethod method : computeTestMethods())
        {
            if (method instanceof EnhancedMethod)
            {
                EnhancedMethod em = (EnhancedMethod) method;
                suiteDescription.addChild(Description.createTestDescription(testClass, em.getTestName()));
            }
            else if (method instanceof FrameworkMethod)
            {
                suiteDescription.addChild(Description.createTestDescription(testClass, method.getName()));
            }

        }

        return suiteDescription;
    }

    private Description getHierarchicalDescription()
    {
        Class<?> testClass = getTestClass().getJavaClass();
        Description suiteDescription = Description.createSuiteDescription(testClass);

        List<FrameworkMethod> computeTestMethods = computeTestMethods();

        for (FrameworkMethod method : computeTestMethods)
        {
            suiteDescription.addChild(describeChild(method));
        }

        return suiteDescription;

    }

    @Override
    protected String testName(FrameworkMethod method)
    {
        if (method instanceof EnhancedMethod)
        {
            return ((EnhancedMethod) method).getTestName();
        }
        else
        {
            return super.testName(method);
        }
    }
}
