package com.xceptance.neodymium.module.statement.browser;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(
{
  TYPE, METHOD
})
/**
 * This annotation defines whether or not the browser should stay open if the annotated test fails and is set to "false" per default.
 * It can be used for test methods and test classes. 
 * If it's used for both, then the method level annotation overrides the class level annotation.
 * Also, if used, the method level annotation and the class level annotation both override the corresponding neodymium configuration property (neodymium.webDriver.keepBrowserOpenOnFailure).
 */
public @interface KeepBrowserOpen
{
    boolean onlyOnFailure() default false;
}
