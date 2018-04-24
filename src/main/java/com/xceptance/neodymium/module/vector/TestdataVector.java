package com.xceptance.neodymium.module.vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xceptance.neodymium.util.Context;

public class TestdataVector implements RunVector
{
    public static Logger LOGGER = LoggerFactory.getLogger(TestdataVector.class);

    private Map<String, String> testData;

    private int currentDataSetIndex;

    private int dataSetAmount;

    public TestdataVector(Map<String, String> dataSet, Map<String, String> packageTestData, int currentDataSetIndex, int dataSetAmount)
    {
        this.currentDataSetIndex = currentDataSetIndex;
        this.dataSetAmount = dataSetAmount;

        testData = new HashMap<>();
        testData.putAll(packageTestData);

        if (currentDataSetIndex >= 0)
        {
            for (Entry<String, String> newDataEntry : dataSet.entrySet())
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

    @Override
    public void beforeMethod()
    {
        Context.get().data.putAll(testData);
    }

    @Override
    public void afterMethod()
    {
        // nothing
    }

    @Override
    public String getTestName()
    {
        String testname;
        if (currentDataSetIndex >= 0)
        {
            // data sets and (maybe) package data
            String testDatasetIndetifier = testData.get("ID");
            if (StringUtils.isBlank(testDatasetIndetifier))
            {
                testDatasetIndetifier = "Data set";
            }
            testname = String.format("%s %d / %d", testDatasetIndetifier, (currentDataSetIndex + 1), dataSetAmount);
        }
        else
        {
            // only package data
            testname = "TestData";
        }

        return testname;
    }

    @Override
    public int vectorHashCode()
    {
        return testData.hashCode();
    }

    @Override
    public void setTestClassInstance(Object testClassInstance)
    {
        // not used
    }

}
