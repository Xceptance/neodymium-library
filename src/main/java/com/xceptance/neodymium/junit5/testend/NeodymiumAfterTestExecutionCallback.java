package com.xceptance.neodymium.junit5.testend;

import java.nio.file.Path;

import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.codeborne.selenide.ex.UIAssertionError;
import com.xceptance.neodymium.util.Neodymium;

@ParametersAreNonnullByDefault
public class NeodymiumAfterTestExecutionCallback implements AfterTestExecutionCallback
{
    private static final Logger log = LoggerFactory.getLogger(NeodymiumAfterTestExecutionCallback.class);

    private final boolean captureSuccessfulTests;

    private final Capture captureMode;

    public NeodymiumAfterTestExecutionCallback()
    {
        this(Neodymium.configuration().screenshotOnSuccess(), Neodymium.configuration().enableFullPageCapture());
    }

    /**
     * @param captureSuccessfulTests
     *            param that indicate if you need to capture successful tests
     */
    public NeodymiumAfterTestExecutionCallback(boolean captureSuccessfulTests, boolean enableFullPageCapture)
    {
        this.captureSuccessfulTests = captureSuccessfulTests;
        if (enableFullPageCapture)
        {
            this.captureMode = Capture.FULL;
        }
        else
        {
            this.captureMode = Capture.VIEWPORT;
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception
    {
        WebDriver driver = Neodymium.getDriver();
        Path imagePath = Path.of(System.getProperty("user.dir") + Neodymium.configuration().reportsPath());

        if (captureSuccessfulTests)
        {
            Shutterbug.shootPage(driver, this.captureMode).save(imagePath.normalize().toString());
            log.info("captured Screenshot of successful Test to: " + imagePath.normalize());
        }
        else
        {
            if (context.getExecutionException().isPresent())
            {
                Throwable error = context.getExecutionException().get();
                if (error instanceof UIAssertionError)
                {
                    Shutterbug.shootPage(driver, this.captureMode).save(imagePath.normalize().toString());
                    log.info("captured Screenshot of failed Test to: " + imagePath.normalize());
                }
            }
        }
    }
}