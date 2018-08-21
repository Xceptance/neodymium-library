package com.xceptance.neodymium.util;

import static com.codeborne.selenide.Selenide.$;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ex.ElementShould;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_headless")
public class SelenideAddonsTest
{
    @Test
    public void testMatchesValueCondition()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();
        $("#search-container .search-field").val("searchphrase").submit();

        $("#content .search-field").should(SelenideAddons.matchesValue("earchphras"));
    }

    @Test
    public void testMatchValueCondition()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();
        $("#search-container .search-field").val("searchphrase").submit();

        $("#content .search-field").should(SelenideAddons.matchValue("^s.a.c.p.r.s.$"));
        $("#content .search-field").should(SelenideAddons.matchValue("\\D+"));
    }

    @Test(expected = ElementShould.class)
    public void testMatchValueConditionError()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();
        $("#search-container .search-field").val("searchphrase").submit();

        $("#content .search-field").should(SelenideAddons.matchValue("\\d+"));
    }
}
