package com.xceptance.neodymium;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.runner.notification.Failure;

import com.codeborne.selenide.ex.UIAssertionError;

class SelenideHelper
{
    public static byte[] getScreenshotFromFailure(Failure failure)
    {
        UIAssertionError error = failureToUIAssertionError(failure);
        if (error != null)
        {
            String pathToScreenshot = error.getScreenshot();
            try
            {
                return IOUtils.toByteArray(new FileInputStream(new URL(pathToScreenshot).getFile()));
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static String getFilenameFromFailure(Failure failure)
    {
        UIAssertionError error = failureToUIAssertionError(failure);
        if (error != null)
        {
            String[] pathParts = error.getScreenshot().split("/");

            return pathParts[pathParts.length - 1];
        }
        return null;
    }

    private static UIAssertionError failureToUIAssertionError(Failure failure)
    {
        if (failure != null && (failure.getException() instanceof UIAssertionError))
        {
            return (UIAssertionError) failure.getException();
        }

        return null;
    }
}
