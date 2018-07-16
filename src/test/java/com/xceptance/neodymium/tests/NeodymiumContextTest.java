package com.xceptance.neodymium.tests;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.xceptance.neodymium.testclasses.context.ContextGetsCleared;
import com.xceptance.neodymium.testclasses.context.DefaultSelenideTimeoutCheck;
import com.xceptance.neodymium.testclasses.context.cucumbercontextclear.CucumberContextGetsCleared;

public class NeodymiumContextTest extends NeodymiumTest
{
    @Test
    public void testContextGetCleared() throws Exception
    {
        // test that NeodymiumRunner clears the context before each run
        Result result = JUnitCore.runClasses(ContextGetsCleared.class);
        checkPass(result, 2, 0, 0);
    }

    @Test
    public void testCucumberContextGetsCleared() throws Exception
    {
        // test that NeodymiumCucumberRunListener clears the context before each run
        Result result = JUnitCore.runClasses(CucumberContextGetsCleared.class);
        checkPass(result, 4, 0, 0);
    }

    @Test
    public void testDefaultSelenideTimeoutCheck() throws Exception
    {
        Result result = JUnitCore.runClasses(DefaultSelenideTimeoutCheck.class);
        checkPass(result, 2, 0, 0);
    }
}
