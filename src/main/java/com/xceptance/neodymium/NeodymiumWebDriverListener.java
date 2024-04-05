package com.xceptance.neodymium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import com.xceptance.neodymium.util.DebugUtils;
import com.xceptance.neodymium.util.Neodymium;

public class NeodymiumWebDriverListener implements WebDriverListener
{
    @Override
    public void beforeFindElement(WebDriver driver, By by)
    {
        DebugUtils.injectHighlightingJs();
        DebugUtils.highlightAllElements(by, driver);
    }

    @Override
    public void beforeFindElements(WebDriver driver, By by)
    {
        DebugUtils.injectHighlightingJs();
        DebugUtils.highlightAllElements(by, driver);
    }

    @Override
    public void beforeFindElement(WebElement element, By locator)
    {
        DebugUtils.highlightElements(element.findElements(locator), Neodymium.getDriver());
    }

    @Override
    public void beforeFindElements(WebElement element, By locator)
    {
        DebugUtils.highlightElements(element.findElements(locator), Neodymium.getDriver());
    }
}
