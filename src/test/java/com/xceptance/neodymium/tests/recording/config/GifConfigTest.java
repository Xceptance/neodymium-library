package com.xceptance.neodymium.tests.recording.config;

import java.io.File;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.neodymium.recording.FilmTestExecution;
import com.xceptance.neodymium.recording.config.GifRecordingConfigurations;
import com.xceptance.neodymium.recording.config.RecordingConfigurations;

public class GifConfigTest extends ConfigTest
{

    public GifConfigTest()
    {
        super(GifRecordingConfigurations.class, "gif");
    }

    @Override
    protected RecordingConfigurations getContext()
    {
        return FilmTestExecution.getContextGif();
    }

    @Before
    public void setDiffDefaultConfig()
    {
        defaultConfig.put("oneImagePerMilliseconds", "500");
        defaultConfig.put("tempFolderToStoreRecording", "target/gifs/");
        defaultConfig.put("imageQuality", "0.2");
        defaultConfig.put("format", "gif");
        defaultConfig.put("ffmpegBinaryPath", null);
        defaultConfig.put("ffmpegLogFile", null);
    }

    @Test
    public void testLoop()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".loop", "true");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(true, getContext().loop());
    }
}
