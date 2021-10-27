package com.xceptance.neodymium.junit5;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import com.xceptance.neodymium.junit5.browser.BrowserData;
import com.xceptance.neodymium.junit5.testdata.TestdataContainer;
import com.xceptance.neodymium.junit5.testdata.TestdataData;

public class NeodymiumData
{
    private BrowserData browserData;

    private TestdataData testdataData;

    public NeodymiumData(Class<?> testClass)
    {
        this.browserData = new BrowserData(testClass);
        this.testdataData = new TestdataData(testClass);
    }

    public Stream<TestTemplateInvocationContext> computeTestMethods(Method templateMethod)
    {
        List<TestTemplateInvocationContext> muliplicationResult = new ArrayList<>();
        List<String> browsers = browserData.createIterationData(templateMethod);
        List<TestdataContainer> dataSets = testdataData.getTestDataForMethod(templateMethod);
        if (browsers.isEmpty())
        {
            browsers.add(null);
        }
        if (dataSets.isEmpty())
        {
            dataSets.add(null);
        }
        for (String browser : browsers)
        {
            for (TestdataContainer dataSet : dataSets)
            {
                muliplicationResult.add(new TemplateInvocationContext(templateMethod.getName(), browser, dataSet));
            }
        }
        return muliplicationResult.stream();
    }

}
