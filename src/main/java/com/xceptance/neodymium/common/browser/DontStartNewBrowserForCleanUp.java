package com.xceptance.neodymium.common.browser;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * By default the methods annotated with @{@code After} in JUnit4 and with @{@code AfterEach} in Junit5 are executed in
 * different browser to ensure cleanup if browser where the test was executed crashed. This annotation allows to
 * suppress this behavior. The annotation can be used on class level to make all after methods to be executed in the
 * same browser the test was. It's also possible to annotate a single @After / @AfterEach method with annotation to
 * suppress staring different browser exactly for this method
 */
@Retention(RUNTIME)
@Target(
{
  TYPE, METHOD
})
public @interface DontStartNewBrowserForCleanUp
{
}
