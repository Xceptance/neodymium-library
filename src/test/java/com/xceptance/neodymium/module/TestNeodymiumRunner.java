package com.xceptance.neodymium.module;

import org.junit.Test;
import org.junit.runner.JUnitCore;

public class TestNeodymiumRunner
{
    @Test
    public void testTestMethodRunner()
    {
        JUnitCore.runClasses(TestMethodVector.class);
    }
}
