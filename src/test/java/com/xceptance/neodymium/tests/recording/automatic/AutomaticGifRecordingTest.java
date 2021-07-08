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
        beforeClass("gif");
        configurationsClass = GifRecordingConfigurations.class;
    }
}
