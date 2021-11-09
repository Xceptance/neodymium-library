package com.xceptance.neodymium.common.testdata;

import com.xceptance.neodymium.util.Neodymium;

public class TestdataRunner
{
    private TestdataContainer testData;

    public TestdataRunner(TestdataContainer testData)
    {
        this.testData = testData;
    }

    public void setUpTest()
    {
        if (testData != null)
        {
            Neodymium.getData().putAll(testData.getDataSet());
        }
        else
        {
            System.out.println("using no dataset");
        }
    }
}
