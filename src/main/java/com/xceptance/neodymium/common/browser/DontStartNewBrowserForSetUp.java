package com.xceptance.neodymium.common.browser;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * By default the methods annotated with @{@code Before} in JUnit4 and with @{@code BeforeEach} in Junit5 are executed
 * in different browser to keep setup isolated from the actual test. It might also be useful to execute set up in
 * different type of browser (for this just annotate @Before / @BeforeEach method with @{@code Browser} tag. This
 * annotation allows to suppress this behavior. The annotation can be used on class level to make all before methods to
 * be executed in the same browser the test will be. It's also possible to annotate a single @Before / @BeforeEach
 * method with annotation to suppress staring different browser exactly for this method
 */
@Retention(RUNTIME)
@Target(
{
  TYPE, METHOD
})
public @interface DontStartNewBrowserForSetUp
{

}
