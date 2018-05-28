package com.xceptance.neodymium.module.statement.testdata;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <b>Do not use this annotation</b>. This annoation is just the wrapper for repeated {@link DataSet} annotations.
 * 
 * @author m.kaufmann
 */
@Retention(RUNTIME)
@Target(
    {
        TYPE, METHOD
    })
public @interface DataSets
{
    DataSet[] value();
}
