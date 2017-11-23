package com.xceptance.neodymium;

import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Attachment;

public class NeodymiumRunListener extends RunListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NeodymiumRunListener.class);

    private List<Failure> failures = new LinkedList<>();

    @Override
    public void testStarted(Description description) throws Exception
    {
        LOGGER.debug("Test started: " + description.toString());
    }

    @Override
    public void testFinished(Description description) throws Exception
    {
        LOGGER.debug("Test finished: " + description.toString());
    }

    @Override
    public void testFailure(Failure failure) throws Exception
    {
        LOGGER.debug("Test failed: " + failure);
        failures.add(failure);

        byte[] screenshot = SelenideHelper.getScreenshotFromFailure(failure);

        if (screenshot != null)
        {
            String screenshotFilename = SelenideHelper.getFilenameFromFailure(failure);
            LOGGER.debug("Attach screenshot with file name: " + screenshotFilename);
            attachPNG(screenshot, screenshotFilename);
        }
        else
        {
            LOGGER.debug("No screenshot available");
        }
    }

    @Attachment(type = "image/png", value = "{filename}", fileExtension = ".png")
    public byte[] attachPNG(byte[] filedata, String filename)
    {
        return filedata;
    }

    public boolean hasFailure()
    {
        return (failures.size() > 0);
    }

    public List<Failure> getFailures()
    {
        return failures;
    }
}
