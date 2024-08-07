package com.xceptance.neodymium.common.browser;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(
    {
        TYPE, METHOD
    })
public @interface SuppressBrowsers
{

}
