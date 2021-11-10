package com.xceptance.neodymium.junit5;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import com.xceptance.neodymium.common.testdata.TestdataContainer;
import com.xceptance.neodymium.junit5.browser.BrowserExecutionCallback;
import com.xceptance.neodymium.junit5.filtering.FilterTestMethodCallback;
import com.xceptance.neodymium.junit5.testdata.TestdataCallback;

public class TemplateInvocationContext implements TestTemplateInvocationContext
{
    private String methodName;

    private String browser;

    private TestdataContainer dataSet;

    public TemplateInvocationContext(String methodName, String browser, TestdataContainer dataSet)
    {
        this.methodName = methodName;
        this.browser = browser;
        this.dataSet = dataSet;
    }

    public String getDisplayName(int invocationIndex)
    {
        return methodName + (dataSet != null ? dataSet.getTitle() : "") + (browser != null ? " :: Browser " + browser : "");
    }

    @Override
    public List<Extension> getAdditionalExtensions()
    {
        List<Extension> extentions = new LinkedList<>();
        if (browser != null)
        {
            extentions.add(new BrowserExecutionCallback(browser));
        }
        if (dataSet != null)
        {
            extentions.add(new TestdataCallback(dataSet));
        }
        extentions.add(new FilterTestMethodCallback());
        return extentions;
    }
};
