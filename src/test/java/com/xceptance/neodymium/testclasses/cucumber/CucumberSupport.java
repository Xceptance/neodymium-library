package com.xceptance.neodymium.testclasses.cucumber;

import org.junit.Assert;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.xceptance.neodymium.NeodymiumCucumberRunListener;
import com.xceptance.neodymium.util.Neodymium;
import com.xceptance.neodymium.util.WebDriverUtils;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class CucumberSupport
{
    // setup hooks
    @Given("^\"([^\"]*)\" is open$")
    public static void setUp(final String browserProfileName)
    {
        WebDriverUtils.setUp(browserProfileName);
    }

    @Before("@SetUpWithBrowserTag")
    public static void setUp(Scenario scenario)
    {
        WebDriverUtils.setUpWithBrowserTag(scenario);
    }

    // have a lower order number than default in order to shut down the driver after
    // the test case specific after hooks
    @After(order = 100)
    public void tearDown(Scenario scenario)
    {
        WebDriverUtils.tearDown(scenario);
    }

    // test hooks
    @Given("^the browser \"([^\"]*)\" is setup$")
    public void validateBrowser(String browserProfileName)
    {
        Assert.assertEquals(browserProfileName, Neodymium.getBrowserProfileName());
    }

    @Then("^validate the AllureSelenide listener is active$")
    public void validateAllureSelenideListenerIsActive()
    {
        Assert.assertTrue(" AllureSelenide listener is not attached", SelenideLogger.hasListener(NeodymiumCucumberRunListener.LISTENER_NAME));
    }
}
