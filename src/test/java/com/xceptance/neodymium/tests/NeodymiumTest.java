package com.xceptance.neodymium.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.Result;

import com.google.common.base.Joiner;
import com.xceptance.neodymium.NeodymiumRunner;

public abstract class NeodymiumTest
{
    // holds files that will be deleted in @After method
    static List<File> tempFiles = new LinkedList<>();

    @AfterClass
    public static void cleanUp()
    {
        for (File tempFile : tempFiles)
        {
            deleteTempFile(tempFile);
        }
    }

    /**
     * delete a temporary test file
     */
    protected static void deleteTempFile(File tempFile)
    {
        if (tempFile.exists())
        {
            try
            {
                Files.delete(tempFile.toPath());
            }
            catch (Exception e)
            {
                System.out.println(MessageFormat.format("Couldn''t delete temporary file: ''{0}'' caused by {1}",
                                                        tempFile.getAbsolutePath(), e));
            }
        }
    }

    public void check(Result result, boolean expectedSuccessful, int expectedRunCount, int expectedIgnoreCount, int expectedFailCount,
                      String expectedFailureMessage)
    {
        Assert.assertEquals("Test successful", expectedSuccessful, result.wasSuccessful());
        Assert.assertEquals("Method run count", expectedRunCount, result.getRunCount());
        Assert.assertEquals("Method ignore count", expectedIgnoreCount, result.getIgnoreCount());
        Assert.assertEquals("Method fail count", expectedFailCount, result.getFailureCount());

        if (expectedFailureMessage != null)
        {
            Assert.assertTrue("Failure count", expectedFailCount == 1);
            Assert.assertEquals("Failure message", expectedFailureMessage, result.getFailures().get(0).getMessage());
        }
    }

    public void checkPass(Result result, int expectedRunCount, int expectedIgnoreCount, int expectedFailCount)
    {
        check(result, true, expectedRunCount, expectedIgnoreCount, expectedFailCount, null);
    }

    public void checkFail(Result result, int expectedRunCount, int expectedIgnoreCount, int expectedFailCount,
                          String expectedFailureMessage)
    {
        check(result, false, expectedRunCount, expectedIgnoreCount, expectedFailCount, expectedFailureMessage);
    }

    public void checkDescription(Description testDescription, String[] expectedTestDescription)
    {
        ArrayList<Description> testChildren = testDescription.getChildren();
        String[] actualDescription = new String[testChildren.size()];

        for (int i = 0; i < testChildren.size(); i++)
        {
            actualDescription[i] = testChildren.get(i).getMethodName();
        }
        Arrays.sort(actualDescription);
        Arrays.sort(expectedTestDescription);

        Assert.assertArrayEquals(expectedTestDescription, actualDescription);
    }

    public void checkDescription(Class<?> clazz, String[] expectedTestDescription) throws Throwable
    {
        checkDescription(new NeodymiumRunner(clazz).getDescription(), expectedTestDescription);
    }

    /**
     * Helper method to write a {@link Map} to an file
     * 
     * @param map
     * @param file
     */
    public static void writeMapToPropertiesFile(Map<String, String> map, File file)
    {
        try
        {
            String join = Joiner.on("\r\n").withKeyValueSeparator("=").join(map);

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(join.getBytes());
            outputStream.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
