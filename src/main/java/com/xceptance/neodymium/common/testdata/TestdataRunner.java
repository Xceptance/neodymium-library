package com.xceptance.neodymium.common.testdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xceptance.neodymium.util.Neodymium;

public class TestdataRunner
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestdataRunner.class);

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
            LOGGER.debug("using no dataset");
        }
    }
}
