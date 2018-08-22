package com.xceptance.neodymium.testclasses.allure;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_headless")
public class AllureSelenideListenerIsActiveForJava
{
    @Test
    public void testWaitingAnimationSelectorUnconfigured()
    {
        Assert.assertTrue(" AllureSelenide listener is not attached", SelenideLogger.hasListener(NeodymiumRunner.LISTENER_NAME));
    }
}
