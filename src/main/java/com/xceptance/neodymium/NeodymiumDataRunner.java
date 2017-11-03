package com.xceptance.neodymium;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import com.xceptance.neodymium.testdata.TestDataUtils;

public class NeodymiumDataRunner extends ParentRunner<Runner>
{
    private List<Runner> children;

    public NeodymiumDataRunner(TestClass testClass, MethodExecutionContext methodExecutionContext)
        throws InitializationError, NoSuchFieldException
    {
        super(testClass.getJavaClass());
        List<Map<String, String>> dataSets;

        children = new LinkedList<>();

        try
        {
            dataSets = TestDataUtils.getDataSets(testClass.getJavaClass());
            // Map<String, String> packageTestData = TestDataUtils.getPackageTestData(testClass);
            // System.out.println(packageTestData.size());
        }
        catch (Exception e)
        {
            throw new InitializationError(e);
        }

        // check if there is a field Map<String, String> data
        Field dataField;
        try
        {
            List<FrameworkField> testDataFields = testClass.getAnnotatedFields(TestData.class);

            if (testDataFields.size() == 0)
                throw new NoSuchFieldException();

            // we simply take the first field
            FrameworkField frameworkField = testDataFields.get(0);
            Class<?> type = frameworkField.getType();

            if (type != Map.class)
                throw new NoSuchFieldException();

            dataField = frameworkField.getField();

            // dataField = testClass.getField("data");
        }
        catch (NoSuchFieldException e)
        {
            // throw new RuntimeException("No public field \"Map<String, String> data\" was found in class hierarchy");
            // expected and caught in NeodymiumRunner
            throw e;
        }
        catch (SecurityException e)
        {
            throw new InitializationError(e);
        }

        if (dataSets != null)
        {

            for (Map<String, String> dataSet : dataSets)
            {
                children.add(new NeodymiumDataRunnerRunner(testClass.getJavaClass(), dataField, dataSet, methodExecutionContext));
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

        private Field dataField;

        public NeodymiumDataRunnerRunner(Class<?> testClass, Field dataField, Map<String, String> data,
                                         MethodExecutionContext methodExecutionContext)
        {
            this.testClass = testClass;
            this.dataField = dataField;
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
                dataField.set(methodExecutionContext.getTestClassInstance(), data);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

    }
}
