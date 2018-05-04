package com.xceptance.neodymium.module.statement.testdata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xceptance.neodymium.module.StatementBuilder;
import com.xceptance.neodymium.module.statement.testdata.util.TestDataUtils;
import com.xceptance.neodymium.util.Context;

public class TestdataStatement extends StatementBuilder
{
    private static final String TEST_ID = "testId";

    public static Logger LOGGER = LoggerFactory.getLogger(TestdataStatement.class);

    private Statement next;

    Map<String, String> testData;

    public TestdataStatement(Statement next, TestdataStatementData parameter)
    {
        this.next = next;

        int currentDataSetIndex = parameter.getIndex();

        testData = new HashMap<>();
        testData.putAll(parameter.getPackageTestData());

        if (currentDataSetIndex >= 0)
        {
            for (Entry<String, String> newDataEntry : parameter.getDataSet().entrySet())
            {
                // only log if a data set entry overwrites an package data entry
                if (testData.containsKey(newDataEntry.getKey()))
                {
                    LOGGER.debug(String.format("Data entry \"%s\" overwritten by data set #%d (old: \"%s\", new: \"%s\")",
                                               newDataEntry.getKey(), currentDataSetIndex + 1, testData.get(newDataEntry.getKey()),
                                               newDataEntry.getValue()));
                }
                testData.put(newDataEntry.getKey(), newDataEntry.getValue());
            }
        }

    }

    public TestdataStatement()
    {
    }

    @Override
    public void evaluate() throws Throwable
    {
        Context.get().data.putAll(testData);
        next.evaluate();
    }

    @Override
    public List<Object> createIterationData(TestClass testClass, FrameworkMethod method)
    {
        List<Map<String, String>> dataSets;
        Map<String, String> packageTestData;
        try
        {
            dataSets = TestDataUtils.getDataSets(testClass.getJavaClass());
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        packageTestData = TestDataUtils.getPackageTestData(testClass.getJavaClass());

        List<Object> iterations = new LinkedList<>();
        if (!dataSets.isEmpty() || !packageTestData.isEmpty())
        {
            if (!dataSets.isEmpty())
            {
                // data sets found
                for (int i = 0; i < dataSets.size(); i++)
                {
                    iterations.add(new TestdataStatementData(dataSets.get(i), packageTestData, i, dataSets.size()));
                }
            }
            else
            {
                // only package data, no data sets
                iterations.add(new TestdataStatementData(new HashMap<>(), packageTestData, -1, -1));
            }
        }
        else
        {
            // we couldn't find any data sets
        }

        return processOverrides(testClass, method, iterations);
    }

    private List<Object> processOverrides(TestClass testClass, FrameworkMethod method, List<Object> iterations)
    {
        SuppressDataSets methodSuppress = method.getAnnotation(SuppressDataSets.class);
        if (methodSuppress != null)
        {
            // the test method is marked to suppress data sets
            return new LinkedList<>();
        }

        SuppressDataSets classSuppress = testClass.getAnnotation(SuppressDataSets.class);
        List<DataSet> methodDataSetAnnotations = getMethodDataSetAnnotations(method);

        if (methodDataSetAnnotations.isEmpty() && classSuppress != null)
        {
            // class is marked to suppress data sets and there is no overriding DataSet on the method
            return new LinkedList<>();
        }

        List<DataSet> dataSetAnnotations = new LinkedList<>();
        if (classSuppress == null)
        {
            List<DataSet> classDataSetAnnotations = getClassDataSetAnnotations(testClass);

            // at this point neither the class nor the method could have a data set supress
            if (!methodDataSetAnnotations.isEmpty())
            {
                dataSetAnnotations = methodDataSetAnnotations;
            }
            else if (!classDataSetAnnotations.isEmpty())
            {
                dataSetAnnotations = classDataSetAnnotations;
            }
        }

        if (dataSetAnnotations.isEmpty())
        {
            // so there is nothing to suppress and nothing to override. Go ahead with all data sets
            return iterations;
        }

        List<Object> fixedIterations = new LinkedList<>();
        for (DataSet dataSet : dataSetAnnotations)
        {
            int dataSetIndex = dataSet.value();
            String dataSetId = dataSet.id();

            // take dataSetId (testId) if is set
            if (dataSetId != null && dataSetId.trim().length() > 0)
            {
                // search the dataset
                boolean found = false;
                for (Object object : iterations)
                {
                    Map<String, String> dataSetInstance = (Map<String, String>) object;
                    String testId = dataSetInstance.get(TEST_ID);
                    if (dataSetId.equals(testId))
                    {
                        fixedIterations.add(object);
                        found = true;
                        break;
                    }
                }
                if (!found)
                {
                    String msg = MessageFormat.format("Could not find data set with test id ''{0}''", dataSetId);
                    throw new IllegalArgumentException(msg);
                }
            }
            else
            {
                if (dataSetIndex <= 0)
                {
                    // add all data sets
                    fixedIterations.addAll(iterations);
                }
                else
                {
                    if (dataSetIndex > iterations.size())
                    {
                        String msg = MessageFormat.format("Method ''{0}'' is marked to be run only with data set index {1}, but there are only {2}",
                                                          method.getName(), dataSetIndex, iterations.size());
                        throw new IllegalArgumentException(msg);
                    }
                    else
                    {
                        fixedIterations.add(iterations.get(dataSetIndex));
                    }
                }
            }
        }
        return fixedIterations;

        // // check first the method an then the class for an Testdata annotation
        // // if there is any fixed dataset set then modidy iteration list
        // DataSet annotation = method.getAnnotation(DataSet.class);
        // if (annotation == null)
        // {
        // annotation = testClass.getAnnotation(DataSet.class);
        // }
        // if (annotation != null)
        // {
        // int fixedDataSetIndex = annotation.value();
        // // just ignore all values <= 0
        // if (fixedDataSetIndex > -1)
        // {
        // if (fixedDataSetIndex > iterations.size())
        // {
        // String msg = MessageFormat.format("Method ''{0}'' is marked to be run only with data set index {1}, but there
        // are only {2}",
        // method.getName(), fixedDataSetIndex, iterations.size());
        // throw new IllegalArgumentException(msg);
        // }
        //
        // List<Object> fixedIteration = new LinkedList<>();
        // // we define 0 to equal to run without datasets
        // if (fixedDataSetIndex > 0)
        // {
        // fixedIteration.add(iterations.get(fixedDataSetIndex - 1));
        // }
        //
        // return fixedIteration;
        // }
        // }
        //
        // return iterations;
    }

    private List<DataSet> getClassDataSetAnnotations(TestClass testClass)
    {
        List<DataSet> annotations = new LinkedList<>();

        DataSets classDataSets = testClass.getAnnotation(DataSets.class);
        if (classDataSets != null)
        {
            annotations.addAll(Arrays.asList(classDataSets.value()));
        }

        DataSet classDataSet = testClass.getAnnotation(DataSet.class);
        if (classDataSet != null)
        {
            annotations.add(classDataSet);
        }

        return annotations;
    }

    private List<DataSet> getMethodDataSetAnnotations(FrameworkMethod method)
    {
        List<DataSet> annotations = new LinkedList<>();

        DataSets classDataSets = method.getAnnotation(DataSets.class);
        if (classDataSets != null)
        {
            annotations.addAll(Arrays.asList(classDataSets.value()));
        }

        DataSet classDataSet = method.getAnnotation(DataSet.class);
        if (classDataSet != null)
        {
            annotations.add(classDataSet);
        }

        return annotations;
    }

    @Override
    public StatementBuilder createStatement(Object testClassInstance, Statement next, Object parameter)
    {
        return new TestdataStatement(next, (TestdataStatementData) parameter);
    }

    @Override
    public String getTestName(Object data)
    {
        TestdataStatementData parameter = (TestdataStatementData) data;
        Map<String, String> testData = new HashMap<>();
        testData.putAll(parameter.getPackageTestData());
        testData.putAll(parameter.getDataSet());

        String testname;
        if (parameter.getIndex() >= 0)
        {
            // data sets and (maybe) package data
            String testDatasetIndetifier = testData.get(TEST_ID);
            if (StringUtils.isBlank(testDatasetIndetifier))
            {
                testDatasetIndetifier = "Data set";
            }
            testname = String.format("%s %d / %d", testDatasetIndetifier, (parameter.getIndex() + 1), parameter.getSize());
        }
        else
        {
            // only package data
            testname = "TestData";
        }

        return testname;
    }

}
