package com.xceptance.neodymium.tests.recording.manual;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.xceptance.neodymium.recording.FilmTestExecution;
import com.xceptance.neodymium.recording.config.GifRecordingConfigurations;
import com.xceptance.neodymium.tests.recording.AbstractRecordingTest;

public class ManualGifRecordingTest extends AbstractRecordingTest
{
    public ManualGifRecordingTest()
    {
        super(true);
    }

    @BeforeClass
    public static void form()
    {
        // we need to initialize both of them because after the config is build no temp file is able to override
        beforeClass("gif", false);
        beforeClass("video", false);
        configurationsClass = GifRecordingConfigurations.class;
    }

    @Before
    public void startFilming()
    {
        FilmTestExecution.startGifRecording(UUID.randomUUID().toString());
    }

    @After
    public void finishFilming()
    {
        FilmTestExecution.finishGifFilming(uuid, false);
    }
}
