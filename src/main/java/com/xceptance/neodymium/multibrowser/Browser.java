package com.xceptance.neodymium.multibrowser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used in context of XLT script test cases.
 * <p>
 * Annotate a class with {@link Browser} and add as annotation value a list of test targets. These targets refer to
 * browserprofiles (browsertag) that are configured in browser.properties located in config folder.
 * 
 * @author m.kaufmann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Browser
{
    String[] value();
}
