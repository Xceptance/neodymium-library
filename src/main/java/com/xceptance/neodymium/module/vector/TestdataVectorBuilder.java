package com.xceptance.neodymium.module.vector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import com.xceptance.neodymium.testdata.TestDataUtils;

public class TestdataVectorBuilder implements RunVectorBuilder
{

    private List<Map<String, String>> dataSets;

    private Map<String, String> packageTestData;

    @Override
    public void create(TestClass testClass, FrameworkMethod frameworkMethod)
    {
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
    }

    @Override
    public List<RunVector> buildRunVectors()
    {
        List<RunVector> children = new LinkedList<>();
        if (!dataSets.isEmpty() || !packageTestData.isEmpty())
        {

            if (!dataSets.isEmpty())
            {
                // data sets found
                for (int i = 0; i < dataSets.size(); i++)
                {
                    // children.add(new NeodymiumDataRunnerRunner(testClass.getJavaClass(), dataSets, i,
                    // packageTestData,
                    // methodExecutionContext));
                    children.add(new TestdataVector(dataSets.get(i), packageTestData, i, dataSets.size()));
                }
            }
            else
            {
                // only package data, no data sets
                // children.add(new NeodymiumDataRunnerRunner(testClass.getJavaClass(), dataSets, -1, packageTestData,
                // methodExecutionContext));
                children.add(new TestdataVector(new HashMap<>(), packageTestData, -1, -1));
            }
        }
        else
        {
            // we couldn't find any data sets
        }

        return children;
    }

}
