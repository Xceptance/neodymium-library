package com.xceptance.neodymium.tests.recording.takescreenshot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.xceptance.neodymium.recording.config.RecordingConfigurations;
import com.xceptance.neodymium.recording.writers.Writer;

public class MockWriter implements Writer
{
    public static List<File> screenshots;

    public MockWriter(RecordingConfigurations recordingConfigurations, String gifFileName)
    {
    }

    @Override
    public void start() throws IOException
    {
        screenshots = new ArrayList<>();
    }

    @Override
    public void write(File image)
    {
        screenshots.add(image);
    }

    @Override
    public void stop()
    {
        Assert.assertNotNull("start method was probably not called", screenshots);
        Assert.assertTrue("no screenshots were taken", screenshots.size() > 0);
    }
}