package com.xceptance.neodymium;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class NeodymiumRunListener extends RunListener
{
    private Failure failure = null;

    @Override
    public void testFailure(Failure failure) throws Exception
    {
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
