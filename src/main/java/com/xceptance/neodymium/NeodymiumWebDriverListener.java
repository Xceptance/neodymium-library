package com.xceptance.neodymium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import com.xceptance.neodymium.util.HighlightAndWait;

public class NeodymiumWebDriverListener extends AbstractWebDriverEventListener
{
    @Override
    public void afterNavigateTo(String url, WebDriver driver)
    {
        HighlightAndWait.implicitWait();
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver)
    {
        HighlightAndWait.implicitWait();
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend)
    {
        HighlightAndWait.implicitWait();
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver)
    {
        HighlightAndWait.injectHighlightingJs();
        HighlightAndWait.highlightAllElements(by, driver);
    }
}
