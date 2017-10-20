package com.xceptance.neodymium;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeodymiumRunListener extends RunListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NeodymiumRunListener.class);

    private Failure failure = null;

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
        this.failure = failure;
    }

    public boolean hasFailure()
    {
        return (failure != null);
    }

    public Failure getFailure()
    {
        return failure;
    }
}
