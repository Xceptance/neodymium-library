package com.xceptance.neodymium.junit5.testclasses.context;

import org.junit.jupiter.api.Assertions;

import com.xceptance.neodymium.junit5.NeodymiumTest;
import com.xceptance.neodymium.util.Neodymium;

public class OverrideNeodymiumConfiguration
{
    @NeodymiumTest
    public void testStandardConfigurationStillAvailable() throws Exception
    {
        Assertions.assertEquals("/some/opera/path/just/for/test/purpose", Neodymium.configuration().getOperaDriverPath());
    }

    @NeodymiumTest
    public void testConfigurationContainsOverriddenParts() throws Exception
    {
        Assertions.assertEquals("/some/phantomjs/path/just/for/test/newPurpose", Neodymium.configuration().getPhantomJsDriverPath());
    }
}
