package com.xceptance.neodymium.util;

import java.util.UUID;
import java.util.function.Supplier;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.xceptance.neodymium.util.Context;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;

/**
 * Convenience methods for step definitions
 *
 * @author rschwietzke
 */
public class AllureUtils
{
    /**
     * Define a step without return value. This is good for complete and encapsulated test steps.
     *
     * @param description
     *            the proper description of this step
     * @param actions
     *            what to do as Lambda
     */
    @Step("{description}")
    public static void step(final String description, final Runnable actions)
    {
        try
        {
            actions.run();
        }
        finally
        {
            if (Context.get().configuration.screenshotPerStep())
            {
                attachPNG(UUID.randomUUID().toString() + ".png");
            }
        }
    }

    @Step("{description}")
    public static <T> T step(final String description, final Supplier<T> actions)
    {
        try
        {
            return actions.get();
        }
        finally
        {
            if (Context.get().configuration.screenshotPerStep())
            {
                attachPNG(UUID.randomUUID().toString() + ".png");
            }
        }
    }

    @Attachment(type = "image/png", value = "{filename}", fileExtension = ".png")
    public static byte[] attachPNG(final String filename)
    {
        return ((TakesScreenshot) Context.get().driver).getScreenshotAs(OutputType.BYTES);
    }

}
