package com.xceptance.neodymium.testclasses.context.cucumbercontextclear;

import org.junit.Assert;

import com.xceptance.neodymium.util.Neodymium;
import com.xceptance.neodymium.util.NeodymiumConfiguration;
import com.xceptance.neodymium.util.WebDriverUtils;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

public class CucumberContextSteps
{
    @Before
    public void beforeTest()
    {
        // setup browser since we set our selenide defaults only if a browser is involved
        WebDriverUtils.setUp("Chrome_headless");
    }

    @After(order = 100)
    public void teardown(Scenario scenario)
    {
        WebDriverUtils.tearDown(scenario);
    }

    @Given("Change timeout to 4000")
    public void testAndChangeDefaultTimeout()
    {
        NeodymiumConfiguration configuration = Neodymium.configuration();
        Assert.assertEquals(3000, configuration.selenideTimeout());

        configuration.setProperty("neodymium.selenide.timeout", "4000");
        Assert.assertEquals(4000, configuration.selenideTimeout());
    }

    @Given("Assert timeout of 3000")
    public void testDefaultTimeout()
    {
        NeodymiumConfiguration configuration = Neodymium.configuration();
        Assert.assertEquals(3000, configuration.selenideTimeout());
    }

    @Given("Change default collection timeout to 1234")
    public void testAndChangeDefaultCollectionTimeout()
    {
        com.codeborne.selenide.Configuration.collectionsTimeout = 1234;
        Assert.assertEquals(1234, com.codeborne.selenide.Configuration.collectionsTimeout);
    }

    @Given("Assert collection timeout of 6000")
    public void testDefaultCollectionTimeout()
    {
        Assert.assertEquals(6000, com.codeborne.selenide.Configuration.collectionsTimeout);
    }
}
