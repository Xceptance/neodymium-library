package com.xceptance.neodymium;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FooRunner extends Runner implements Filterable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FooRunner.class);

    private BlockJUnit4ClassRunner blockJUnit4ClassRunner;

    public FooRunner(Class<?> testClassInput, RunnerBuilder rb) throws InitializationError
    {
        LOGGER.debug("Constructor");
        blockJUnit4ClassRunner = new BlockJUnit4ClassRunner(testClassInput);
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException
    {
        LOGGER.debug("Filter with type: " + filter.getClass());
    }

    @Override
    public Description getDescription()
    {
        LOGGER.debug("getDescription");
        return blockJUnit4ClassRunner.getDescription();
    }

    @Override
    public void run(RunNotifier notifier)
    {
        LOGGER.debug("Run");
        blockJUnit4ClassRunner.run(notifier);
        // for (FrameworkMethod m : testMethods)
        // {
        // Description description = Description.createSuiteDescription(m.getName(), testClass.getAnnotations());
        // notifier.fireTestStarted(description);
        // notifier.fireTestFinished(description);
        // }
    }
}
