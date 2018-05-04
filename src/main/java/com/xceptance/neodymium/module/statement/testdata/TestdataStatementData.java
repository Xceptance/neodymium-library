package com.xceptance.neodymium.module.statement.testdata;

import java.util.HashMap;
import java.util.Map;

public class TestdataStatementData
{

    private Map<String, String> dataSet;

    private Map<String, String> packageTestData;

    private int index;

    private int size;

    private int iterationIndex;

    public TestdataStatementData(Map<String, String> dataSet, Map<String, String> packageTestData, int index, int size)
    {
        this.dataSet = dataSet;
        this.packageTestData = packageTestData;
        this.index = index;
        this.size = size;
        this.iterationIndex = 0;
    }

    public TestdataStatementData(TestdataStatementData another)
    {
        if (another.dataSet != null)
        {
            this.dataSet = new HashMap<>();
            this.dataSet.putAll(another.dataSet);
        }

        if (another.packageTestData != null)
        {
            this.packageTestData = new HashMap<>();
            this.packageTestData.putAll(another.packageTestData);
        }
        this.size = another.size;
        this.index = another.index;
        this.iterationIndex = another.iterationIndex;
    }

    public Map<String, String> getDataSet()
    {
        return dataSet;
    }

    public Map<String, String> getPackageTestData()
    {
        return packageTestData;
    }

    public int getIndex()
    {
        return index;
    }

    public int getSize()
    {
        return size;
    }

    public int getIterationIndex()
    {
        return iterationIndex;
    }

    public void setIterationIndex(int iterationIndex)
    {
        this.iterationIndex = iterationIndex;
    }
}
