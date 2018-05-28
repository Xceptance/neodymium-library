package com.xceptance.neodymium.testclasses.data.override.methodonly;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;

@RunWith(NeodymiumRunner.class)
public class MethodExplicitDefaultTwoDataSets
{
    @Test
    @DataSet(0)
    public void test1() throws Exception
    {

    }
}
