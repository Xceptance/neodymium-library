package com.xceptance.neodymium.junit5.tests;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.opentest4j.AssertionFailedError;

import com.xceptance.neodymium.junit5.tests.utils.NeodymiumTestExecutionSummary;
import com.xceptance.neodymium.junit5.tests.utils.ResultAndDescriptionListener;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public abstract class AbstractNeodymiumTest
{
    // holds files that will be deleted in @After method
    static List<File> tempFiles = new LinkedList<>();

    ResultAndDescriptionListener listener = new ResultAndDescriptionListener();

    @AfterAll
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

    public NeodymiumTestExecutionSummary run(Class<?> testClass)
    {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                                                                          .request()
                                                                          .selectors(selectClass(testClass))
                                                                          .build();
        Launcher launcher = LauncherFactory.create();
        launcher.discover(request);

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        return listener.getSummary();
    }

    public List<String> getDescription(Class<?> testClass)
    {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                                                                          .request()
                                                                          .selectors(selectClass(testClass))
                                                                          .build();
        Launcher launcher = LauncherFactory.create();
        launcher.discover(request);

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        return listener.getSummary().getDescriptions();
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
    public void check(final NeodymiumTestExecutionSummary result, final boolean expectSuccessful, final int expectedRunCount, final int expectedIgnoreCount,
                      final int expectedFailCount, final Map<String, String> expectedFailureMessages)
    {
        final Optional<String> accumulatedTrace = result.getFailures().stream().map(failure -> List.of(failure.getException().getStackTrace()).toString())
                                                        .reduce(String::concat);
        final String stackTrace = accumulatedTrace.orElse("n/a");
        try
        {
            Assertions.assertEquals(expectSuccessful, result.getTotalFailureCount() == 0, "Test successful");
            Assertions.assertEquals(expectedRunCount, result.getTestsFoundCount(), "Method run count");
            Assertions.assertEquals(result.getTestsSkippedCount(), expectedIgnoreCount, "Method ignore count");
            Assertions.assertEquals(result.getTotalFailureCount(), expectedFailCount, "Method fail count");

            if (expectedFailureMessages != null)
            {
                final long failureCount = result.getTotalFailureCount();
                for (int i = 0; i < failureCount; i++)
                {
                    final String methodName = result.getDescriptions().get(i);
                    Throwable exception = result.getFailures().stream().filter(failure -> failure.getTestIdentifier().getDisplayName().equals(methodName))
                                                .collect(Collectors.toList()).get(0).getException();
                    Assertions.assertEquals(expectedFailureMessages.get(methodName),
                                            exception.toString(),
                                            "Failure message");
                }
            }
        }
        catch (AssertionFailedError e)
        {
            Assertions.fail("Assertionsion failed. " + e.getMessage() + " Stack trace: " + stackTrace);
        }
    }

    /**
     * Assertions that all tests have passed.
     * 
     * @param result
     *            test result to validate
     * @param expectedRunCount
     *            expected number of run tests (including ignored)
     * @param expectedIgnoreCount
     *            expected number of ignored tests
     */
    public void checkPass(final NeodymiumTestExecutionSummary result, final int expectedRunCount, final int expectedIgnoreCount)
    {
        check(result, true, expectedRunCount, expectedIgnoreCount, 0, null);
    }

    /**
     * Assertions that at least one test has failed.
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
    public void checkFail(final NeodymiumTestExecutionSummary result, final int expectedRunCount, final int expectedIgnoreCount, final int expectedFailCount)
    {
        check(result, false, expectedRunCount, expectedIgnoreCount, expectedFailCount, null);
    }

    /**
     * Assertions that at least one test has failed.
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
    public void checkFail(final NeodymiumTestExecutionSummary result, final int expectedRunCount, final int expectedIgnoreCount, final int expectedFailCount,
                          final String expectedFailureMessage)
    {
        final HashMap<String, String> expectedFailureMessages = new HashMap<String, String>();
        for (int i = 0; i < result.getFailures().size(); i++)
        {
            expectedFailureMessages.put(result.getDescriptions().get(i), expectedFailureMessage);
        }
        check(result, false, expectedRunCount, expectedIgnoreCount, expectedFailCount, expectedFailureMessages);
    }

    /**
     * Assertions that at least one test has failed.
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
    public void checkFail(final NeodymiumTestExecutionSummary result, final int expectedRunCount, final int expectedIgnoreCount, final int expectedFailCount,
                          final Map<String, String> expectedFailureMessages)
    {
        check(result, false, expectedRunCount, expectedIgnoreCount, expectedFailCount, expectedFailureMessages);
    }

    /**
     * Assertions that the test description is valid.
     * 
     * @param testDescription
     *            the test description that should be tested
     * @param expectedTestDescription
     *            expected test description as String array
     */
    public void checkDescription(final List<String> testDescription, final String[] expectedTestDescription)
    {
        final String[] actualTestDescription = testDescription.toArray(new String[0]);
        Arrays.sort(actualTestDescription);
        Arrays.sort(expectedTestDescription);

        Assertions.assertArrayEquals(expectedTestDescription, actualTestDescription);
    }

    /**
     * Assertions that the test description is valid.
     * 
     * @param clazz
     *            the class whose description should be tested
     * @param expectedTestDescription
     *            expected test description as String array
     */
    public void checkDescription(final Class<?> clazz, final String[] expectedTestDescription) throws Throwable
    {
        checkDescription(getDescription(clazz), expectedTestDescription);
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
