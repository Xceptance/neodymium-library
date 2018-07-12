package com.xceptance.neodymium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import com.xceptance.neodymium.util.DebugUtils;

public class NeodymiumWebDriverListener extends AbstractWebDriverEventListener
{
    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver)
    {
        DebugUtils.injectHighlightingJs();
        DebugUtils.highlightAllElements(by, driver);
    }
}
