package com.xceptance.neodymium.tests.recording.config;

import java.io.File;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.neodymium.recording.FilmTestExecution;
import com.xceptance.neodymium.recording.config.GifRecordingConfigurations;

public class GifConfigTest extends ConfigTest
{

    public GifConfigTest()
    {
        super(GifRecordingConfigurations.class, "gif");
    }

    @Override
    protected GifRecordingConfigurations getContext()
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
        defaultConfig.put("loop", "false");     
    }
    
    @Test
    public void testLoopDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("loop")), getContext().loop());
    } 
    
    @Test
    public void testLoop()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put("gif.loop", "true");
        File tempConfigFile1 = new File("./config/dev-gif-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(true, getContext().loop());
    }    
}
