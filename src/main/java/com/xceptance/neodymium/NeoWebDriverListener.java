package com.xceptance.neodymium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.util.Context;

public class NeoWebDriverListener extends AbstractWebDriverEventListener
{
    private static String injectJS = "";

    public NeoWebDriverListener()
    {
        if (injectJS.isEmpty())
        {
            try
            {
                ClassLoader cl = getClass().getClassLoader();
                File file = new File(cl.getResource("./inject.js").getFile());
                injectJS = new String(Files.readAllBytes(file.toPath()));
            }
            catch (IOException e)
            {
                throw new RuntimeException("Could not load inject.js", e);
            }
        }
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver)
    {
        implicitWait();
        injectHighlightingJs(driver);
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver)
    {
        implicitWait();
        injectHighlightingJs(driver);
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend)
    {
        implicitWait();
        injectHighlightingJs(driver);
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver)
    {
        if (Context.get().configuration.highlightSelectors())
        {
            List<WebElement> foundElements = driver.findElements(by);
            Selenide.executeJavaScript("window.NEO.highlightAllElements(arguments[0], document, " + Context.get().configuration.implicitWait() + ")",
                                       foundElements,
                                       driver.getWindowHandle());
            implicitWait();
            Selenide.executeJavaScript("window.NEO.resetHighlightElements(document)");
        }
    }

    private void implicitWait()
    {
        if (Context.get().configuration.implicitWait() > 0)
        {
            Selenide.sleep(Context.get().configuration.implicitWait());
        }
    }

    private void injectHighlightingJs(WebDriver driver)
    {
        if (Context.get().configuration.highlightSelectors())
        {
            // inject driver.js script and css
            Selenide.executeJavaScript(injectJS);
        }
    }
}
