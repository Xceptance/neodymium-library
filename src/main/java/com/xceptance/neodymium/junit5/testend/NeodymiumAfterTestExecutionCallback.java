package com.xceptance.neodymium.junit5.testend;

import java.io.File;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeborne.selenide.ex.UIAssertionError;
import com.xceptance.neodymium.util.Neodymium;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

@ParametersAreNonnullByDefault
public class NeodymiumAfterTestExecutionCallback implements AfterTestExecutionCallback
{
    private static final Logger log = LoggerFactory.getLogger(NeodymiumAfterTestExecutionCallback.class);

    private final boolean captureSuccessfulTests;

    public NeodymiumAfterTestExecutionCallback()
    {
        this(false);
    }

    /**
     * @param captureSuccessfulTests
     *            param that indicate if you need to capture successful tests
     */
    public NeodymiumAfterTestExecutionCallback(final boolean captureSuccessfulTests)
    {
        this.captureSuccessfulTests = captureSuccessfulTests;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception
    {
        AShot screenshotShooter = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100));
        WebDriver driver = Neodymium.getDriver();
        Screenshot s = null;

        if (captureSuccessfulTests)
        {
            s = screenshotShooter.takeScreenshot(driver);
            log.info("captured Screenshot of successful Test to:");
        }
        else
        {
            Throwable error = context.getExecutionException().get();
            if (!(error instanceof UIAssertionError))
            {
                s = screenshotShooter.takeScreenshot(driver);
                log.info("captured Screenshot of failed Test to:");
            }
        }
        if (s != null)
        {
            ImageIO.write(s.getImage(), "PNG", new File(Neodymium.configuration().reportsPath() + UUID.randomUUID().toString() + ".png"));
        }
    }
}