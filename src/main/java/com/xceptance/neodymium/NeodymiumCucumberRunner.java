package com.xceptance.neodymium;

import java.io.IOException;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import cucumber.api.junit.Cucumber;

public class NeodymiumCucumberRunner extends Cucumber
{
    public NeodymiumCucumberRunner(Class<?> clazz) throws InitializationError, IOException
    {
        super(clazz);
    }

    @Override
    public void run(RunNotifier notifier)
    {
        // we add our own run listener in order to attach screenshots taken by Selenide to the Allure report
        // this also neccessary to clear the context between tests
        notifier.addListener(new NeodymiumCucumberRunListener());
        super.run(notifier);
    }
}
