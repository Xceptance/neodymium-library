package com.xceptance.neodymium.junit5.testend;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.PageSnapshot;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.image.ImageProcessor;
import com.assertthat.selenium_shutterbug.utils.web.Coordinates;
import com.codeborne.selenide.ex.UIAssertionError;
import com.xceptance.neodymium.common.testdata.DataSet;
import com.xceptance.neodymium.util.Neodymium;

import io.qameta.allure.Allure;

@ParametersAreNonnullByDefault
public class NeodymiumAfterTestExecutionCallback implements AfterTestExecutionCallback
{
    private static final Logger log = LoggerFactory.getLogger(NeodymiumAfterTestExecutionCallback.class);

    private final boolean captureSuccessfulTests, enableTreeStructure, highlightViewport, highlightLastElement;

    private final Capture captureMode;

    private final String fullScreenHighlightColor, highlightColor;

    private int linethickness = 9;


    public NeodymiumAfterTestExecutionCallback()
    {
        this.captureSuccessfulTests = Neodymium.configuration().enableOnSuccess();
        this.enableTreeStructure = Neodymium.configuration().enableTreeDirectoryStructure();
        this.fullScreenHighlightColor = Neodymium.configuration().fullScreenHighlightColor();
        this.highlightLastElement = Neodymium.configuration().enableHighlightLastElement();
        this.highlightColor = Neodymium.configuration().highlightColor();
        if (Neodymium.configuration().enableFullPageCapture())
        {
            this.captureMode = Capture.FULL;
            this.highlightViewport = Neodymium.configuration().enableHighlightViewport();
        }
        else
        {
            this.captureMode = Capture.VIEWPORT;
            this.highlightViewport = false;
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception
    {
        if (Neodymium.configuration().enableAcvancedScreenShots())
        {
            WebDriver driver = Neodymium.getDriver();
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

            String testClassName = context.getRequiredTestClass().getName();
            String testMethodName = context.getRequiredTestMethod().getName();
            String dataSetName = "";
            Annotation[] annotationList = context.getRequiredTestMethod().getAnnotations();
            for (Annotation a : annotationList)
            {
                if (a.annotationType().equals(DataSet.class))
                {
                    DataSet set = (DataSet) a;
                    dataSetName += "_DataSet";
                    if (set.value() > 0)
                    {
                        dataSetName += "_" + set.value();
                    }
                    if (!set.id().isEmpty())
                    {
                        dataSetName += "_" + set.id();
                    }
                }
            }

            if (this.enableTreeStructure)
            {
                testClassName = testClassName.replace('.', '\\');
            }

            if (this.captureSuccessfulTests)
            {
                this.doScreenshotForDriver(driver, this.captureMode, testMethodName + '_' + Neodymium.getBrowserProfileName() + dataSetName + '_' + timeStamp,
                                       testClassName);
            }
            else
            {
                if (context.getExecutionException().isPresent())
                {
                    Throwable error = context.getExecutionException().get();
                    if (error instanceof UIAssertionError)
                    {
                        this.doScreenshotForDriver(driver, this.captureMode,
                                               testMethodName + '_' + Neodymium.getBrowserProfileName() + dataSetName + '_' + timeStamp,
                                               testClassName);
                    }
                }
            }
        }
    }

    public String getFormatedReportsPath()
    {
        return Path.of(System.getProperty("user.dir") + Neodymium.configuration().reportsPath()).normalize().toString();
    }

    public boolean doScreenshotForDriver(WebDriver driver, Capture captureMode, String filename, String testclassName) throws IOException
    {
        PageSnapshot snapshot = Shutterbug.shootPage(driver, captureMode);
        BufferedImage image = snapshot.getImage();
        String pathname = this.getFormatedReportsPath() + File.separator + testclassName;
        Files.createDirectories(Paths.get(pathname));
        String imagePath = pathname + File.separator + filename + ".png";
        File outputfile = new File(imagePath);
        if (this.highlightViewport)
        {
            double devicePixelRatio = (double) ((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio");
            int offsetY = (int) (Double.parseDouble(((JavascriptExecutor) driver)
                                                                                 .executeScript("return Math.round(Math.max(document.documentElement.scrollTop, document.body.scrollTop))")
                                                                                 .toString()));
            int offsetX = (int) (Double.parseDouble(((JavascriptExecutor) driver)
                                                                                 .executeScript("return Math.round(Math.max(document.documentElement.scrollLeft, document.body.scrollLeft))")
                                                                                 .toString()));

            Dimension size = Neodymium.getViewportSize();
            if (driver instanceof FirefoxDriver)
            {
                size = new Dimension(size.width - (int) (15 * devicePixelRatio), size.height - (int) (15 * devicePixelRatio));
            }
            Point currentLocation = new Point(offsetX, offsetY);
            Coordinates coords = new Coordinates(currentLocation, currentLocation, size, new Dimension(0, 0), devicePixelRatio);
            image = ImageProcessor.blurExceptArea(image, coords);
            image = ImageProcessor.highlight(image, coords, Color.decode(this.fullScreenHighlightColor), this.linethickness);
        }
        if (this.highlightLastElement)
        {
            double devicePixelRatio = (double) ((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio");
            image = ImageProcessor.highlight(image, new Coordinates(Neodymium.getLastUsedElement(), devicePixelRatio), Color.decode(this.highlightColor),
                                             this.linethickness);
        }
        log.info("captured Screenshot to: " + imagePath);
        boolean result = ImageIO.write(image, "png", outputfile);
        if (result)
        {
            Allure.addAttachment("Screenshot", new FileInputStream(imagePath));
        }
        return result;
    }
}