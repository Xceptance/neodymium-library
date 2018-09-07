package com.xceptance.neodymium.tests;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Configuration.AssertionMode;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ex.ElementNotFound;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_headless")
public class SoftAssertionTest
{
    @Test(expected = ElementNotFound.class)
    public void validateSoftAssertion()
    {
        Neodymium.softAssertions(true);
        Selenide.open("https://blog.xceptance.com/");

        Assert.assertEquals(Configuration.assertionMode, AssertionMode.SOFT);
        $("#notFound1").should(exist);
        $("#notFound2").should(exist);
        $("#masthead .search-toggle").click();
        $("#notFound3").should(exist);
        $("#notFound4").click();

        // This should not be called since
        throw new NullPointerException();

        // TODO validate that SoftAssertions are visible in JUnit output
    }
}
