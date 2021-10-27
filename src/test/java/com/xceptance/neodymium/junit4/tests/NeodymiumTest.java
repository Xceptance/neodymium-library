package com.xceptance.neodymium.junit4.tests;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.FrameworkMethod;

import com.xceptance.neodymium.junit4.NeodymiumRunner;

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
     * 
     * @param tempFile
     *            the tempFile that should be deleted after test execution
     */
    public static void deleteTempFile(final File tempFile)
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

    /**
     * Basic method to perform assertions on a given test result.
     * 
     * @param result
     *            test result to validate
     * @param expectSuccessful
     *            the test result should be successful
     * @param expectedRunCount
     *            expected number of run tests (including ignored)
     * @param expectedIgnoreCount
     *            expected number of ignored tests
     * @param expectedFailCount
     *            expected number of failed tests
     * @param expectedFailureMessages
     *            expected failure messages mapped by name of test method
     */
    public void check(final Result result, final boolean expectSuccessful, final int expectedRunCount, final int expectedIgnoreCount,
                      final int expectedFailCount, final Map<String, String> expectedFailureMessages)
    {
        final Optional<String> accumulatedTrace = result.getFailures().stream().map(Failure::getTrace).reduce(String::concat);
        final String stackTrace = accumulatedTrace.orElse("n/a");
        try
        {
            Assert.assertEquals("Test successful", expectSuccessful, result.wasSuccessful());
            Assert.assertEquals("Method run count", expectedRunCount, result.getRunCount());
            Assert.assertEquals("Method ignore count", expectedIgnoreCount, result.getIgnoreCount());
            Assert.assertEquals("Method fail count", expectedFailCount, result.getFailureCount());

            if (expectedFailureMessages != null)
            {
                final int failureCount = result.getFailureCount();
                for (int i = 0; i < failureCount; i++)
                {
                    final String methodName = result.getFailures().get(i).getDescription().getMethodName();
                    Assert.assertEquals("Failure message", expectedFailureMessages.get(methodName), result.getFailures().get(i).getMessage());
                }
            }
        }
        catch (AssertionError e)
        {
            Assert.fail("Assertion failed. " + e.getMessage() + " Stack trace: " + stackTrace);
        }
    }

    /**
     * Assert that all tests have passed.
     * 
     * @param result
     *            test result to validate
     * @param expectedRunCount
     *            expected number of run tests (including ignored)
     * @param expectedIgnoreCount
     *            expected number of ignored tests
     */
    public void checkPass(final Result result, final int expectedRunCount, final int expectedIgnoreCount)
    {
        check(result, true, expectedRunCount, expectedIgnoreCount, 0, null);
    }

    /**
     * Assert that at least one test has failed.
     * 
     * @param result
     *            test result to validate
     * @param expectedRunCount
     *            expected number of run tests (including ignored)
     * @param expectedIgnoreCount
     *            expected number of ignored tests
     * @param expectedFailCount
     *            expected number of failed tests
     */
    public void checkFail(final Result result, final int expectedRunCount, final int expectedIgnoreCount, final int expectedFailCount)
    {
        check(result, false, expectedRunCount, expectedIgnoreCount, expectedFailCount, null);
    }

    /**
     * Assert that at least one test has failed.
     * 
     * @param result
     *            test result to validate
     * @param expectedRunCount
     *            expected number of run tests (including ignored)
     * @param expectedIgnoreCount
     *            expected number of ignored tests
     * @param expectedFailCount
     *            expected number of failed tests
     * @param expectedFailureMessage
     *            expected message of all failures (same message for each failure)
     */
    public void checkFail(final Result result, final int expectedRunCount, final int expectedIgnoreCount, final int expectedFailCount,
                          final String expectedFailureMessage)
    {
        final HashMap<String, String> expectedFailureMessages = new HashMap<String, String>();
        for (Failure failure : result.getFailures())
        {
            expectedFailureMessages.put(failure.getDescription().getMethodName(), expectedFailureMessage);
        }
        check(result, false, expectedRunCount, expectedIgnoreCount, expectedFailCount, expectedFailureMessages);
    }

    /**
     * Assert that at least one test has failed.
     * 
     * @param result
     *            test result to validate
     * @param expectedRunCount
     *            expected number of run tests (including ignored)
     * @param expectedIgnoreCount
     *            expected number of ignored tests
     * @param expectedFailCount
     *            expected number of failed tests
     * @param expectedFailureMessages
     *            expected failures messages mapped by name of test method
     */
    public void checkFail(final Result result, final int expectedRunCount, final int expectedIgnoreCount, final int expectedFailCount,
                          final Map<String, String> expectedFailureMessages)
    {
        check(result, false, expectedRunCount, expectedIgnoreCount, expectedFailCount, expectedFailureMessages);
    }

    /**
     * Assert that the test description is valid.
     * 
     * @param testDescription
     *            the test description that should be tested
     * @param expectedTestDescription
     *            expected test description as String array
     */
    public void checkDescription(final Description testDescription, final String[] expectedTestDescription)
    {
        final ArrayList<Description> testChildren = testDescription.getChildren();
        final String[] actualDescription = new String[testChildren.size()];

        for (int i = 0; i < testChildren.size(); i++)
        {
            actualDescription[i] = testChildren.get(i).getMethodName();
        }
        Arrays.sort(actualDescription);
        Arrays.sort(expectedTestDescription);

        Assert.assertArrayEquals(expectedTestDescription, actualDescription);
    }

    /**
     * Assert that the test description is valid.
     * 
     * @param clazz
     *            the class whose description should be tested
     * @param expectedTestDescription
     *            expected test description as String array
     */
    public void checkDescription(final Class<?> clazz, final String[] expectedTestDescription) throws Throwable
    {
        checkDescription(new NeodymiumRunner(clazz).getDescription(), expectedTestDescription);
    }

    /**
     * Assert that the test method annotations are valid.
     * 
     * @param clazz
     *            the class whose methods annotations should be tested
     * @param expectedAnnotations
     *            expected test methods annotations as Map of method names as key and List of String containing the
     *            toString output of the annotations
     */
    public void checkAnnotations(final Class<?> clazz, final Map<String, List<String>> expectedAnnotations) throws Throwable
    {
        final Map<FrameworkMethod, Description> compDescriptions = new NeodymiumRunner(clazz).getChildDescriptions();
        boolean matching = true;

        for (Entry<String, List<String>> entry : expectedAnnotations.entrySet())
        {
            String methodName = entry.getKey();
            List<String> expAnnotations = entry.getValue();

            final List<String> compAnnotations = getAnnotationsForMethod(compDescriptions, methodName);
            for (String expAnnotation : expAnnotations)
            {
                boolean expAnnotationFound = false;
                for (String compAnnotation : compAnnotations)
                {
                    if (compAnnotation.equals(expAnnotation))
                    {
                        expAnnotationFound = true;
                        break;
                    }
                }
                matching &= expAnnotationFound;
            }
        }
        Assert.assertTrue(matching);
    }

    private List<String> getAnnotationsForMethod(final Map<FrameworkMethod, Description> compDescriptions, final String methodName)
    {
        List<String> resultList = new ArrayList<String>();
        for (Entry<FrameworkMethod, Description> entry : compDescriptions.entrySet())
        {
            Description description = entry.getValue();
            String descMethodName = description.getMethodName();
            if (descMethodName.equals(methodName))
            {
                for (Annotation annotation : description.getAnnotations())
                {
                    resultList.add(annotation.toString());
                }
                break;
            }
        }
        return resultList;
    }

    /**
     * Helper method to write a {@link Map} to an file
     * 
     * @param map
     * @param file
     */
    public static void writeMapToPropertiesFile(final Map<String, String> map, final File file)
    {
        String propertiesString = map.entrySet().stream()
                                     .map(entry -> entry.getKey() + "=" + entry.getValue())
                                     .collect(Collectors.joining(System.lineSeparator()));

        try
        {
            FileUtils.writeStringToFile(file, propertiesString, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
