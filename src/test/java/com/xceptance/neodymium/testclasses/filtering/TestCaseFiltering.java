package com.xceptance.neodymium.testclasses.filtering;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.SuppressDataSets;
import com.xceptance.neodymium.util.DataUtils;

import io.cucumber.java.it.Data;

@RunWith(NeodymiumRunner.class)
public class TestCaseFiltering
{

    @Test
    @Data("id=executable")
    public void shouldBeExecuted()
    {
        Assert.assertTrue("This test should be executed", true);
    }

    @Test
    @SuppressDataSets
    public void shouldNotBeExecuted()
    {
        Assert.assertTrue("This test should not be executed", false);
    }

    @Test
    public void shouldBeExecutedForDataSetWithExecutableId()
    {
        Assert.assertEquals("This test should only be executed for data set with id 'executable'", "executable",
                            DataUtils.asString("testId"));
    }

}
