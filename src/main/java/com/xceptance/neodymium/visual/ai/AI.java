// Copyright 2017 Thomas Volkmann
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this
// software and associated documentation files (the "Software"), 
// to deal in the Software without restriction, including without limitation the rights 
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
// and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all 
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
// BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.xceptance.neodymium.visual.ai;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import com.xceptance.neodymium.visual.ai.image.FastBitmap;
import com.xceptance.neodymium.visual.ai.image.PatternHelper;
import com.xceptance.neodymium.visual.ai.machine_learning.ActivationNetwork;
import com.xceptance.neodymium.visual.ai.machine_learning.BipolarSigmoidFunction;
import com.xceptance.neodymium.visual.ai.machine_learning.PerceptronLearning;
import com.xceptance.neodymium.visual.ai.pre_processing.ImageTransformation;
import com.xceptance.neodymium.visual.ai.util.AiConfiguration;
import com.xceptance.neodymium.visual.ai.util.Constants;
import com.xceptance.neodymium.visual.ai.util.Helper;

/**
 * Module for the visual assertion of changes in a browser page. The module is called in an action and takes a
 * screenshot of the current page. This screenshot is then compared to already taken reference images of the same page,
 * or stored as reference image. The configurations for this module are done in the visualassertion.properties under
 * /config There are different algorithms that can be used for the comparison of the images and different ways to
 * visualize those differences for the evaluation.
 */
public class AI
{
    /**
     * Counter for the current screenshots
     */
    private static ThreadLocal<Integer> indexCounter = new ThreadLocal<>();

    // subdirectories
    private final static String RESULT_DIRECTORY_TRAINING = "training";

    private final static String RESULT_DIRECTORY_TRAINING_LEARN = "learn";

    private final static String RESULT_DIRECTORY_TRAINING_VALIDATE = "validate";

    private final static String RESULT_DIRECTORY_RECOGNITION = "recognition";

    private final static String RESULT_DIRECTORY_UNRECOGNIZED = "unrecognized";

    private final static String RESULT_DIRECTORY_RECOGNIZED = "recognized";

    private final static String RESULT_DIRECTORY_NETWORKS = "networks";

    public void execute(final WebDriver webdriver, String testCaseName, String actionName)
    {
        final AiConfiguration props = ConfigFactory.create(AiConfiguration.class);

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

        // Identification of the current environment for this test
        final String id = props.id();
        // Parent directory of the visual assertion results
        final String aiPersistentDirectory = "data/ai";
        final String aiTargetDirectory = "target/ai";

        // Wait time for the page to load completely
        final int waitTime = props.waitingTime();

        Constants.TESTCASE_BOUND_NAME = props.testCaseName();
        Constants.TESTCASE_BOUND = props.testCaseBound();
        Constants.NETWORK_MODE = props.trainingMode();
        Constants.IMAGE_HEIGHT = props.imageHeight();
        Constants.IMAGE_WIDTH = props.imageWidth();
        Constants.FORMAT = props.imageFormat();

        Constants.USE_ORIGINAL_SIZE = props.useOriginalSize();
        Constants.USE_COLOR_FOR_COMPARISON = props.useColorForComparison();
        Constants.LEARNING_RATE = props.learningRate();
        Constants.INTENDED_PERCENTAGE_MATCH = props.percentageMatch();
        Constants.PERCENTAGE_DIFFERENCE = props.percentageDifference();

        final String timeStamp = Long.toString(new Date().getTime());

        // --------------------------------------------------------------------------------
        // Get the current environment
        // --------------------------------------------------------------------------------

        // Get the name of the test case for the correct folder identifier
        final String currentTestCaseName;
        if (Constants.TESTCASE_BOUND && testCaseName != null)
        {
            currentTestCaseName = testCaseName;
        }
        else
        {
            currentTestCaseName = Constants.TESTCASE_BOUND_NAME;
        }

        // Get browser name and browser version for the subfolders
        final String browserName = getBrowserName(webdriver);

        // Get the name of the action that called the visual verification
        final String currentActionName;
        if (actionName == null)
        {
            currentActionName = testCaseName + timeStamp;
        }
        else
        {
            currentActionName = actionName;
        }

        // --------------------------------------------------------------------------------
        // Initialize the directory and file paths, create the directories if necessary
        // --------------------------------------------------------------------------------

        // Generate the child directories for the current environment in the parent result folder
        final File aiTrainingDirectoryFolder = new File(new File(new File(new File(aiPersistentDirectory, RESULT_DIRECTORY_TRAINING), id), currentTestCaseName), browserName);
        aiTrainingDirectoryFolder.mkdirs();

        // Retrieve current index counter for the image file names, only used internal
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

        String screenshotName = "";

        // Name of the image file for the screenshot
        if (actionName == null)
        {
            screenshotName = currentActionName;
        }
        else
        {
            // if the argument is not null, take the destination from the script and change everything according
            screenshotName = Helper.checkFolderForMatch(aiTrainingDirectoryFolder + "", currentActionName)
                             + currentActionName;

        }

        // Directory for the training images
        final File trainingDirectory = new File(aiTrainingDirectoryFolder, screenshotName);
        final File trainingDirectory_uft = new File(trainingDirectory, RESULT_DIRECTORY_TRAINING_LEARN);
        final File trainingDirectory_val = new File(trainingDirectory, RESULT_DIRECTORY_TRAINING_VALIDATE);

        // Path of the screenshot image file
        final String exactScreenshotName = currentActionName + "_" + timeStamp + "_" + index + "." + Constants.FORMAT;
        final File trainingScreenShotFile = new File(trainingDirectory_uft, exactScreenshotName);

        // Directory of the network file
        final File networkDirectoryPath = new File(aiPersistentDirectory, RESULT_DIRECTORY_NETWORKS);
        networkDirectoryPath.mkdirs();
        // Path of the network file
        final File networkFile = new File(networkDirectoryPath, screenshotName);

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
        // Make the screenshot and load the network or create a new one
        // --------------------------------------------------------------------------------

        // initialization
        final FastBitmap screenshot;
        ActivationNetwork an = new ActivationNetwork(new BipolarSigmoidFunction(), 1);
        ImageTransformation im;
        ArrayList<FastBitmap> imgList = new ArrayList<>();
        ArrayList<PatternHelper> patternList = new ArrayList<>();

        if (networkFile.exists())
        {
            // load the corresponding network and all settings which are saved
            an = (ActivationNetwork) an.Load(networkFile.getPath());
            an.setConstants();
            screenshot = new FastBitmap(takeScreenshot(webdriver), exactScreenshotName, Constants.USE_ORIGINAL_SIZE);

            // TODO Has this to be handled in a different way?
            // webdriver cannot take the screenshot -> RETURN
            if (screenshot == null)
            {
                System.exit(-1);
            }

            // if the network is not done with training check the training folder for changes
            // if there are changes, all unknwon images get loaded
            imgList.add(screenshot);
            if (Constants.NETWORK_MODE)
            {
                trainingDirectory.mkdirs();
                trainingDirectory_uft.mkdir();
                trainingDirectory_val.mkdir();
                imgList.addAll(an.scanFolderForChanges(trainingScreenShotFile.getParent(), exactScreenshotName));
            }
            // transform the new screenshot
            im = new ImageTransformation(imgList, an.getAverageMetric(), Constants.NETWORK_MODE);
            imgList = null;
        }
        else
        {
            trainingDirectory.mkdirs();
            trainingDirectory_uft.mkdir();
            trainingDirectory_val.mkdir();
            screenshot = new FastBitmap(takeScreenshot(webdriver), exactScreenshotName, Constants.USE_ORIGINAL_SIZE);

            imgList.add(screenshot);
            // TODO Has this to be handled in a different way?
            // webdriver cannot take the screenshot -> RETURN
            if (screenshot == null)
            {
                System.exit(-1);
            }

            Constants.IMAGE_WIDTH = screenshot.getWidth();
            Constants.IMAGE_HEIGHT = screenshot.getHeight();

            imgList.addAll(an.scanFolderForChanges(trainingScreenShotFile.getParent(), exactScreenshotName));

            // load all images from the directory
            im = new ImageTransformation(imgList);
            imgList = null;
        }

        patternList = im.computeAverageMetric();

        // internal list in network for self testing and image confirmation
        an.setInternalParameter(im.getAverageMetric());
        an.setInternalList(patternList);

        PerceptronLearning pl = new PerceptronLearning(an);
        pl.setLearningRate(Constants.LEARNING_RATE);

        if (Constants.NETWORK_MODE)
        {
            for (PatternHelper pattern : patternList)
            {
                pl.run(pattern.getPatternList());
            }
        }

        ArrayList<FastBitmap> validationList = an.scanFolderForChanges(trainingDirectory_val.toString());
        ArrayList<PatternHelper> validationPatternList = new ArrayList<>();
        if (!validationList.isEmpty())
        {
            ImageTransformation imt = new ImageTransformation(validationList, an.getAverageMetric(), false);
            validationPatternList = imt.computeAverageMetric();
        }

        boolean selfTest = an.onSelfTest(validationPatternList, Constants.NETWORK_MODE);
        double result = 2.0;

        // ensure to get the last element in the list, which is always the current screenshot
        result = an.checkForRecognitionAsDouble(patternList.get(patternList.size() - 1).getPatternList());
        System.out.println("Recognition result: " + result);

        // console output
        if (selfTest)
        {
            System.out.println("Network not ready");
        }

        // Save the screenshot
        final File aiRecognitionDirectoryFolder = new File(new File(new File(new File(aiTargetDirectory, RESULT_DIRECTORY_RECOGNITION), id), currentTestCaseName), browserName);
        aiRecognitionDirectoryFolder.mkdirs();
        if (Constants.INTENDED_PERCENTAGE_MATCH > result && !Constants.NETWORK_MODE && !selfTest)
        {
            // Directory for the unrecognized images of the current test run
            final File unrecognizedInstanceDirectory = new File(new File(aiRecognitionDirectoryFolder, RESULT_DIRECTORY_UNRECOGNIZED), screenshotName);
            // Path of the unrecognized image file
            final File unrecognizedImageFile = new File(unrecognizedInstanceDirectory, exactScreenshotName);
            unrecognizedImageFile.mkdirs();
            Helper.saveImage(screenshot.toBufferedImage(), unrecognizedImageFile);
            Assert.fail("Failure during visual image assertion:");
        }
        else if (Constants.INTENDED_PERCENTAGE_MATCH < result && !Constants.NETWORK_MODE && !selfTest)
        {
            final File recognizedInstanceDirectory = new File(new File(aiRecognitionDirectoryFolder, RESULT_DIRECTORY_RECOGNIZED), screenshotName);
            final File recognizedImageFile = new File(recognizedInstanceDirectory, exactScreenshotName);
            recognizedInstanceDirectory.mkdirs();
            Helper.saveImage(screenshot.toBufferedImage(), recognizedImageFile);
        }
        else
        {
            // Save the network
            trainingDirectory.mkdirs();
            trainingDirectory_uft.mkdir();
            trainingDirectory_val.mkdir();
            an.setInternalParameter(im.getAverageMetric());
            an.Save(networkFile.toString());
            Helper.saveImage(screenshot.toBufferedImage(), trainingScreenShotFile);
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
}
