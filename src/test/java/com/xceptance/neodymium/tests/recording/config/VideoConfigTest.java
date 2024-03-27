package com.xceptance.neodymium.tests.recording.config;

import java.io.File;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.neodymium.recording.FilmTestExecution;
import com.xceptance.neodymium.recording.config.VideoRecordingConfigurations;

public class VideoConfigTest extends ConfigTest
{

    public VideoConfigTest()
    {
        super(VideoRecordingConfigurations.class, "video");
    }

    @Override
    protected VideoRecordingConfigurations getContext()
    {
        return FilmTestExecution.getContextVideo();
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
    public void testFFmpegLogFileDefault()
    {
        Assert.assertEquals(defaultConfig.get("ffmpegLogFile"), getContext().ffmpegLogFile());
    }
    
    @Test
    public void testFFmpegBinaryPathDefault()
    {
        Assert.assertEquals(defaultConfig.get("ffmpegBinaryPath"), getContext().ffmpegBinaryPath());
    }

    @Test
    public void testFFmpegLogFile()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put("video.ffmpegLogFile", "build");
        File tempConfigFile1 = new File("./config/dev-video-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("build", getContext().ffmpegLogFile());
    }

    @Test
    public void testFFmpegBinaryPath()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put("video.ffmpegBinaryPath", "/home/user/node_modules/@ffmpeg-installer/linux-x64/");
        File tempConfigFile1 = new File("./config/dev-video-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("/home/user/node_modules/@ffmpeg-installer/linux-x64/", getContext().ffmpegBinaryPath());
    }
}
