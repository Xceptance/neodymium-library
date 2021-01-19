package com.xceptance.neodymium;

import io.qameta.allure.Allure;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;

public class StepListener implements StepLifecycleListener
{
    @Override
    public void beforeStepStop(final StepResult result)
    {
        // however the StepResult is marked as deprecated, we can still use this class here because the
        // StepLifecycleListener interface, which uses this POJO, is not deprecated. Furthermore, this POJO is used in
        // many other placed, e.g. by io.qameta.allure.selenide.AllureSelenide, which are also not marked as deprecated.
        // This lets us hope, that there'll be a replacement for the class in the next allure release
        if (result.getStatus().equals(Status.FAILED) && !result.getName().contains("$"))
        {
            StatusDetails statusDetails = result.getStatusDetails();
            Allure.addAttachment("Information about step failure: ", result.getStatusDetails().getTrace());
        }
    }
}
