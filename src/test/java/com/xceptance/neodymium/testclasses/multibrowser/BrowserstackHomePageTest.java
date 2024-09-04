package com.xceptance.neodymium.testclasses.multibrowser;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("Safari_Browserstack")
public class BrowserstackHomePageTest
{
    @Test
    public void testVisitingHomepage()
    {
        // Goto the home page
        Selenide.open("https://www.xceptance.com/en/");

        // verify the opened browser is safari browser via navigator object, which contains information about the
        // browser
        Assert.assertTrue(Selenide.executeJavaScript("return navigator.userAgent.indexOf(\"Safari\")>-1;"));

        // short validation to check that the correct page was opened, should be moved to OpenHomePageFlow
        $("#service-areas").should(exist);
        // basic validation
        // Verifies the company Logo and name are visible.
        $("#navigation .navbar-brand a[title=Home]").shouldBe(visible);

        // Verifies the Navigation bar is visible
        $("#navigation .navbar-header ul.nav").shouldBe(visible);

        // Asserts there's categories in the nav bar.
        $$("#navigation .navbar-header ul.nav > li > a").shouldHave(sizeGreaterThan(0));

        // Asserts the first headline is there.
        $("#service-areas .landing-intro > h1").shouldBe(matchText("[A-Z].{3,}"));

        // Asserts the animated carousel is there.
        $("#myCarousel").shouldBe(visible);

        // Verifies the "services" section is there.
        // Asserts there's at least 1 item in the list.
        $$("#service-areas .container .thumbnail").shouldHave(sizeGreaterThan(0));

        // Verifies the company button is there.
        $$("#xlt-background .container p.lead > a.btn-primary").shouldHave(sizeGreaterThan(0));
    }
}
