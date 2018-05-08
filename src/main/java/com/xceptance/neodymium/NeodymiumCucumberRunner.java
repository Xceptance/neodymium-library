package com.xceptance.neodymium;

import java.io.IOException;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import com.codeborne.selenide.Configuration;
import com.xceptance.neodymium.util.Context;

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
        notifier.addListener(new NeodymiumRunListener());

        Configuration.timeout = Context.get().configuration.timeout();
        Configuration.collectionsTimeout = Configuration.timeout * 2;

        super.run(notifier);
    }
}
