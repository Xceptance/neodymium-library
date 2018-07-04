package com.xceptance.neodymium.util;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.xceptance.neodymium.NeodymiumWebDriverListener;

public class NeodymiumWebDriverTestListener extends NeodymiumWebDriverListener
{
    public int impliciteWaitCount = 0;

    public NeodymiumWebDriverTestListener()
    {
        boolean allFound = true;
        Method[] parentMethods = NeodymiumWebDriverListener.class.getDeclaredMethods();
        Method[] childMethods = NeodymiumWebDriverTestListener.class.getDeclaredMethods();

        for (int i = 0; i < parentMethods.length; i++)
        {
            boolean foundMethod = false;
            for (int j = 0; j < childMethods.length; j++)
            {
                if (parentMethods[i].getName().equals(childMethods[j].getName()))
                {
                    foundMethod = true;
                    break;
                }
            }
            allFound &= foundMethod;
        }
        Assert.assertEquals("Test classes do not contain the same number of listeners", parentMethods.length, childMethods.length);
        Assert.assertTrue("Test classes do not contain the same listeners", allFound);
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver)
    {
        super.beforeFindBy(by, element, driver);
        impliciteWaitCount++;
    }
}
