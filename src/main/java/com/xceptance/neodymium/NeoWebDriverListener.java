package com.xceptance.neodymium;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class NeoWebDriverListener implements WebDriverEventListener
{
    List<String> selectors = new LinkedList<>();

    private static String driverJS = "";

    private static String driverCSS = "";

    private static String injectJS = "";

    public NeoWebDriverListener()
    {
        if (driverJS.isEmpty())
        {
            try
            {
                driverJS = new String(Files.readAllBytes(new File("config/driver.min.js").toPath()));
            }
            catch (IOException e)
            {
                throw new RuntimeException("Could not load driver.js", e);
            }
        }
        if (driverCSS.isEmpty())
        {
            try
            {
                driverCSS = new String(Files.readAllBytes(new File("config/driver.min.css").toPath()));
            }
            catch (IOException e)
            {
                throw new RuntimeException("Could not load driver.css", e);
            }
        }
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
        System.out.println("NeoWebDriverListener.beforeFindBy()");

        String selector = SeleniumSelectorUtil.getSelector(by);
        selectors.add(selector);

        try
        {
            JavascriptExecutor jsExec = (JavascriptExecutor) driver;
            jsExec.executeScript("if (window.driver==null) { window.driver = new Driver({allowClose: false});} window.driver.highlight({element: '" +
                                 selector + "',popover: {title: 'Currently selected',description: '" + by.toString() +
                                 " <a onclick=\"window.next=true;\">next</a>" + "'}});");
            jsExec.executeScript("window.next = null");

            // Object executeScript;
            // do
            // {
            // JavaScript.sleep(1000);
            // executeScript = jsExec.executeScript("return window.next");
            // System.out.println("next = " + executeScript);
            // }
            // while (executeScript == null);
            // JavaScript.sleep(500);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.afterFindBy()");
        String selector = SeleniumSelectorUtil.getSelector(by);

        System.out.println(MessageFormat.format("Successfully found {0}", selector));
        selectors.remove(selector);
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;
        jsExec.executeScript("if (window.driver==null) { window.driver = new Driver({allowClose: false});} window.driver.reset(true);");
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver)
    {
        System.out.println("NeoWebDriverListener.onException()");
        throwable.printStackTrace();
    }

    public void injectDriverJs(WebDriver driver)
    {
        // inject driver.js script and css
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;
        jsExec.executeScript(injectJS, driverCSS);
        jsExec.executeScript(driverJS);
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
