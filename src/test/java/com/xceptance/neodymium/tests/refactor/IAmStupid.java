package com.xceptance.neodymium.tests.refactor;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("A")
public class IAmStupid
{
    @Test
    public void test1() throws Exception
    {

    }

    @Test
    @Browser("B")
    public void testName() throws Exception
    {

    }
}
