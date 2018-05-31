package com.xceptance.neodymium;

import java.lang.reflect.Field;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.By.ByXPath;

public class SeleniumSelectorUtil
{
    public static String getSelector(By selector)
    {
        if (selector instanceof ByCssSelector)
        {
            return getSelector((ByCssSelector) selector);
        }
        else if (selector instanceof ByLinkText)
        {
            return getSelector((ByLinkText) selector);
        }
        else if (selector instanceof ByXPath)
        {
            return getSelector((ByXPath) selector);
        }
        else if (selector instanceof ByTagName)
        {
            return getSelector((ByTagName) selector);
        }
        else
        {
            throw new NotImplementedException("guess what: " + selector.getClass());
        }
    }

    public static String getSelector(ByCssSelector selector)
    {
        Field selectorField;
        try
        {
            selectorField = ByCssSelector.class.getDeclaredField("selector");
            selectorField.setAccessible(true);
            return (String) selectorField.get(selector);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String getSelector(ByLinkText selector)
    {
        Field selectorField;
        try
        {
            selectorField = ByLinkText.class.getDeclaredField("linkText");
            selectorField.setAccessible(true);
            return (String) selectorField.get(selector);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String getSelector(ByXPath selector)
    {
        Field selectorField;
        try
        {
            selectorField = ByXPath.class.getDeclaredField("xpathExpression");
            selectorField.setAccessible(true);
            return (String) selectorField.get(selector);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String getSelector(ByTagName selector)
    {
        Field selectorField;
        try
        {
            selectorField = ByTagName.class.getDeclaredField("name");
            selectorField.setAccessible(true);
            return (String) selectorField.get(selector);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
