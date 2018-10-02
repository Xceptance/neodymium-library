package com.xceptance.neodymium.testclasses.multibrowser;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.TestEnvironment;
import com.xceptance.neodymium.tests.EnvironmentConfigurationTest;

public class EnvironmentsConfiguration
{
    @Test
    public void testEnvionmentApi()
    {
        TestEnvironment environment = MultibrowserConfiguration.getInstance().getTestEnvironment("unittest");

        Assert.assertEquals(EnvironmentConfigurationTest.URL, environment.getUrl());
        Assert.assertEquals(EnvironmentConfigurationTest.USERNAME, environment.getUsername());
        Assert.assertEquals(EnvironmentConfigurationTest.PASSWORD, environment.getPassword());
    }

    @Test
    public void testUnavailableEnvironment()
    {
        TestEnvironment environment = MultibrowserConfiguration.getInstance().getTestEnvironment("notdefined");

        Assert.assertNull(environment);
    }

    @Test
    public void testOverridingEnvironment()
    {
        TestEnvironment environment = MultibrowserConfiguration.getInstance().getTestEnvironment("override");

        Assert.assertEquals(EnvironmentConfigurationTest.URL2, environment.getUrl());
        Assert.assertEquals(EnvironmentConfigurationTest.USERNAME2, environment.getUsername());
        Assert.assertEquals(EnvironmentConfigurationTest.PASSWORD2, environment.getPassword());
    }
}
