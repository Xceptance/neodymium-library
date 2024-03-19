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
        // we need to initialize both of them because after the config is build no temp file is able to override
        beforeClass("gif", true);
        beforeClass("video", true);
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
