package com.xceptance.neodymium.testclasses.data.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.DataItem;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
public class InstantiateDtoViaJsonPathInAnnotation extends InstantiateFieldViaAnnotation
{
    @DataItem("$.user")
    private User user;

    @DataItem("$.user.contacts.friends[2]")
    private User friend;

    @Test
    public void test1()
    {
        Assert.assertEquals("john" + DataUtils.asString("testId") + "@varmail.de", user.getEmail());
        Assert.assertEquals("neodymium" + testId, user.getPassword());
        validateDataItemDefaultValues();
    }

    @Test
    public void test2()
    {
        Assert.assertEquals("friend.of.john" + DataUtils.asString("testId") + "@varmail.de", friend.getEmail());
        Assert.assertEquals("neodymium_friend_of_john" + testId, friend.getPassword());
        validateDataItemDefaultValues();
    }
}
