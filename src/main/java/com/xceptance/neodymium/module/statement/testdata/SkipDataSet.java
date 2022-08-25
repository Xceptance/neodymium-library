package com.xceptance.neodymium.module.statement.testdata;

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
public @interface SkipDataSet
{
    int value() default 0;

    String id() default "";
}
