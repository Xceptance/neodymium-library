package com.xceptance.neodymium;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.xceptance.neodymium.module.EnhancedMethod;
import com.xceptance.neodymium.module.StatementBuilder;
import com.xceptance.neodymium.module.order.DefaultStatementRunOrder;
import com.xceptance.neodymium.util.Context;

public class NeodymiumRunner extends BlockJUnit4ClassRunner
{
    public NeodymiumRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    public enum DescriptionMode
    {
     flat,
     hierarchical,
    };

    private List<FrameworkMethod> computedTestMethods;

    private Map<FrameworkMethod, Description> childDescriptions = new HashMap<>();

    private Description globalTestDescription = null;

    private Object testClassInstance;

    @Override
    protected Statement methodBlock(FrameworkMethod method)
    {
        // This will build a default JUnit statement which runs excactly one of the test methods including before/after
        // methods. This call will also create the test class instance in which this method will be invoked.
        Statement methodStatement = super.methodBlock(method);
        // We need this particular test class instance for our own statements but we can not access it from here.
        // We can get the instance by having createTest overridden, see implementation of createTest in this class as
        // well as in BlockJUnit4ClassRunner.

        // At this point our createTest implementation was called and we have the testClassInstance

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
        else if (method instanceof FrameworkMethod)
        {
            // This could happen if there are plain test methods in the class with no data sets or test data defined.
            // Also the SuppressDataSets annotation can degrade a method to an FrameworkMethod even if there is some
            // test data or data sets.

            // It's fine, just make sure super.methodBlock is called with this method and return the resulting statement
        }

        return methodStatement;
    }

    @Override
    protected Object createTest() throws Exception
    {
        // Very important code which will be called from super's methodBlock function (see our methodBlock)
        // The super's call creates the instance of the class to test which will be a new one for each method.
        // Since we need this particular instance for our own statements we need to save it for us.
        // Keep in mind that this method will be called for every method that will be returned from computeTestMethods.
        // So there is not the one and only test class instance. Its one instance per method.
        this.testClassInstance = super.createTest();
        return testClassInstance;
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods()
    {
        // Normally JUnit works with all methods that are annotated with @Test, see super's implementation
        // But we override this function in order to do all the fancy stuff, like method multiplication and so on.
        // So we basically start with the list of test methods and add and rearrange new one's to this list and JUnit
        // will take this list and call us for each entry to create a statement which actually does all the stuff.
        // Each entry of this list causes a call to methodBlock().

        // Since this method is called at least two times and is somewhat expensive, we cache the result
        if (computedTestMethods != null)
            return computedTestMethods;

        // That list will contain all methods that need to be run for the class
        List<FrameworkMethod> testMethods = new LinkedList<>();

        // Statement run order defines the order of our own statements that will surround the default JUnit statement
        // from methodBlock
        List<Class<? extends StatementBuilder>> statementRunOrder = new DefaultStatementRunOrder().getRunOrder();

        // super.computeTestMethods will return all methods that are annotated with @Test
        for (FrameworkMethod testAnnotatedMethod : super.computeTestMethods())
        {
            // these lists contain all the builders and data that will be responsible for a partiuclar method
            List<StatementBuilder> builderList = new LinkedList<>();
            List<List<Object>> builderDataList = new LinkedList<>();

            // iterate over statements defined in the order
            for (Class<? extends StatementBuilder> statementClass : statementRunOrder)
            {
                // ask each statement builder if this method should be processed
                // results in a list of parameters for this statement for method multiplication
                // e.g. for @Browser({"A", "B"}) the data list will contain "A" and "B"

                StatementBuilder builder = StatementBuilder.instantiate(statementClass);

                List<Object> iterationData = null;
                try
                {
                    iterationData = builder.createIterationData(getTestClass(), testAnnotatedMethod);
                }
                catch (Throwable e)
                {
                    throw new RuntimeException(e);
                }

                // Avoid empty entries in the list since its a hassle to deal with
                if (iterationData != null && !iterationData.isEmpty())
                {
                    // we save both, the builder instance as well as the "data" to run with
                    builderList.add(builder);
                    builderDataList.add(iterationData);
                }
            }

            // This is the point where multiple test methods are computed for the current processed method.
            testMethods.addAll(buildCrossProduct(testAnnotatedMethod.getMethod(), builderList, builderDataList));
        }

        // this list is now final for class execution so make it unmodifiable
        computedTestMethods = Collections.unmodifiableList(testMethods);

        return computedTestMethods;
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier)
    {
        // clear the context before next child run
        Context.clearThreadContext();
        super.runChild(method, notifier);
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

    @Override
    protected Description describeChild(FrameworkMethod method)
    {
        return describeChild(method, null);
    }

    private Description describeChild(FrameworkMethod method, Description parentDescription)
    {
        // cache child descriptions because they will never change and get requestet alot
        Description childDescription = childDescriptions.computeIfAbsent(method, (m) -> {
            return describeChildWithMode(m, parentDescription);
        });

        return childDescription;
    }

    private Description describeChildWithMode(FrameworkMethod method, Description parentDescription)
    {
        Class<?> clazz = getTestClass().getJavaClass();
        String className = clazz.getName();
        String testName = testName(method);
        Description description = null;
        switch (Context.get().configuration.junitViewMode())
        {
            case flat:
                description = Description.createTestDescription(className, testName, testName);
                break;

            case hierarchical:
                if (method instanceof EnhancedMethod)
                {
                    description = describeEnhancedMethod((EnhancedMethod) method, parentDescription);
                }
                else
                {
                    description = Description.createTestDescription(className, method.getName(), testName);
                }
                break;
        }
        if (parentDescription != null && description != null)
            parentDescription.addChild(description);
        return description;
    }

    private Description describeEnhancedMethod(EnhancedMethod method, Description parentDescription)
    {
        return recursiveDescribe(parentDescription, method, 0);
    }

    private Description recursiveDescribe(Description methodDescription, EnhancedMethod method, int currentBuilderIndex)
    {
        if (currentBuilderIndex == method.getBuilder().size())
        {
            // abort recursion
            methodDescription.addChild(Description.createTestDescription(getTestClass().getJavaClass().getName(), method.getTestName(),
                                                                         method.getTestName()));
            return methodDescription;
        }

        StatementBuilder statementBuilder = method.getBuilder().get(currentBuilderIndex);
        Object data = method.getData().get(currentBuilderIndex);
        Description childDescription = Description.createSuiteDescription(statementBuilder.getTestName(data), method.getTestName());
        methodDescription.addChild(childDescription);
        return recursiveDescribe(childDescription, method, (currentBuilderIndex + 1));
    }

    @Override
    public Description getDescription()
    {
        if (globalTestDescription == null)
        {
            globalTestDescription = getTestDescription();
        }
        return globalTestDescription;
    }

    private Description getTestDescription()
    {
        // create a suite description and a test description as childs for all methods
        Description suiteDescription = Description.createSuiteDescription(getTestClass().getJavaClass());

        for (FrameworkMethod method : computeTestMethods())
        {
            describeChild(method, suiteDescription);
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
