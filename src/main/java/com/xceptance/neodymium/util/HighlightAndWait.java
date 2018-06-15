package com.xceptance.neodymium.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.codeborne.selenide.Selenide;

public class HighlightAndWait
{
    private static String injectJS = "";

    public static void implicitWait()
    {
        if (Context.get().configuration.implicitWait() > 0)
        {
            Selenide.sleep(Context.get().configuration.implicitWait());
        }
    }

    public static void injectHighlightingJs()
    {
        if (Context.get().configuration.highlightSelectors())
        {
            setUp();
        }
    }

    public static void highlightAllElements(By by, WebDriver driver)
    {
        if (Context.get().configuration.highlightSelectors())
        {
            List<WebElement> foundElements = driver.findElements(by);
            highlightElements(foundElements, driver);
            implicitWait();
            resetHighlight();
        }
    }

    static void setUp()
    {
        if (injectJS.isEmpty())
        {
            try
            {
                ClassLoader cl = HighlightAndWait.class.getClassLoader();
                File file = new File(cl.getResource("./inject.js").getFile());
                injectJS = new String(Files.readAllBytes(file.toPath()));
            }
            catch (IOException e)
            {
                throw new RuntimeException("Could not load inject.js", e);
            }
        }
        // inject driver.js script and css
        Selenide.executeJavaScript(injectJS);
    }

    static void highlightElements(List<WebElement> elements, WebDriver driver)
    {
        Selenide.executeJavaScript("window.NEO.highlightAllElements(arguments[0], document, " + Context.get().configuration.implicitWait() + ")",
                                   elements, driver.getWindowHandle());

    }

    static void resetHighlight()
    {
        Selenide.executeJavaScript("window.NEO.resetHighlightElements(document)");
    }
}
