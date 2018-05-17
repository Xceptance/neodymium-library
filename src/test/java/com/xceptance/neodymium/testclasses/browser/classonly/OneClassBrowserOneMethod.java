package com.xceptance.neodymium.testclasses.browser.classonly;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser(
    {
        "chrome"
    })
public class OneClassBrowserOneMethod
{
    @Test
    public void first()
    {

    }
}
