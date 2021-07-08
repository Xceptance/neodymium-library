package com.xceptance.neodymium.testclasses.data.annotation.inheritance;

import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataItem;
import com.xceptance.neodymium.testclasses.data.annotation.User;

@RunWith(NeodymiumRunner.class)
public abstract class ParentClassWithDtoFromAnnotation
{
    @DataItem
    protected User user;
}
