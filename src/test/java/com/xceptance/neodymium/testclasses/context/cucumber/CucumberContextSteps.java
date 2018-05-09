package com.xceptance.neodymium.testclasses.context.cucumber;

import org.junit.Assert;

import com.xceptance.neodymium.util.Configuration;
import com.xceptance.neodymium.util.Context;

import cucumber.api.java.en.Given;

public class CucumberContextSteps
{
    @Given("Change timeout to 4000")
    public void contextContainsData()
    {
        Configuration configuration = Context.get().configuration;
        Assert.assertEquals(3000, configuration.timeout());

        configuration.setProperty("selenide.timeout", "4000");
    }

    @Given("Assert timeout of 3000")
    public void contextDataIsEmpty()
    {
        Configuration configuration = Context.get().configuration;
        Assert.assertEquals(3000, configuration.timeout());
    }
}
