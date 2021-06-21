package com.xceptance.neodymium.tests.recording.automatic;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import com.xceptance.neodymium.recording.FilmTestExecution;
import com.xceptance.neodymium.recording.config.VideoRecordingConfigurations;
import com.xceptance.neodymium.tests.recording.AbstractRecordingTest;

public class AutomaticVideoRecordingTest extends AbstractRecordingTest
{
    public AutomaticVideoRecordingTest()
    {
        super(false);
    }

    @BeforeClass
    public static void form()
    {
        beforeClass("video");
        configurationsClass = VideoRecordingConfigurations.class;
    }

    @AfterClass
    public static void assertLogFileExists()
    {
        File logFile = new File(FilmTestExecution.getContextVideo().ffmpegLogFile());
        Assert.assertTrue(logFile.exists());
        logFile.delete();
    }
}
