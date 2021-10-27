package com.xceptance.neodymium.junit5.testdata;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.xceptance.neodymium.util.Neodymium;

public class TestdataCallback implements BeforeEachCallback
{
    private TestdataContainer testData;

    private String methodName;

    public TestdataCallback(TestdataContainer testData, String methodName)
    {
        this.testData = testData;
        this.methodName = methodName;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception
    {
        if (testData != null)
        {
            System.out.println("using dataset " + testData.getTitle() + " for " + methodName);
            System.out.println(StringUtils.join(testData.getDataSet()));
            Neodymium.getData().putAll(testData.getDataSet());
        }
        else
        {
            System.out.println("using no dataset");
        }
    }

}
