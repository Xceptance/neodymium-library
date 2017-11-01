package com.xceptance.neodymium;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import com.xceptance.neodymium.testdata.TestDataUtils;

public class NeodymiumDataRunner extends ParentRunner<Runner>
{
    private List<Runner> children;

    public NeodymiumDataRunner(Class<?> testClass, MethodExecutionContext methodExecutionContext) throws InitializationError
    {
        super(testClass);
        List<Map<String, String>> dataSets;

        children = new LinkedList<>();

        try
        {
            dataSets = TestDataUtils.getDataSets(testClass);
        }
        catch (Exception e)
        {
            throw new InitializationError(e);
        }

        if (dataSets != null)
        {

            for (Map<String, String> dataSet : dataSets)
            {
                children.add(new NeodymiumDataRunnerRunner(testClass, dataSet, methodExecutionContext));
            }
        }
        else
        {
            // we couldn't find any data sets
            // throw an IllegalArgumentException that will be caught in NeodymiumRunner
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected List<Runner> getChildren()
    {
        return children;
    }

    @Override
    protected Description describeChild(Runner child)
    {
        return ((NeodymiumDataRunnerRunner) child).getDescription();
    }

    @Override
    protected void runChild(Runner child, RunNotifier notifier)
    {
        child.run(notifier);
    }

    private class NeodymiumDataRunnerRunner extends Runner
    {

        private Map<String, String> data;

        private Class<?> testClass;

        private MethodExecutionContext methodExecutionContext;

        public NeodymiumDataRunnerRunner(Class<?> testClass, Map<String, String> data, MethodExecutionContext methodExecutionContext)
        {
            this.testClass = testClass;
            this.data = data;
            this.methodExecutionContext = methodExecutionContext;
        }

        @Override
        public Description getDescription()
        {
            return Description.createSuiteDescription(data.toString(), testClass.getAnnotations());
        }

        @Override
        public void run(RunNotifier notifier)
        {
            try
            {
                testClass.getField("data").set(methodExecutionContext.getTestClassInstance(), data);
            }
            catch (NoSuchFieldException e)
            {
                throw new RuntimeException("No field \"data\" was found in class hierarchy");
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

    }
}
