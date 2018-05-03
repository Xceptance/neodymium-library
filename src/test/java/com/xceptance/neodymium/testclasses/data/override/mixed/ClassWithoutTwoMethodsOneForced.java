package com.xceptance.neodymium.testclasses.data.override.mixed;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.Testdata;

@RunWith(NeodymiumRunner.class)
@Testdata(0)
public class ClassWithoutTwoMethodsOneForced
{
    @Test
    @Testdata(1)
    public void test1() throws Exception
    {

    }

    @Test
    public void test2() throws Exception
    {

    }
}
