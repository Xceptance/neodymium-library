package com.xceptance.neodymium.module.statement.browser;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(
{
  TYPE
})
public @interface KeepBrowserOpen
{
    boolean onlyOnFailure() default false;
}
