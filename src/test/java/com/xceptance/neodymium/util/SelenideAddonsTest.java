package com.xceptance.neodymium.util;

import static com.codeborne.selenide.Selenide.$;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.UIAssertionError;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_headless")
public class SelenideAddonsTest
{
    @Test
    public void testMatchesAttributeCondition()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();

        $("#search-container .search-field").should(SelenideAddons.matchesAttribute("placeholder", "Search"));
    }

    @Test
    public void testMatchAttributeCondition()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();

        $("#search-container .search-field").should(SelenideAddons.matchAttribute("placeholder", "^S.a.c.\\s…"));
        $("#search-container .search-field").should(SelenideAddons.matchAttribute("placeholder", "\\D+"));
    }

    @Test(expected = ElementShould.class)
    public void testMatchAttributeConditionError()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();

        $("#search-container .search-field").should(SelenideAddons.matchAttribute("placeholder", "\\d+"));
    }

    @Test(expected = ElementShould.class)
    public void testMatchAttributeConditionErrorMissingAttribute()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();

        $("#search-container .search-field").should(SelenideAddons.matchAttribute("foo", "bar"));
    }

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

    @Test()
    public void testWrapAssertion()
    {
        Selenide.open("https://blog.xceptance.com/");
        SelenideAddons.wrapAssertionError(() -> {
            Assert.assertEquals("Passionate Testing | Xceptance Blog", Selenide.title());
        });
    }

    @Test(expected = UIAssertionError.class)
    public void testWrapAssertionError()
    {
        Selenide.open("https://blog.xceptance.com/");
        SelenideAddons.wrapAssertionError(() -> {
            Assert.assertEquals("MyPageTitle", Selenide.title());
        });
    }
}
