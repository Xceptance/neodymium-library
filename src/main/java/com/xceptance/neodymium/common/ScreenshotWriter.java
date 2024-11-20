package com.xceptance.neodymium.common;

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
import java.util.Optional;

import javax.imageio.ImageIO;

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
import com.xceptance.neodymium.util.AllureAddons;
import com.xceptance.neodymium.util.Neodymium;

public class ScreenshotWriter
{
    private static final Logger log = LoggerFactory.getLogger(ScreenshotWriter.class);

    private final boolean captureSuccessfulTests, enableTreeStructure, highlightViewport, highlightLastElement;

    private final Capture captureMode;

    private final String fullScreenHighlightColor, highlightColor;

    private int linethickness = 9;

    public ScreenshotWriter()
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

    public void doScreenshot(String displayName, String testClassName, Optional<Throwable> executionException, Annotation[] annotationList) throws IOException
    {
        if (Neodymium.configuration().enableAcvancedScreenShots())
        {
            WebDriver driver = Neodymium.getDriver();
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            String dataSetName = "";
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
            String imageName = displayName + '_' + Neodymium.getBrowserProfileName() + dataSetName + '_' + timeStamp;
            if (this.enableTreeStructure)
            {
                testClassName = testClassName.replace('.', File.separatorChar);
            }
            if (this.captureSuccessfulTests)
            {
                this.doScreenshotForDriver(driver, this.captureMode, imageName, testClassName);
            }
            else
            {
                if (executionException.isPresent())
                {
                    Throwable error = executionException.get();
                    if (error instanceof UIAssertionError)
                    {
                        this.doScreenshotForDriver(driver, this.captureMode, imageName, testClassName);
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
            double devicePixelRatio = Double.parseDouble(((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio") + "");
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
            double devicePixelRatio = Double.parseDouble("" + ((JavascriptExecutor) driver).executeScript("return window.devicePixelRatio"));
            image = ImageProcessor.highlight(image, new Coordinates(Neodymium.getLastUsedElement(), devicePixelRatio), Color.decode(this.highlightColor),
                                             this.linethickness);
        }
        log.info("captured Screenshot to: " + imagePath);
        boolean result = ImageIO.write(image, "png", outputfile);
        if (result)
        {
            AllureAddons.removeAttachmentFromStepByName("Screenshot");
            boolean screenshotAdded = AllureAddons.addAttachmentToStep("Screenshot", "image/png", ".png", new FileInputStream(imagePath));

            // to spare disk space, remove the file if we already used it inside the report
            if (screenshotAdded)
            {
                outputfile.delete();
            }
        }
        return result;
    }

}
