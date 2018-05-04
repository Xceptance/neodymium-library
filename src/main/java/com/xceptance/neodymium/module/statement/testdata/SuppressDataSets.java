package com.xceptance.neodymium.module.statement.testdata;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(
    {
        TYPE, METHOD
    })
public @interface SuppressDataSets
{

}
