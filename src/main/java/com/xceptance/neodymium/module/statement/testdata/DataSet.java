package com.xceptance.neodymium.module.statement.testdata;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation can be used to limit override data set execution for an entire class or at the same time for a single
 * method.
 * <p>
 * The <b>value</b> defines the index of the data set that has to be force used for the class/method.
 * <p>
 * Default is -1 which will not have any effect on execution.
 * <p>
 * A value of 0 indicates that no data sets at all shall be used.
 * <p>
 * Any number above zero will enforce execution with only that data set. First data set would be equal to 1 and so on.
 * 
 * @author m.kaufmann
 */
@Retention(RUNTIME)
@Target(
    {
        TYPE, METHOD
    })
public @interface Testdata
{
    int value() default -1;
}
