package com.xceptance.xrunner.groups;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(
    {
        METHOD
    })
@Repeatable(TestGroups.class)
public @interface TestGroup
{
    Class<?> group() default DefaultGroup.class;

    int ordinal() default -1;
}
