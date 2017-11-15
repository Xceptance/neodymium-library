package com.xceptance.neodymium;

import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
