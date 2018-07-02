package com.xceptance.neodymium.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.xceptance.neodymium.NeodymiumWebDriverListener;

public class NeodymiumWebDriverTestListener extends NeodymiumWebDriverListener
{
    public int impliciteWaitCount = 0;

    @Override
    public void afterNavigateTo(String url, WebDriver driver)
    {
        super.afterNavigateTo(url, driver);
        impliciteWaitCount++;
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver)
    {
        super.afterClickOn(element, driver);
        impliciteWaitCount++;
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend)
    {
        super.afterChangeValueOf(element, driver, keysToSend);
        impliciteWaitCount++;
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver)
    {
        super.beforeFindBy(by, element, driver);
        impliciteWaitCount++;
    }
}
