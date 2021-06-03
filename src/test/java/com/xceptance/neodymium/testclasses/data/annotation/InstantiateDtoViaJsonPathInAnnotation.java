package com.xceptance.neodymium.testclasses.data.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.testdata.Data;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
public class InstantiateDtoViaJsonPathInAnnotation
{
    @Data("$.user")
    private User user;

    @Data("$.user.contacts.friends[2]")
    private User friend;

    @Test
    public void test1()
    {
        Assert.assertEquals("john" + DataUtils.asString("testId") + "@varmail.de", user.getEmail());
        Assert.assertEquals("neodymium" + DataUtils.asString("testId"), user.getPassword());
    }

    @Test
    public void test2()
    {
        Assert.assertEquals("friend.of.john" + DataUtils.asString("testId") + "@varmail.de", friend.getEmail());
        Assert.assertEquals("neodymium_friend_of_john" + DataUtils.asString("testId"), friend.getPassword());
    }
}
