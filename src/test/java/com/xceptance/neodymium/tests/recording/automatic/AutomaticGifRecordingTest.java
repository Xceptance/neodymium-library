package com.xceptance.neodymium.tests.recording.automatic;

import org.junit.BeforeClass;

import com.xceptance.neodymium.recording.config.GifRecordingConfigurations;
import com.xceptance.neodymium.tests.recording.AbstractRecordingTest;

public class AutomaticGifRecordingTest extends AbstractRecordingTest
{
    public AutomaticGifRecordingTest()
    {
        super(true);
    }

    @BeforeClass
    public static void form()
    {
        // we need to initialize both of them because after the config is build no temp file is able to override
        beforeClass("gif", true);
        beforeClass("video", true);
        configurationsClass = GifRecordingConfigurations.class;
    }
}
