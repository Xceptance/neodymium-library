package com.xceptance.neodymium.tests.recording.config;

import java.io.File;
import java.util.HashMap;

import org.aeonbits.owner.ConfigFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.neodymium.recording.config.GifRecordingConfigurations;

public class GifConfigTest extends ConfigTest
{

    public GifConfigTest()
    {
        super(GifRecordingConfigurations.class, "gif");
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
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".loop", "true");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(true, ConfigFactory.create(configClass).loop());
    }
}
