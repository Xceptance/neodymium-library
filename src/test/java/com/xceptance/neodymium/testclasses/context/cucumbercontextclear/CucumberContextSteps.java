package com.xceptance.neodymium.testclasses.context.cucumbercontextclear;

import org.junit.Assert;

import com.xceptance.neodymium.util.Configuration;
import com.xceptance.neodymium.util.Context;
import com.xceptance.neodymium.util.Driver;

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
        Driver.setUp("Chrome_headless");
    }

    @After(order = 100)
    public void teardown(Scenario scenario)
    {
        Driver.tearDown(scenario);
    }

    @Given("Change timeout to 4000")
    public void testAndChangeDefaultTimeout()
    {
        Configuration configuration = Context.get().configuration;
        Assert.assertEquals(3000, configuration.timeout());

        configuration.setProperty("selenide.timeout", "4000");
        Assert.assertEquals(4000, configuration.timeout());
    }

    @Given("Assert timeout of 3000")
    public void testDefaultTimeout()
    {
        Configuration configuration = Context.get().configuration;
        Assert.assertEquals(3000, configuration.timeout());
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
