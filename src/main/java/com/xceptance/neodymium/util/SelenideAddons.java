package com.xceptance.neodymium.util;

import static com.codeborne.selenide.Selenide.$$;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.codeborne.selenide.AssertionMode;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.Html;
import com.codeborne.selenide.impl.WebElementsCollectionWrapper;
import com.codeborne.selenide.logevents.SelenideLog;
import com.codeborne.selenide.logevents.SelenideLogger;

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
     * Executes the given code at least once but potentially multiple times as long as a
     * {@link StaleElementReferenceException} occurs.
     * <p>
     * Attention: Since the SelenideElement class implements the InvocationHandler interface you have to make sure that
     * the element is retrieved in order to provoke a StaleElementReferenceException. You can do this by calling a
     * should function that uses a condition.
     * </p>
     * <p>
     * The following settings can be configured within the Neodymium configuration to tune the retry behavior:
     * </p>
     * <ul>
     * <li>neodymium.selenideAddons.staleElement.retry.count (default 3 retries)</li>
     * <li>neodymium.selenideAddons.staleElement.retry.timeout (default 500ms pause between retries)</li>
     * </ul>
     * <p>
     * <b>Example:</b>
     * </p>
     * 
     * <pre>
     * SelenideAddons.$safe(() -&gt; {
     *     return $("selector").should(exist);
     * });
     * </pre>
     *
     * @param code
     *            the code to run
     * @return the element of the execution or any exception that might bubble up
     */
    public static SelenideElement $safe(final Supplier<SelenideElement> code)
    {
        final int maxRetryCount = Neodymium.configuration().staleElementRetryCount();
        int retryCounter = 0;

        while (retryCounter <= maxRetryCount)
        {
            try
            {
                return code.get();
            }
            catch (final Throwable t)
            {
                if (isThrowableCausedBy(t, StaleElementReferenceException.class))
                {
                    retryCounter++;
                    if (retryCounter > maxRetryCount)
                    {
                        // fail
                        throw t;
                    }
                    else
                    {
                        AllureAddons.addToReport("StaleElementReferenceException catched times: \"" + retryCounter + "\".", retryCounter);
                        Selenide.sleep(Neodymium.configuration().staleElementRetryTimeout());
                    }
                }
                else
                {
                    // not the kind of error we are looking for
                    throw t;
                }
            }
        }

        // never get here
        return null;
    }

    /**
     * Executes the given code at least once but potentially multiple times as long as a
     * {@link StaleElementReferenceException} occurs.
     * <p>
     * The following settings can be configured within the Neodymium configuration to tune the retry behavior:
     * </p>
     * <ul>
     * <li>neodymium.selenideAddons.staleElement.retry.count (default 3 retries)</li>
     * <li>neodymium.selenideAddons.staleElement.retry.timeout (default 500ms pause between retries)</li>
     * </ul>
     * <p>
     * <b>Example:</b>
     * </p>
     * 
     * <pre>
     * SelenideAddons.$safe(() -&gt; {
     *     $("selectorOne").find("selectorTwo").shouldBe(visible);
     * });
     * </pre>
     * 
     * @param code
     *            the code to run
     */
    public static void $safe(final Runnable code)
    {
        final int maxRetryCount = Neodymium.configuration().staleElementRetryCount();
        int retryCounter = 0;

        while (retryCounter <= maxRetryCount)
        {
            try
            {
                code.run();
                break;
            }
            catch (final Throwable t)
            {
                if (isThrowableCausedBy(t, StaleElementReferenceException.class))
                {
                    retryCounter++;
                    if (retryCounter > maxRetryCount)
                    {
                        // fail
                        throw t;
                    }
                    else
                    {
                        AllureAddons.addToReport("StaleElementReferenceException catched times: \"" + retryCounter + "\".", retryCounter);
                        Selenide.sleep(Neodymium.configuration().staleElementRetryTimeout());
                    }
                }
                else
                {
                    // not the kind of error we are looking for
                    throw t;
                }
            }
        }
    }

    private static boolean isThrowableCausedBy(final Throwable throwable, Class<? extends Throwable> clazz)
    {
        Throwable t = throwable;
        while (t != null)
        {
            if (clazz.isInstance(t))
            {
                return true;
            }
            t = t.getCause();
        }
        return false;
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
                return this.getName() + " '" + regex + '\'';
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
            Driver driver = WebDriverRunner.driver();
            String message = "No error message provided by the Assertion.";
            if (StringUtils.isNotBlank(e.getMessage()))
            {
                message = e.getMessage();
            }
            else
            {
                AssertionError wrapper = new AssertionError(message, e.getCause());
                wrapper.setStackTrace(e.getStackTrace());
                e = wrapper;
            }
            SelenideLogger.commitStep(new SelenideLog("Assertion error", message), e);
            if (!driver.config().assertionMode().equals(AssertionMode.SOFT))
            {
                throw UIAssertionError.wrap(driver, e, 0);
            }
        }
    }

    /**
     * Drag and drop a horizontal or vertical slider to a given position. The position will be set by the user. It drags
     * the slider and moves it to a specific position of the respective slider.
     * 
     * @param slider
     *            The selector of the slider to drag and drop
     * @param moveOffset
     *            The offset of the horizontal or vertical movement
     * @param textContainer
     *            The selector of the text container of the slider
     * @param moveUntil
     *            The value of the slider until the slider will be moved.
     * @param direction
     *            Decision about a horizontal or vertical movement.
     *            <p>
     *            If <b>direction</b> boolean value is <i>true</i> - the slider will move vertical.
     *            <p>
     *            If <b>direction</b> boolean value is <i>false</i> - the slider will move horizontal.
     */

    public void dragAndDropUntilText(SelenideElement slider, int moveOffset, SelenideElement textContainer, String moveUntil, boolean direction)
    {
        if (direction)
        {
            dragAndDropVerticalUntilText(slider, moveOffset, textContainer, moveUntil);
        }
        else
        {
            dragAndDropHorizontalUntilText(slider, moveOffset, textContainer, moveUntil);
        }
    }

    /**
     * Drag and drop a horizontal slider to a given position. The position will be set by the user. It drags the slider
     * and moves it to a specific position of the respective slider.
     * 
     * @param slider
     *            The selector of the slider to drag and drop
     * @param horizontalMoveOffset
     *            The offset of the horizontal movement
     * @param textContainer
     *            The selector of the text container of the slider
     * @param moveUntil
     *            The value of the slider until the slider will be moved.
     */
    private void dragAndDropHorizontalUntilText(SelenideElement slider, int horizontalMoveOffset, SelenideElement textContainer, String moveUntil)
    {
        Actions moveSlider = new Actions(Neodymium.getDriver());

        int counter = 0;
        while (!textContainer.has(Condition.text(moveUntil)))
        {
            if (counter > 23)
            {
                SelenideAddons.wrapAssertionError(() -> {
                    Assert.assertTrue("CircutBreaker: Was not able to interact with the slider", false);
                });
            }
            Action action = moveSlider.dragAndDropBy(slider.getWrappedElement(), horizontalMoveOffset, 0).build();
            action.perform();
            Selenide.sleep(3000);
            counter++;
        }
    }

    /**
     * Drag and drop a vertical slider to a given position. The position will be set by the user. It drags the slider
     * and moves it to a specific position of the respective slider.
     * 
     * @param slider
     *            The selector of the slider to drag and drop
     * @param verticalMoveOffset
     *            The offset of the vertical movement
     * @param textContainer
     *            The selector of the text container of the slider
     * @param moveUntil
     *            The value of the slider until the slider will be moved.
     */
    private void dragAndDropVerticalUntilText(SelenideElement slider, int verticalMoveOffset, SelenideElement textContainer, String moveUntil)
    {
        Actions moveSlider = new Actions(Neodymium.getDriver());

        int counter = 0;
        while (!textContainer.has(Condition.text(moveUntil)))
        {
            if (counter > 23)
            {
                SelenideAddons.wrapAssertionError(() -> {
                    Assert.assertTrue("CircutBreaker: Was not able to interact with the slider", false);
                });
            }
            Action action = moveSlider.dragAndDropBy(slider.getWrappedElement(), 0, verticalMoveOffset).build();
            action.perform();
            Selenide.sleep(3000);
            counter++;
        }
    }
}