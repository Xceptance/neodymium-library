package com.xceptance.neodymium.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Supplier;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.model.StepResult;

/**
 * Convenience methods for step definitions
 *
 * @author rschwietzke
 */
public class AllureAddons
{
    private static final Properties ALLURE_PROPERTIES = io.qameta.allure.util.PropertiesUtils.loadAllureProperties();

    /**
     * Define a step without return value. This can be used to transport data (information) from test into the report.
     *
     * @param info
     *            the info of the information (maybe the information itself if short enough), used in the description of
     *            this step
     * @param content
     *            further information that need to be passed to the report
     */
    @Step("INFO: {info}")
    public static void addToReport(String info, Object content)
    {
    }

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
            if (Neodymium.configuration().screenshotPerStep())
            {
                attachPNG(UUID.randomUUID().toString() + ".png");
            }
        }
    }

    /**
     * Define a step with a return value. This is good for complete and encapsulated test steps.
     *
     * @param <T>
     *            generic return type
     * @param description
     *            the proper description of this step
     * @param actions
     *            what to do as Lambda
     * @return T
     */
    @Step("{description}")
    public static <T> T step(final String description, final Supplier<T> actions)
    {
        try
        {
            return actions.get();
        }
        finally
        {
            if (Neodymium.configuration().screenshotPerStep())
            {
                attachPNG(UUID.randomUUID().toString() + ".png");
            }
        }
    }

    /**
     * Takes screenshot and converts it to byte stream
     * 
     * @param filename
     * @return
     */
    @Attachment(type = "image/png", value = "{filename}", fileExtension = ".png")
    public static byte[] attachPNG(final String filename)
    {
        return ((TakesScreenshot) Neodymium.getDriver()).getScreenshotAs(OutputType.BYTES);
    }
    
    
    public static void removeAttachmentFromStepByName(final String name)
    {

        AllureLifecycle lifecycle = Allure.getLifecycle();
        // suppress errors if we are running without allure
        if (lifecycle.getCurrentTestCase().isPresent())
        {
            lifecycle.updateTestCase((result) -> {
                var stepResult = findCurrentStep(result.getSteps());
                var attachments = stepResult.getAttachments();
                for (int i = 0; i < attachments.size(); i++)
                {
                    io.qameta.allure.model.Attachment attachment = attachments.get(i);
                    if (attachment.getName().equals(name))
                    {
                        String path = ALLURE_PROPERTIES.getProperty("allure.results.directory", "allure-results");
                        // clean up from hard disk
                        File file = Paths.get(path).resolve(attachment.getSource()).toFile();
                        if (file.exists())
                        {
                            file.delete();
                        }
                        attachments.remove(i);
                        i--;
                    }
                }
            });
        }
    }

    public static boolean addAttachmentToStep(final String name, final String type,
                                        final String fileExtension, final InputStream stream)
    {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        // suppress errors if we are running without allure
        if (lifecycle.getCurrentTestCase().isPresent())
        {
            lifecycle.addAttachment(name, type, fileExtension, stream);

            lifecycle.updateTestCase((result) -> {
                var stepResult = findCurrentStep(result.getSteps());
                var attachment = result.getAttachments().get(result.getAttachments().size() - 1);
                result.getAttachments().remove(result.getAttachments().size() - 1);
                stepResult.getAttachments().add(attachment);
            });
            return true;
        }
        return false;
    }

    private static StepResult findCurrentStep(List<StepResult> steps)
    {
        var lastStep = steps.get(steps.size() - 1);
        List<StepResult> childStepts = lastStep.getSteps();
        if (childStepts != null && childStepts.isEmpty() == false)
        {
            return findCurrentStep(childStepts);
        }
        return lastStep;
    }

}
