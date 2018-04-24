package com.xceptance.neodymium.tests.refactor;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.multibrowser.Browser;

//@RunOrder({BrowserVector.class, TestdataVector.class, PrarameterVector.class})
@RunWith(NeodymiumRunner.class)
@Browser(
    {
        "A", "B"
    })
public class IAmStupidToo
{
    @Test
    @Browser({})
    public void test7_EMPTY() throws Exception
    {

    }
}
