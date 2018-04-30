package com.xceptance.neodymium.module.statement.testdata;

import java.util.Map;

public class TestdataStatementData
{

    private Map<String, String> dataSet;

    private Map<String, String> packageTestData;

    private int index;

    private int size;

    public TestdataStatementData(Map<String, String> dataSet, Map<String, String> packageTestData, int index, int size)
    {
        this.dataSet = dataSet;
        this.packageTestData = packageTestData;
        this.index = index;
        this.size = size;
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

}
