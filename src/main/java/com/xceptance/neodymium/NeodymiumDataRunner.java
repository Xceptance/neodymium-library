package com.xceptance.neodymium;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xceptance.neodymium.testdata.TestDataUtils;
import com.xceptance.neodymium.util.Context;

public class NeodymiumDataRunner extends ParentRunner<Runner>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NeodymiumDataRunner.class);

    private List<Runner> children;

    public NeodymiumDataRunner(TestClass testClass, MethodExecutionContext methodExecutionContext) throws InitializationError
    {
        super(testClass.getJavaClass());
        List<Map<String, String>> dataSets;
        Map<String, String> packageTestData;

        children = new LinkedList<>();

        try
        {
            dataSets = TestDataUtils.getDataSets(testClass.getJavaClass());
            packageTestData = TestDataUtils.getPackageTestData(testClass.getJavaClass());
        }
        catch (Exception e)
        {
            throw new InitializationError(e);
        }

        if (!dataSets.isEmpty() || !packageTestData.isEmpty())
        {

            if (!dataSets.isEmpty())
            {
                // data sets found
                for (int i = 0; i < dataSets.size(); i++)
                {
                    children.add(new NeodymiumDataRunnerRunner(testClass.getJavaClass(), dataSets, i, packageTestData,
                                                               methodExecutionContext));
                }
            }
            else
            {
                // only package data, no data sets
                children.add(new NeodymiumDataRunnerRunner(testClass.getJavaClass(), dataSets, -1, packageTestData,
                                                           methodExecutionContext));
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

    public class NeodymiumDataRunnerRunner extends Runner
    {

        private Class<?> testClass;

        private MethodExecutionContext methodExecutionContext;

        private int index;

        private Map<String, String> testData;

        private int dataSetCount;

        public NeodymiumDataRunnerRunner(Class<?> javaClass, List<Map<String, String>> dataSets, int index,
                                         Map<String, String> packageTestData, MethodExecutionContext methodExecutionContext)
        {
            this.index = index;
            this.testClass = javaClass;
            this.dataSetCount = dataSets.size();
            this.methodExecutionContext = methodExecutionContext;

            // these are the test data that will be injected into the test class
            testData = new HashMap<>();
            // first put all package test data into the map
            testData.putAll(packageTestData);
            // if data sets are present then add them afterwards so they can overwrite package data
            if (index >= 0)
            {
                for (Entry<String, String> newDataEntry : dataSets.get(index).entrySet())
                {
                    // only log if a data set entry overwrites an package data entry
                    if (testData.containsKey(newDataEntry.getKey()))
                    {
                        LOGGER.debug(String.format("Data entry \"%s\" overwritten by data set #%d (old: \"%s\", new: \"%s\")",
                                                   newDataEntry.getKey(), index + 1, testData.get(newDataEntry.getKey()),
                                                   newDataEntry.getValue()));
                    }
                    testData.put(newDataEntry.getKey(), newDataEntry.getValue());
                }

                testData.putAll(dataSets.get(index));
            }
        }

        @Override
        public Description getDescription()
        {
            if (index >= 0)
            {
                // data sets and (maybe) package data
                String testDatasetIndetifier = testData.get("ID");
                if (StringUtils.isBlank(testDatasetIndetifier))
                {
                    testDatasetIndetifier = "Data set";
                }
                String displayName = String.format("%s %d / %d", testDatasetIndetifier, (index + 1), dataSetCount);
                return Description.createSuiteDescription(displayName, testClass.getAnnotations());
            }
            else
            {
                // only package data
                return Description.createSuiteDescription("TestData", testClass.getAnnotations());
            }
        }

        public boolean hasDataSets()
        {
            // used in NeodymiumRunner to determine if this runner should be visible in the runner list / JUnit view / test report
            return (index >= 0);
        }

        @Override
        public void run(RunNotifier notifier)
        {
            try
            {
                Context.get().data.putAll(testData);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

    }
}
