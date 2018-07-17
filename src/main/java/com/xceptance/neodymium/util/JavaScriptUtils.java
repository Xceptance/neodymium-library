package com.xceptance.neodymium.util;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

/**
 * Source: http://www.swtestacademy.com/selenium-wait-javascript-angular-ajax/ With a lot of modifications
 *
 * @author rschwietzke
 */
public class JavaScriptUtils
{
    // Wait Until JQuery and JS Ready
    public static void waitForReady()
    {
        List<Function<WebDriver, Boolean>> conditionsToWaitFor = new LinkedList<Function<WebDriver, Boolean>>();

        // Wait for jQuery to load
        conditionsToWaitFor.add(driver -> {
            return ((Boolean) ((JavascriptExecutor) driver).executeScript("return !!window.jQuery && window.jQuery.active == 0"));
        });

        // dom ready
        conditionsToWaitFor.add(driver -> {
            return ((Boolean) ((JavascriptExecutor) driver).executeScript("return document.readyState == 'complete'"));
        });

        if (Neodymium.configuration().javascriptLoadingAnimationSelector() != null)
        {
            // no loading animation
            conditionsToWaitFor.add(driver -> {
                return !$(Neodymium.configuration().javascriptLoadingAnimationSelector()).is(exist);
            });
        }

        until(conditionsToWaitFor);
    }

    /**
     * Wait until all conditions are true. One wait statement, so the timeouts don't sum up
     *
     * @param conditions
     *            a list of conditions to verify
     */
    public static void until(final List<Function<WebDriver, Boolean>> conditions)
    {
        final long timeout = Neodymium.configuration().javaScriptTimeout();
        final long start = System.currentTimeMillis();

        // keeps track if we have seen a condition at least once being false aka continue
        // case was not true, so for instance the Jquery was still active
        boolean wasActive = false;

        // loop if still is time
        for (final Function<WebDriver, Boolean> condition : conditions)
        {
            boolean endEarly = false;

            while (!endEarly && System.currentTimeMillis() - start < timeout)
            {
                try
                {
                    final boolean result = condition.apply(Neodymium.getDriver());
                    if (result)
                    {
                        if (Neodymium.configuration().javaScriptMustHaveBeenActive() && wasActive)
                        {
                            endEarly = true;
                            continue;
                        }
                        else if (Neodymium.configuration().javaScriptMustHaveBeenActive() == false)
                        {
                            endEarly = true;
                            continue;
                        }
                    }
                    else
                    {
                        wasActive = true;
                    }
                }
                catch (final StaleElementReferenceException | NoSuchElementException e)
                {
                    // we might have to limit the exception range
                }

                sleep(Neodymium.configuration().javaScriptPollingInterval());

                // time is up?
                if (System.currentTimeMillis() - start >= timeout)
                {
                    return;
                }
            }
        }
    }

    private static void sleep(final int msec)
    {
        // wait
        try
        {
            Thread.sleep(msec);
        }
        catch (final InterruptedException e1)
        {
            return; // leave immediately
        }
    }
}
