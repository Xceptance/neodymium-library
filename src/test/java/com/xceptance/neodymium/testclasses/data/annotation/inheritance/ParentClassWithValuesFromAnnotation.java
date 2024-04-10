package com.xceptance.neodymium.testclasses.data.annotation.inheritance;

import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataItem;

@RunWith(NeodymiumRunner.class)
public abstract class ParentClassWithValuesFromAnnotation
{
    @DataItem
    protected String name;

    @DataItem
    protected String testId;
}
