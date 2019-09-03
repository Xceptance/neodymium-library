package com.xceptance.neodymium.testclasses.multibrowser;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.TestEnvironment;
import com.xceptance.neodymium.tests.EnvironmentAndBrowserConfigurationTest;

public class EnvironmentAndBrowserConfiguration
{
    @Test
    public void testEnvironmentApi()
    {
        TestEnvironment environment = MultibrowserConfiguration.getInstance().getTestEnvironment("unittest");

        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.URL, environment.getUrl());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.USERNAME, environment.getUsername());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PASSWORD, environment.getPassword());

        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.USEPROXY1, environment.useProxy());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PROXYHOST1, environment.getProxyHost());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PROXYPORT1, environment.getProxyPort());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PROXYUSERNAME1, environment.getProxyUsername());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PROXYPASSWORD1, environment.getProxyPassword());
    }

    @Test
    public void testUnavailableEnvironment()
    {
        TestEnvironment environment = MultibrowserConfiguration.getInstance().getTestEnvironment("notdefined");

        Assert.assertNull(environment);
    }

    @Test
    public void testDeactivatedEnvironmentProxy()
    {
        TestEnvironment environment = MultibrowserConfiguration.getInstance().getTestEnvironment("noProxy");

        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.URL, environment.getUrl());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.USERNAME, environment.getUsername());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PASSWORD, environment.getPassword());

        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.USEPROXY2, environment.useProxy());
        Assert.assertNull(environment.getProxyHost());
        Assert.assertNull(environment.getProxyPort());
        Assert.assertNull(environment.getProxyUsername());
        Assert.assertNull(environment.getProxyPassword());
    }

    @Test
    public void testOverridingEnvironment()
    {
        TestEnvironment environment = MultibrowserConfiguration.getInstance().getTestEnvironment("override");

        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.URL2, environment.getUrl());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.USERNAME2, environment.getUsername());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PASSWORD2, environment.getPassword());

        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.USEPROXY1, environment.useProxy());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PROXYHOST1, environment.getProxyHost());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PROXYPORT1, environment.getProxyPort());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PROXYUSERNAME2, environment.getProxyUsername());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.PROXYPASSWORD2, environment.getProxyPassword());
    }

    @Test
    public void testOverridingBrowsers()
    {
        BrowserConfiguration configuration = MultibrowserConfiguration.getInstance().getBrowserProfiles().get("Galaxy_Note3_Emulation");

        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.BROWSERNAME, configuration.getName());
        Assert.assertEquals(EnvironmentAndBrowserConfigurationTest.ENVIRONMENTNAME, configuration.getTestEnvironment());
    }
}
