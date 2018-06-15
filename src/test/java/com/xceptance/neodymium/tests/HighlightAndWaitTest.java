package com.xceptance.neodymium.tests;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.util.Context;
import com.xceptance.neodymium.util.HighlightAndWait;

@RunWith(NeodymiumRunner.class)
public class HighlightAndWaitTest
{
    @Test
    @Browser("Chrome_1024x768")
    public void test1() throws Exception
    {
        Context.get().configuration.setProperty("implicitWait", "1000");
        Selenide.open("https://blog.xceptance.com/");
        HighlightAndWait._setUp();

        List<WebElement> list = $("body").findElements(By.cssSelector("#masthead"));
        HighlightAndWait._highlightElements(list, Context.get().driver);
        $(".neo-highlight-box").shouldBe(visible);

        HighlightAndWait._resetHighlight();
        $(".neo-highlight-box").shouldNotBe(visible);
    }
}
