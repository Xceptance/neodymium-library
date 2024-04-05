package com.xceptance.neodymium;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Works like the opposite of @Ignore. If tests in a class are annotated with @WorkInProgress,
 * only those tests will be executed. All other tests of the class, not being annotated with 
 * @WorkInProgress, get skipped. The @WorkInProgress annotation is per default disabled but can
 * be enabled via neodymium property "workInProgress".
 */
@Retention(RUNTIME)
@Target(
{
  METHOD
})
public @interface WorkInProgress
{
}
