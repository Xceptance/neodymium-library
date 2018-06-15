package com.xceptance.neodymium.util;

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

@RunWith(NeodymiumRunner.class)
public class HighlightAndWaitTest
{
    @Test
    @Browser("Chrome_1024x768")
    public void test() throws Exception
    {
        Context.get().configuration.setProperty("implicitWait", "1000");
        Selenide.open("https://blog.xceptance.com/");
        HighlightAndWait.setUp();

        List<WebElement> list = $("body").findElements(By.cssSelector("#masthead"));
        HighlightAndWait.highlightElements(list, Context.get().driver);
        $(".neo-highlight-box").shouldBe(visible);

        HighlightAndWait.resetHighlight();
        $(".neo-highlight-box").shouldNotBe(visible);
    }
}
