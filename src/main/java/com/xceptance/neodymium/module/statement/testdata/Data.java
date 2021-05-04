package com.xceptance.neodymium.module.statement.testdata;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(
{
  ElementType.FIELD
})
public @interface Data
{
    String value() default "";
}
