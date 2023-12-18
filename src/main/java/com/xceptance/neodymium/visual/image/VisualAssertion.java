package com.xceptance.neodymium.visual.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.aeonbits.owner.ConfigFactory;
import org.junit.Assert;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xceptance.neodymium.visual.image.algorithm.ColorFuzzy;
import com.xceptance.neodymium.visual.image.algorithm.ComparisonAlgorithm;
import com.xceptance.neodymium.visual.image.algorithm.ExactMatch;
import com.xceptance.neodymium.visual.image.algorithm.PixelFuzzy;
import com.xceptance.neodymium.visual.image.util.ImageComparison;
import com.xceptance.neodymium.visual.image.util.MaskImage;
import com.xceptance.neodymium.visual.image.util.RectangleMask;
import com.xceptance.neodymium.visual.image.util.VisualAssertConfiguration;

/**
 * Module for the visual assertion of changes in a browser page. The module is called in an action and takes a
 * screenshot of the current page. This screenshot is then compared to already taken reference images of the same page,
 * or stored as reference image. The configurations for this module are done in the visualassertion.properties under
 * /config There are different algorithms that can be used for the comparison of the images and different ways to
 * visualize those differences for the evaluation.
 */
public class VisualAssertion
{

    /**
     * Counter for the current screenshots
     */
    private static ThreadLocal<Integer> indexCounter = new ThreadLocal<>();

    // subdirectories
    private final String RESULT_DIRECTORY_BASELINE = "baseline";

    private final String RESULT_DIRECTORY_MASKS = "masks";

    private final String RESULT_DIRECTORY_RESULTS = "results"; // all live screenshots go here

    // the property names

    public final String MARK_WITH_BOXES = "box";

    public final String MARK_WITH_A_MARKER = "marker";

    public final String PROPERTY_ALGORITHM_FUZZY = "FUZZY";

    public final String PROPERTY_ALGORITHM_COLORFUZZY = "COLORFUZZY";

    public final String PROPERTY_ALGORITHM_EXACTMATCH = "EXACT";

    public void execute(final WebDriver webdriver, String testCaseName, String actionName)
    {
        final VisualAssertConfiguration props = ConfigFactory.create(VisualAssertConfiguration.class);

        // check if we have to do anything?
        final boolean enabled = props.enabled();
        if (!enabled)
        {
            // skipped silently
            return;
        }

        // --------------------------------------------------------------------------------
        // Get Properties and convert them from String if necessary
        // --------------------------------------------------------------------------------

        // Parent directory of the visual assertion results
        final String resultDirectory = props.resultDirectory();

        // Wait time for the page to load completely
        final int waitTime = props.waitingTime();

        // Block size for the visual marking of differences in the snapshot
        final int markBlockSizeX = props.markBlocksizeX();
        final int markBlockSizeY = props.markBlocksizeY();

        // Marking type that is used for the test
        final String markType = props.markType();

        // fuzzyBlockLength, fuzzyness parameter
        final int fuzzyBlockLength = props.fuzzyBlocksizeXY();

        // Tolerance value for differences in color
        final double colorTolerance = props.toleranceColors();

        // Tolerance value for differences between specific pixels
        final double pixelTolerance = props.tolerancePixels();

        // Flag whether the training mode is enabled
        final boolean trainingsModeEnabled = props.trainingMode();

        // Flag whether masks should be closed to make the covered area larger
        final boolean closeMask = props.maskClose();

        // Width of the mask close
        final int closeMaskWidth = props.maskCloseWidth();

        // Height of the mask close
        final int closeMaskHeight = props.maskCloseHeight();

        // Flag whether a pixel difference image should be created
        final boolean createDifferenceImage = props.createDifferenceImage();

        // Selector for the algorithm that shall be used
        final String algorithmString = props.algorithm().trim().toUpperCase();

        // Identification of the current environment for this test
        final String id = props.id();

        // --------------------------------------------------------------------------------
        // Get the current environment
        // --------------------------------------------------------------------------------

        // Get the name of the test case for the correct folder identifier
        final String currentTestCaseName = testCaseName;

        // Get browsername and browserversion for the subfolders
        final String browserName = getBrowserName(webdriver);
        final String browserVersion = getBrowserVersion(webdriver);

        // Get the name of the action that called the visual assertion
        final String currentActionName = actionName;

        final String timeStamp = Long.toString(new Date().getTime());

        // --------------------------------------------------------------------------------
        // Initialize the directory and file paths, create the directories if necessary
        // --------------------------------------------------------------------------------

        // Generate the child directories for the current environment in the parent result folder
        final File targetDirectory = new File(new File(new File(new File(resultDirectory, id), currentTestCaseName), browserName), browserVersion);
        targetDirectory.mkdirs();

        // Retrieve current index counter for the image file names
        Integer index = indexCounter.get();
        if (index == null)
        {
            index = 1;
        }
        else
        {
            index = index + 1;
        }
        // Update the index
        indexCounter.set(index);

        // Name of the image file for the screenshot
        final String screenshotName = String.format("%03d", index) + "-" + currentActionName;

        // Directory for the reference images
        final File baselineDirectory = new File(targetDirectory, RESULT_DIRECTORY_BASELINE);
        baselineDirectory.mkdirs();
        // Path of the reference image for this assertion
        final File referenceImageFile = new File(baselineDirectory, screenshotName + ".png");

        // Directory for the results of the current test run
        final File testInstanceDirectory = new File(new File(targetDirectory, RESULT_DIRECTORY_RESULTS), timeStamp);
        testInstanceDirectory.mkdirs();
        // Path of the screenshot image file
        final File currentScreenShotFile = new File(testInstanceDirectory, screenshotName + ".png");
        // Path of the marked image file
        final File markedImageFile = new File(testInstanceDirectory, screenshotName + "-marked" + ".png");
        // Path of the difference image file
        final File differenceImageFile = new File(testInstanceDirectory, screenshotName + "-difference" + ".png");

        // Directory of the mask images
        final File maskDirectoryPath = new File(targetDirectory, RESULT_DIRECTORY_MASKS);
        maskDirectoryPath.mkdirs();
        // Path of the mask image file
        final File maskImageFile = new File(maskDirectoryPath, screenshotName + ".png");

        // --------------------------------------------------------------------------------
        // Wait for the page to fully load, so that a correct screenshot can be taken
        // --------------------------------------------------------------------------------

        try
        {
            TimeUnit.MILLISECONDS.sleep(waitTime);
        }
        catch (final InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        // --------------------------------------------------------------------------------
        // Make the screenshot and load the reference image
        // --------------------------------------------------------------------------------

        try
        {
            final BufferedImage screenshot = takeScreenshot(webdriver);
            if (screenshot == null)
            {
                // TODO Has this to be handled in a different way?
                // webdriver cannot take the screenshot -> RETURN
                return;
            }
            // Save the screenshot
            writeImage(screenshot, currentScreenShotFile);

            // If there's no reference screenshot yet -> save screenshot as reference image in baseline
            if (!referenceImageFile.isFile())
            {
                writeImage(screenshot, referenceImageFile);
                // There is no reference for the comparison -> RETURN
                return;
            }

            // Load the reference image
            final BufferedImage reference = ImageIO.read(referenceImageFile);

            // Mask for the image comparison
            MaskImage mask;
            // If a mask already exists load it, else create a new one
            if (maskImageFile.exists())
            {
                mask = new MaskImage(reference, ImageIO.read(maskImageFile));
            }
            else
            {
                mask = new MaskImage(reference);
                writeImage(mask.getMask(), maskImageFile);
            }

            // --------------------------------------------------------------------------------
            // Initialize the configured algorithm
            // --------------------------------------------------------------------------------

            ComparisonAlgorithm algorithm = null;
            switch (algorithmString)
            {
                case PROPERTY_ALGORITHM_COLORFUZZY:
                    algorithm = new ColorFuzzy(colorTolerance);
                    break;
                case PROPERTY_ALGORITHM_EXACTMATCH:
                    algorithm = new ExactMatch();
                    break;
                case PROPERTY_ALGORITHM_FUZZY:
                    algorithm = new PixelFuzzy(pixelTolerance, colorTolerance, fuzzyBlockLength);
                    break;
            }

            // --------------------------------------------------------------------------------
            // If training is enabled adjust the mask, else compare the screenshot to the
            // reference image
            // --------------------------------------------------------------------------------

            if (trainingsModeEnabled)
            {
                // Train the mask to take the current difference between the reference image and screenshot into account
                mask.train(screenshot, algorithm, new RectangleMask(markBlockSizeX, markBlockSizeY));

                // Close the mask to cover a bigger area
                if (closeMask)
                {
                    mask.closeMask(closeMaskWidth, closeMaskHeight);
                }

                // Save the trained mask
                writeImage(mask.getMask(), maskImageFile);
            }
            else
            {
                // Initialize the comparator
                final ImageComparison comparator = new ImageComparison(reference);

                // Result of the comparison whether the images are similar
                final boolean result = comparator.isEqual(screenshot, mask, algorithm);

                // If the two images don't match..
                if (!result)
                {
                    if (createDifferenceImage)
                    {
                        // Create a image of the pixel differences and save it
                        writeImage(comparator.getDifferenceImage(), differenceImageFile);
                    }

                    BufferedImage markedImage = null;
                    switch (markType)
                    {
                        case MARK_WITH_A_MARKER:
                            // Highlight the differences in the image with red and yellow
                            markedImage = comparator.getMarkedImageWithAMarker(markBlockSizeX, markBlockSizeY);
                            break;
                        case MARK_WITH_BOXES:
                            // Surround the differences with red boxes
                            markedImage = comparator.getMarkedImageWithBoxes(markBlockSizeX, markBlockSizeY);
                            break;
                        default:
                            // break
                            Assert.fail(MessageFormat.format("Mark type '{0}' is not supported.", markType));
                            break;
                    }

                    // Save the marked image
                    writeImage(markedImage, markedImageFile);
                }

                // Assert the result of the comparison
                Assert.assertTrue(MessageFormat.format("Website does not match the reference screenshot: {0} ", currentActionName), result);
            }
        }
        catch (final IOException e)
        {
            Assert.fail(MessageFormat.format("Failure during visual image assertion: {0}", e.getMessage()));
        }
    }

    /**
     * Takes a screenshot if the underlying web driver instance is capable of doing it. Fails with a message only in
     * case the webdriver cannot take screenshots. Avoids issue when certain drivers are used.
     * 
     * @param webDriver
     *            the web driver to use
     * @return {@link BufferedImage} if the webdriver supports taking screenshots, null otherwise
     * @throws RuntimeException
     *             In case the files cannot be written
     */
    private BufferedImage takeScreenshot(final WebDriver webDriver)
    {
        if (webDriver instanceof TakesScreenshot)
        {
            final byte[] bytes = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
            try
            {
                return ImageIO.read(new ByteArrayInputStream(bytes));
            }
            catch (final IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the browser name using Selenium methods
     * 
     * @param webDriver
     *            the WebDriver to query
     * @return the browser name
     */
    private String getBrowserName(final WebDriver webDriver)
    {
        final Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();
        final String browserName = capabilities.getBrowserName();

        return browserName == null ? "unknown" : browserName;
    }

    /**
     * Returns the browser version
     * 
     * @param webDriver
     *            the WebDriver to query
     * @return the browser name
     */
    private String getBrowserVersion(final WebDriver webDriver)
    {
        final Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();
        final String browserVersion = capabilities.getBrowserVersion();

        return browserVersion == null ? "unknown" : browserVersion;
    }

    /**
     * Write the image into the filepath given by file
     * 
     * @param image
     *            that should be saved
     * @param file
     *            path where the image shall be saved
     */
    private void writeImage(final BufferedImage image, final File file)
    {
        try
        {
            ImageIO.write(image, "PNG", file);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
