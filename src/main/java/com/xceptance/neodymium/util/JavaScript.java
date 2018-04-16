package com.xceptance.neodymium.util;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.xceptance.neodymium.util.Context;

/**
 * Source: http://www.swtestacademy.com/selenium-wait-javascript-angular-ajax/ With a lot of modifications
 *
 * @author rschwietzke
 */
public class JavaScript
{
    // Wait Until JQuery and JS Ready
    public static void waitForReady()
    {
        // Wait for jQuery to load
        final Function<WebDriver, Boolean> jQueryLoad = driver -> {
            return ((Boolean) ((JavascriptExecutor) driver).executeScript("return !!window.jQuery && window.jQuery.active == 0"));
        };

        // dom ready
        final Function<WebDriver, Boolean> readyState = driver -> {
            return ((Boolean) ((JavascriptExecutor) driver).executeScript("return document.readyState == 'complete'"));
        };

        // no loading animation
        final Function<WebDriver, Boolean> loadingAnimation = driver -> {
            return !$(Context.get().configuration.javascriptLoadingAnimationSelector()).is(exist);
        };

        until(Arrays.asList(loadingAnimation, jQueryLoad, readyState));
    }

    /**
     * Wait until all conditions are true. One wait statement, so the timeouts don't sum up
     *
     * @param conditions
     *            a list of conditions to verify
     */
    public static void until(final List<Function<WebDriver, Boolean>> conditions)
    {
        final long timeout = Context.get().configuration.javaScriptTimeout();
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
                    final boolean result = condition.apply(Context.get().driver);
                    if (result)
                    {
                        if (Context.get().configuration.javaScriptMustHaveBeenActive() && wasActive)
                        {
                            endEarly = true;
                            continue;
                        }
                        else if (Context.get().configuration.javaScriptMustHaveBeenActive() == false)
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

                sleep(Context.get().configuration.javaScriptPoolInterval());

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