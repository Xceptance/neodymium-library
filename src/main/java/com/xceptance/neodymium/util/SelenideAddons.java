package com.xceptance.neodymium.util;

import static com.codeborne.selenide.Selenide.$$;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.Html;
import com.codeborne.selenide.impl.WebElementsCollectionWrapper;

/**
 * Additional helpers for limits chained lookup in Selenide. Contribute that later back to Selenide if it proves to
 * work, so it can become API or better fully integrated so we don't need that workaround concept.
 */
public class SelenideAddons
{
    /**
     * Returns an supplier that will return exactly one result if any. It will return an element that is found by
     * parentSelector and has a result for subElementSelector. It does NOT return the subelements, it is meant to be a
     * workaround for poor CSS where the parent is only recognizable by looking at its children, but we don't need the
     * children. Important, this is meant to be lazy so don't call get() when you setup a field or so, only when you
     * really need the element. It reselects all the time!
     *
     * @param parentSelector
     *            The selector that is used to find subElementSelector
     * @param subElementSelector
     *            The selector that is shall have the element selected by parentSelector
     * @return an supplier that will return the result later
     */
    public static Supplier<SelenideElement> parentBySubElement(final By parentSelector, final By subElementSelector)
    {
        return new Supplier<SelenideElement>()
        {
            @Override
            public SelenideElement get()
            {
                return $$(parentSelector).stream().filter(e -> {
                    return e.$(subElementSelector).exists();
                }).findFirst().get();
            };
        };
    }

    /**
     * Returns an supplier that will return all results if any. It will return elements that are found by parentSelector
     * and have a result for subElementSelector. It does NOT return the subelements, it is meant to be a workaround for
     * poor CSS where the parent is only recognizable by looking at its children, but we don't need the children.
     * Important, this is meant to be lazy so don't call get() when you setup a field or so, only when you really need
     * the element. It reselects all the time!
     *
     * @param parentSelector
     *            The selector that is used to find subElementSelector
     * @param subElementSelector
     *            The selector that is shall have the element selected by parentSelector
     * @return an supplier that will return the result later
     */
    public static Supplier<ElementsCollection> parentsBySubElement(final By parentSelector, final By subElementSelector)
    {
        return new Supplier<ElementsCollection>()
        {
            @Override
            public ElementsCollection get()
            {
                List<SelenideElement> list = $$(parentSelector).stream().filter(e -> {
                    return e.$(subElementSelector).exists();
                }).collect(Collectors.toList());
                return new ElementsCollection(new WebElementsCollectionWrapper(WebDriverRunner.driver(), list));
            };
        };
    }

    /**
     * Returns an supplier that will return all results if any. It will return elements that are found by parentSelector
     * and have a no result for subElementSelector. It does NOT return the subelements, it is meant to be a workaround
     * for poor CSS where the parent is only recognizable by looking at its children, but we don't need the children.
     * Important, this is meant to be lazy so don't call get() when you setup a field or so, only when you really need
     * the element. It reselects all the time!
     *
     * @param parentSelector
     *            The selector that is used to find subElementSelector
     * @param subElementSelector
     *            The selector that shall not be contained by the parentSelector
     * @return an supplier that will return the result later
     */
    public static Supplier<ElementsCollection> parentsWithoutSubElement(final By parentSelector, final By subElementSelector)
    {
        return new Supplier<ElementsCollection>()
        {
            @Override
            public ElementsCollection get()
            {
                List<SelenideElement> list = $$(parentSelector).stream().filter(e -> {
                    return !e.$(subElementSelector).exists();
                }).collect(Collectors.toList());
                return new ElementsCollection(new WebElementsCollectionWrapper(WebDriverRunner.driver(), list));
            };
        };
    }

    /**
     * Re-execute the entire code when a stale element exception comes up
     *
     * @param code
     *            the code to run
     * @return the element of the execution or any exception that might bubble up
     */
    public static SelenideElement $safe(final Supplier<SelenideElement> code)
    {
        int retryCounter = Neodymium.configuration().staleElementRetryCount();

        while (retryCounter >= 0)
        {
            try
            {
                return code.get();
            }
            catch (final StaleElementReferenceException e)
            {
                retryCounter--;

                if (retryCounter < 0)
                {
                    // fail
                    throw e;
                }

                // wait
                try
                {
                    Thread.sleep(Neodymium.configuration().staleElementRetryTimeout());
                }
                catch (final InterruptedException e1)
                {
                }
            }
        }

        // never get here
        return null;
    }

    /**
     * The missing regular expression condition for value attributes.<br>
     * <br>
     * <p>
     * Sample: <code>$("input").waitWhile(matchesValue("foo"), 12000)</code>
     * </p>
     * 
     * @param text
     *            The text that should be contained within the value attribute
     * @return a Selenide {@link Condition}
     * @see #matchValue(String)
     */
    public static Condition matchesValue(String text)
    {
        return matchValue(text);
    }

    /**
     * The missing regular expression condition for value attributes.<br>
     * <br>
     * <p>
     * Sample: Assert that given element's value attribute matches given regular expression
     * <code>$("input").should(matchValue("Hello\s*John"))</code>
     * </p>
     *
     * @param regex
     *            e.g. Kicked.*Chuck Norris - in this case ".*" can contain any characters including spaces, tabs, CR
     *            etc.
     * @return a Selenide {@link Condition}
     */
    public static Condition matchValue(final String regex)
    {
        return matchesAttribute("value", regex);
    }

    /**
     * The missing regular expression condition for attributes.<br>
     * <br>
     * <p>
     * Sample: <code>$("input").waitWhile(matchesValue("foo"), 12000)</code>
     * </p>
     * 
     * @param attributeName
     *            The name of the attribute that should contain the text
     * @param text
     *            The text that should be contained within the attribute
     * @return a Selenide {@link Condition}
     * @see #matchAttribute(String, String)
     */
    public static Condition matchesAttribute(String attributeName, String text)
    {
        return matchAttribute(attributeName, text);
    }

    /**
     * The missing regular expression condition for attributes.<br>
     * <br>
     * <p>
     * Sample: Assert that given element's value attribute matches given regular expression
     * <code>$("input").should(matchValue("Hello\s*John"))</code>
     * </p>
     *
     * @param attributeName
     *            The name of the attribute that should be matched with the regex
     * @param regex
     *            e.g. Kicked.*Chuck Norris - in this case ".*" can contain any characters including spaces, tabs, CR
     *            etc.
     * @return a Selenide {@link Condition}
     */
    public static Condition matchAttribute(final String attributeName, final String regex)
    {
        return new Condition("match " + attributeName)
        {
            @Override
            public String toString()
            {
                return name + " '" + regex + '\'';
            }

            @Override
            public boolean apply(Driver driver, WebElement element)
            {
                return Html.text.matches(getAttributeValue(element, attributeName), regex);
            }
        };
    }

    private static String getAttributeValue(WebElement element, String attributeName)
    {
        String attr = element.getAttribute(attributeName);
        return attr == null ? "" : attr;
    }

    /**
     * The missing wrapper to generate screenshots and save the html source code if a jUnit assertion fails.<br>
     * <br>
     * <p>
     * Sample: Assert that page title is correct and dump the page source and a screenshot in case of a mismatch
     * <code> wrapAssertionError(()-&gt;{Assert.assertEquals("MyPageTitle", Selenide.title());});</code>
     * </p>
     * 
     * @param runnable
     *            The lambda containing an assertion
     */
    public static void wrapAssertionError(final Runnable runnable)
    {
        try
        {
            runnable.run();
        }
        catch (AssertionError e)
        {
            throw UIAssertionError.wrap(WebDriverRunner.driver(), e, System.currentTimeMillis());
        }
    }
}
