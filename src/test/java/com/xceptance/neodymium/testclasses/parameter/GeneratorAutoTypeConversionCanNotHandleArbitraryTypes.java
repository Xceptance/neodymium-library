package com.xceptance.neodymium.testclasses.parameter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.BrowserConfiguration;

@RunWith(NeodymiumRunner.class)
public class GeneratorAutoTypeConversionCanNotHandleArbitraryTypes
{
    @Parameters
    public static List<Object[]> createData()
    {
        List<Object[]> iterations = new ArrayList<>(1);
        iterations.add(new Object[]
            {
                "a string can not be parsed to an arbitrary type"
            });
        return iterations;
    }

    @Parameter
    public BrowserConfiguration browser;

    @Test
    public void test()
    {

    }
}
