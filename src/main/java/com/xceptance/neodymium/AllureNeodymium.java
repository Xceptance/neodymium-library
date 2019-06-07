package com.xceptance.neodymium;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import com.xceptance.neodymium.util.Neodymium;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;

public class AllureNeodymium implements LogEventListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureNeodymium.class);

    private AllureLifecycle lifecycle;

    public AllureNeodymium()
    {
        lifecycle = Allure.getLifecycle();
    }

    @Override
    public void beforeEvent(LogEvent currentLog)
    {
        // nothing to do at the moment
    }

    @Override
    public void afterEvent(final LogEvent event)
    {
        lifecycle.getCurrentTestCaseOrStep().ifPresent(parentUuid -> {
            switch (event.getStatus())
            {
                case PASS:
                    if (Neodymium.configuration().screenshotPerStep())
                    {
                        LOGGER.warn(event.getElement());
                        getScreenshotBytes()
                                            .ifPresent(bytes -> lifecycle.addAttachment("Screenshot", "image/png", "png", bytes));
                    }
                    break;
                default:
                    break;
            }
            // lifecycle.stopStep();
        });
    }

    private static Optional<byte[]> getScreenshotBytes()
    {
        try
        {
            return Optional.of((TakesScreenshot) WebDriverRunner.getWebDriver())
                           .map(wd -> wd.getScreenshotAs(OutputType.BYTES));
        }
        catch (WebDriverException e)
        {
            LOGGER.warn("Could not get screen shot", e);
            return Optional.empty();
        }
    }

    private static Optional<byte[]> getPageSourceBytes()
    {
        try
        {
            return Optional.of(WebDriverRunner.getWebDriver())
                           .map(WebDriver::getPageSource)
                           .map(ps -> ps.getBytes(StandardCharsets.UTF_8));
        }
        catch (WebDriverException e)
        {
            LOGGER.warn("Could not get page source", e);
            return Optional.empty();
        }
    }

}
