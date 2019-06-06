package com.xceptance.neodymium;

import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;

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

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.selenide.AllureSelenide;

public class NeodymiumAllureSelenide extends AllureSelenide
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureSelenide.class);

    private boolean saveScreenshots = true;

    private boolean savePageHtml = true;

    private AllureLifecycle lifecycle;

    public NeodymiumAllureSelenide()
    {
        this(Allure.getLifecycle());
    }

    public NeodymiumAllureSelenide(AllureLifecycle lifecycle)
    {
        this.lifecycle = lifecycle;
    }

    @Override
    public void afterEvent(final LogEvent event)
    {
        lifecycle.getCurrentTestCaseOrStep().ifPresent(parentUuid -> {
            switch (event.getStatus())
            {
                case PASS:
                    lifecycle.updateStep(step -> step.setStatus(Status.PASSED));
                    if (saveScreenshots)
                    {
                        getScreenshotBytes()
                                            .ifPresent(bytes -> lifecycle.addAttachment("Screenshot", "image/png", "png", bytes));
                    }
                    break;
                case FAIL:
                    if (saveScreenshots)
                    {
                        getScreenshotBytes()
                                            .ifPresent(bytes -> lifecycle.addAttachment("Screenshot", "image/png", "png", bytes));
                    }
                    if (savePageHtml)
                    {
                        getPageSourceBytes()
                                            .ifPresent(bytes -> lifecycle.addAttachment("Page source", "text/html", "html", bytes));
                    }
                    lifecycle.updateStep(stepResult -> {
                        stepResult.setStatus(getStatus(event.getError()).orElse(Status.BROKEN));
                        stepResult.setStatusDetails(getStatusDetails(event.getError()).orElse(new StatusDetails()));
                    });
                    break;
                default:
                    LOGGER.warn("Step finished with unsupported status {}", event.getStatus());
                    break;
            }
            lifecycle.stopStep();
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
