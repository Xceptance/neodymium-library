package com.xceptance.neodymium.common.browser;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * By default the methods annotated with @{@code After} in JUnit4 and with @{@code AfterEach} in Junit5 are executed in
 * the same browser. If it'required to start new browser for one of after methods, please, annotate it
 * with @StartNewBrowserForCleanUp. In case all after methods need to be executed in separate browser each, annotate the
 * whole class with @StartNewBrowserForCleanUp. If all after methods, except for one require a new browser, annotate the
 * class with @StartNewBrowserForCleanUp and the one method that doesn't need new browser with @SuppressBrowsers
 */
@Retention(RUNTIME)
@Target(
{
  TYPE, METHOD
})
public @interface StartNewBrowserForCleanUp
{
}
