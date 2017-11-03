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

            if (type != Map.class || !frameworkField.isPublic())
            {
                // expected and caught in NeodymiumRunner
                throw new NoSuchFieldException("The TestData field has to be a public map e.g. \"public Map<String, String> data;\"");
            }

            dataField = frameworkField.getField();
        }
        catch (SecurityException e)
        {
            throw new InitializationError(e);
        }

        if (dataSets != null)
        {

            for (int i = 0; i < dataSets.size(); i++)
            {
                children.add(new NeodymiumDataRunnerRunner(testClass.getJavaClass(), dataField, dataSets, i, methodExecutionContext));
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

        private List<Map<String, String>> dataSets;

        private Class<?> testClass;

        private MethodExecutionContext methodExecutionContext;

        private Field dataField;

        private int index;

        public NeodymiumDataRunnerRunner(Class<?> javaClass, Field dataField, List<Map<String, String>> dataSets, int index,
                                         MethodExecutionContext methodExecutionContext)
        {
            this.index = index;
            this.testClass = javaClass;
            this.dataField = dataField;
            this.dataSets = dataSets;
            this.methodExecutionContext = methodExecutionContext;
        }

        @Override
        public Description getDescription()
        {
            return Description.createSuiteDescription("Data set " + (index + 1) + " / " + dataSets.size(), testClass.getAnnotations());
        }

        @Override
        public void run(RunNotifier notifier)
        {
            try
            {
                dataField.set(methodExecutionContext.getTestClassInstance(), dataSets.get(index));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

    }
}
