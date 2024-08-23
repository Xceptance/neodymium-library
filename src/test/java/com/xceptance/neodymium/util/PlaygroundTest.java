package com.xceptance.neodymium.util;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.junit5.NeodymiumTest;

@Browser("Chrome_headless")
public class PlaygroundTest
{
    @NeodymiumTest
    public void test()
    {
        Neodymium.configuration().setProperty("neodymium.debugUtils.highlight.duration", "1000");

        Selenide.open("https://blog.xceptance.com/");
        $("body").find("#content").shouldBe(Condition.visible);
        Selenide.$$("article").shouldHave(CollectionCondition.sizeGreaterThan(0));
        $("body").find("#content").findAll("article").shouldHave(CollectionCondition.sizeGreaterThan(0));
        Selenide.$$("article").shouldHave(CollectionCondition.sizeGreaterThan(0));
    }
}
