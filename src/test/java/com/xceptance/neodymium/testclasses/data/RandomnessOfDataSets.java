package com.xceptance.neodymium.testclasses.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;
import com.xceptance.neodymium.module.statement.testdata.SuppressDataSets;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
public class RandomnessOfDataSets
{
    private static List<String> datasets = new ArrayList<String>();

    @Test
    @DataSet(randomSets = 4)
    public void test1()
    {
        datasets.add(DataUtils.asString("key1"));
    }

    @Test
    @SuppressDataSets
    public void test2()
    {
        boolean changedOrder = false;
        for (int i = 1; i < datasets.size(); i++)
        {
            if (!datasets.get(i).equals("val" + (i + 1)))
            {
                changedOrder = true;
                break;
            }
        }
        Assert.assertTrue(changedOrder);
    }
}
