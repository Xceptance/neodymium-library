package com.xceptance.neodymium;

import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeborne.selenide.logevents.SelenideLogger;

import io.qameta.allure.Attachment;
import io.qameta.allure.selenide.AllureSelenide;

public class NeodymiumRunListener extends RunListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NeodymiumRunListener.class);

    private List<Failure> failures = new LinkedList<>();

    public NeodymiumRunListener()
    {
        SelenideLogger.addListener("allure-selenide", new AllureSelenide());
    }

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
    }

    @Attachment(type = "image/png", value = "{filename}", fileExtension = ".png")
    public byte[] attachPNG(byte[] filedata, String filename)
    {
        return filedata;
    }

    @Attachment(type = "text/plain", value = "{filename}")
    public byte[] attachTxt(byte[] filedata, String filename)
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
