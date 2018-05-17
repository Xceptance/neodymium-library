package com.xceptance.neodymium.module.statement.browser.multibrowser;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(
    {
        TYPE, METHOD
    })
@Inherited
public @interface Browsers
{
    Browser[] value();
}
