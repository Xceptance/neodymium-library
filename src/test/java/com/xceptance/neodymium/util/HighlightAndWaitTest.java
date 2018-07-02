package com.xceptance.neodymium.util;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_headless")
public class HighlightAndWaitTest
{
    @Test
    public void testHighlighting()
    {
        Context.get().configuration.setProperty("implicitWait", "1000");
        Selenide.open("https://blog.xceptance.com/");
        HighlightAndWait.injectJavaScript();

        final List<WebElement> list = $("body").findElements(By.cssSelector("#masthead"));
        HighlightAndWait.highlightElements(list, Context.get().driver);
        $(".neodymium-highlight-box").shouldBe(visible);

        HighlightAndWait.resetAllHighlight();
        $(".neodymium-highlight-box").shouldNot(exist);

        final List<WebElement> list2 = $("body").findElements(By.cssSelector("#content article"));
        HighlightAndWait.highlightElements(list2, Context.get().driver);
        $$(".neodymium-highlight-box").shouldHaveSize(10);

        HighlightAndWait.resetAllHighlight();
        $(".neodymium-highlight-box").shouldNot(exist);
    }

    @Test
    public void testHighlightingWithoutImplicitWaitTime()
    {
        Selenide.open("https://blog.xceptance.com/");
        HighlightAndWait.injectJavaScript();

        final List<WebElement> list = $("body").findElements(By.cssSelector("#masthead"));
        HighlightAndWait.highlightElements(list, Context.get().driver);
        $(".neodymium-highlight-box").shouldBe(visible);

        HighlightAndWait.resetAllHighlight();
        $(".neodymium-highlight-box").shouldNot(exist);
    }

    @Test
    public void testWaiting()
    {
        NeodymiumWebDriverTestListener eventListener = new NeodymiumWebDriverTestListener();
        ((EventFiringWebDriver) Context.get().driver).register(eventListener);

        final long waitingTime = 500;

        Context.get().configuration.setProperty("implicitWait", Long.toString(waitingTime));
        Context.get().configuration.setProperty("highlightSelectors", "true");

        // one wait due to navigation
        Selenide.open("https://blog.xceptance.com/");
        Assert.assertEquals(1, eventListener.impliciteWaitCount);

        // one wait due to find
        $("body #masthead").should(exist);
        Assert.assertEquals(2, eventListener.impliciteWaitCount);

        // two waits due to chain finding
        $("body").findElements(By.cssSelector("#content article"));
        Assert.assertEquals(4, eventListener.impliciteWaitCount);

        // two waits due to find and click
        $("#text-3 h1").click();
        Assert.assertEquals(6, eventListener.impliciteWaitCount);

        // additional two waits due to find and click
        $("#masthead .search-toggle").click();
        Assert.assertEquals(8, eventListener.impliciteWaitCount);

        // three waits due to find and change value (consumes 2 waits)
        $("#search-container .search-form input.search-field").val("abc");
        Assert.assertEquals(11, eventListener.impliciteWaitCount);

        // two waits due to find and press enter
        $("#search-container .search-form input.search-field").pressEnter();
        Assert.assertEquals(13, eventListener.impliciteWaitCount);
    }

    @Test
    public void testIFrames() throws Exception
    {
        Context.get().configuration.setProperty("highlightSelectors", "true");
        Context.get().configuration.setProperty("implicitWait", "1000");

        Selenide.open("https://www.w3schools.com/tags/tryit.asp?filename=tryhtml_select");
        Context.get().driver.switchTo().frame("iframeResult");

        SelenideElement body = $("body");
        body.click();

        final List<WebElement> list = $("body").findElements(By.cssSelector("select"));

        Context.get().configuration.setProperty("highlightSelectors", "false");
        HighlightAndWait.highlightElements(list, Context.get().driver);
        $(".neodymium-highlight-box").shouldBe(visible);

        HighlightAndWait.resetAllHighlight();
        $(".neodymium-highlight-box").shouldNot(exist);
    }
}
