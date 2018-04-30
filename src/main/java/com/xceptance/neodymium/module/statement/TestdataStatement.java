package com.xceptance.neodymium.module.statement;

import java.io.FileNotFoundException;
import java.io.IOException;
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

import com.xceptance.neodymium.testdata.TestDataUtils;
import com.xceptance.neodymium.util.Context;

public class TestdataStatement extends StatementBuilder
{
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

        return iterations;

    }

    @Override
    public StatementBuilder createStatement(Statement next, Object parameter)
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
            String testDatasetIndetifier = testData.get("ID");
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
