package com.xceptance.neodymium.junit5.testend;

import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.xceptance.neodymium.common.ScreenshotWriter;

@ParametersAreNonnullByDefault
public class NeodymiumAfterTestExecutionCallback implements AfterTestExecutionCallback
{

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception
    {
        ScreenshotWriter.doScreenshot(context.getRequiredTestMethod().getName(), context.getRequiredTestClass().getName(), context.getExecutionException(),
                                      context.getRequiredTestMethod().getAnnotations());
    }
}