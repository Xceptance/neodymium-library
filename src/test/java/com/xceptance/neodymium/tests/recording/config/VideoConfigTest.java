package com.xceptance.neodymium.tests.recording.config;

import java.io.File;
import java.util.HashMap;

import org.aeonbits.owner.ConfigFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.neodymium.recording.config.VideoRecordingConfigurations;

public class VideoConfigTest extends ConfigTest
{

    public VideoConfigTest()
    {
        super(VideoRecordingConfigurations.class, "video");
    }

    @Before
    public void setDiffDefaultConfig()
    {
        defaultConfig.put("oneImagePerMilliseconds", "100");
        defaultConfig.put("tempFolderToStoreRecording", "target/videos/");
        defaultConfig.put("imageQuality", "1.0");
        defaultConfig.put("format", "mp4");
        defaultConfig.put("ffmpegBinaryPath", "ffmpeg");
        defaultConfig.put("ffmpegLogFile", "target/ffmpeg_output_msg.txt");
    }

    @Test
    public void testFFmpegLogFile()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".ffmpegLogFile", "build");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("build", ConfigFactory.create(configClass).ffmpegLogFile());
    }

    @Test
    public void testFFmpegBinaryPath()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".ffmpegBinaryPath", "/home/user/node_modules/@ffmpeg-installer/linux-x64/");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("/home/user/node_modules/@ffmpeg-installer/linux-x64/", ConfigFactory.create(configClass).ffmpegBinaryPath());
    }
}
