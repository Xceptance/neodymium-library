package com.xceptance.neodymium.testclasses.data.override.mixed;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataSet;

@RunWith(NeodymiumRunner.class)
public class TwoDataSetsTwoMethodsOneWithout
{
    @Test
    public void test1() throws Exception
    {

    }

    @Test
    @DataSet(0)
    public void test2() throws Exception
    {

    }
}
