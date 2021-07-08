package com.xceptance.neodymium;

import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;

import java.util.HashMap;

import org.junit.runner.notification.Failure;

import io.qameta.allure.junit4.AllureJunit4;

public class AllureNeodymium extends AllureJunit4
{
    private static HashMap<String, Boolean> testFailureSet = new HashMap<String, Boolean>();

    @Override
    public void testFailure(final Failure failure)
    {
        getLifecycle().getCurrentTestCase().ifPresent(uuid -> {
            if (!testFailureSet.containsKey(uuid))
            {
                getLifecycle().updateTestCase(uuid, testResult -> testResult
                                                                            .setStatus(getStatus(failure.getException()).orElse(null))
                                                                            .setStatusDetails(getStatusDetails(failure.getException()).orElse(null)));
                testFailureSet.put(uuid, true);
            }
        });
    }
}
