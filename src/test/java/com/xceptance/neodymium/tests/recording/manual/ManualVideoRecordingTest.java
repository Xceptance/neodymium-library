package com.xceptance.neodymium.tests.recording.manual;

import java.io.File;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import com.xceptance.neodymium.recording.FilmTestExecution;
import com.xceptance.neodymium.recording.config.VideoRecordingConfigurations;
import com.xceptance.neodymium.tests.recording.AbstractRecordingTest;

public class ManualVideoRecordingTest extends AbstractRecordingTest
{
    public ManualVideoRecordingTest()
    {
        super(false);
    }

    @BeforeClass
    public static void form()
    {
        // we need to initialize both of them because after the config is build no temp file is able to override
        properties1.put("video.filmAutomaticaly", "false");
        beforeClass("gif");
        beforeClass("video");
        configurationsClass = VideoRecordingConfigurations.class;
    }

    @Before
    public void startFilming()
    {
        FilmTestExecution.startVideoRecording(UUID.randomUUID().toString());
    }

    @After
    public void finishFilming()
    {
        FilmTestExecution.finishVideoFilming(uuid, false);
    }

    @AfterClass
    public static void assertLogFileExists()
    {
        File logFile = new File(FilmTestExecution.getContextVideo().ffmpegLogFile());
        Assert.assertTrue(logFile.exists());
        logFile.delete();
    }
}