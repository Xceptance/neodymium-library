package com.xceptance.neodymium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class NeoWebDriverListener implements WebDriverEventListener
{
    private static String injectJS = "";

    public NeoWebDriverListener()
    {
        if (injectJS.isEmpty())
        {
            try
            {
                injectJS = new String(Files.readAllBytes(new File("config/inject.js").toPath()));
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
        System.out.println("NeoWebDriverListener.afterNavigateTo()");
        injectDriverJs(driver);
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.afterClickOn()");
        injectDriverJs(driver);
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver)
    {
        List<WebElement> foundElements = driver.findElements(by);
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;

        for (WebElement e : foundElements)
        {
            jsExec.executeScript("window.highlightElement('" + findCssPathForElement(e) + "')");
        }
    }

    public String findCssPathForElement(WebElement element)
    {
        // find all the parents of the given element in the tree
        List<String> pathToElement = new LinkedList<>();

        while (true)
        {
            try
            {
                // get parent element
                WebElement parentElement = element.findElement(By.xpath(".."));

                String currentElementLocator = element.getTagName();
                String selector = String.join(" > ", pathToElement);
                if (selector.length() == 0)
                {
                    selector = currentElementLocator;
                }
                else
                {
                    selector = currentElementLocator + " > " + selector;
                }

                // we've got the parent, now we need to determine if there is only one child with the current html tag
                // or not
                List<WebElement> foundElements = parentElement.findElements(By.cssSelector(selector));

                if (foundElements.size() == 1)
                {
                    // yes, there is only one child with the current tag below the parent
                    pathToElement.add(0, currentElementLocator);
                }
                else
                {
                    // the elements uuid we are looking for
                    String targetId = ((RemoteWebElement) element).getId();

                    // There are multiple childs with this tag. Now we need to find the correct child (identify our
                    // self) and use the "nth-child" notation
                    boolean found = false;
                    for (int i = 1; i <= foundElements.size(); i++)
                    {
                        String nthChildSelector = parentElement.getTagName() + " > " + currentElementLocator + ":nth-child(" + i + ")";
                        RemoteWebElement nthChild = (RemoteWebElement) parentElement.findElement(By.cssSelector(nthChildSelector));
                        if (targetId.equals(nthChild.getId()))
                        {
                            // we have found the correct index
                            pathToElement.add(0, currentElementLocator + ":nth-child(" + i + ")");
                            found = true;
                            break;
                        }
                    }

                    if (!found)
                    {
                        throw new RuntimeException("Could not find correct child element");
                    }
                }

                element = parentElement;
            }
            catch (InvalidSelectorException ex)
            {
                // We probaby tried to get the parent of <html> or what ever the root tag in that document is.
                // This is the default condition to break this loop since we will never know how the document is
                // structured.
                pathToElement.add(0, element.getTagName());
                break;
            }
        }

        String elementSelector = String.join(" > ", pathToElement);
        // Final check: The locator should find exact one element with the calculated locator
        List<WebElement> foundElements = element.findElements(By.cssSelector(elementSelector));
        if (foundElements.size() != 1)
        {
            throw new RuntimeException(MessageFormat.format("Selector ''{0}'' should match exact one element but matched {1}",
                                                            elementSelector, foundElements.size()));
        }

        return elementSelector;
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver)
    {
        System.out.println(MessageFormat.format("Successfully found {0}", SeleniumSelectorUtil.getSelector(by)));
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.onException()");
    }

    public void injectDriverJs(WebDriver driver)
    {
        // inject driver.js script and css
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;
        jsExec.executeScript(injectJS, "");
    }

    @Override
    public void beforeAlertAccept(WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.beforeAlertAccept()");
    }

    @Override
    public void afterAlertAccept(WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.afterAlertAccept()");
    }

    @Override
    public void afterAlertDismiss(WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.afterAlertDismiss()");
    }

    @Override
    public void beforeAlertDismiss(WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.beforeAlertDismiss()");
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.beforeNavigateTo()");
    }

    @Override
    public void beforeNavigateBack(WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.beforeNavigateBack()");
    }

    @Override
    public void afterNavigateBack(WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.afterNavigateBack()");

    }

    @Override
    public void beforeNavigateForward(WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.beforeNavigateForward()");

    }

    @Override
    public void afterNavigateForward(WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.afterNavigateForward()");
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.beforeNavigateRefresh()");

    }

    @Override
    public void afterNavigateRefresh(WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.afterNavigateRefresh()");

    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.beforeClickOn()");
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend)
    {
        System.out.println("NeoWebDriverListener.beforeChangeValueOf()");
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend)
    {
        System.out.println("NeoWebDriverListener.afterChangeValueOf()");
    }

    @Override
    public void beforeScript(String script, WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.beforeScript()");
    }

    @Override
    public void afterScript(String script, WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.afterScript()");
    }
}
