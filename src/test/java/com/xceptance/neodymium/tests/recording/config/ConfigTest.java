package com.xceptance.neodymium.tests.recording.config;

import java.io.File;
import java.util.HashMap;

import org.aeonbits.owner.ConfigFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.neodymium.recording.config.RecordingConfigurations;
import com.xceptance.neodymium.tests.NeodymiumTest;

public abstract class ConfigTest extends NeodymiumTest
{
    protected HashMap<String, String> defaultConfig = new HashMap<>();

    protected Class<? extends RecordingConfigurations> configClass;

    protected String prefix;

    protected ConfigTest(Class<? extends RecordingConfigurations> configClass, String prefix)
    {
        this.configClass = configClass;
        this.prefix = prefix;
    }

    @Before
    public void setDefaultConfig()
    {
        defaultConfig.put("enableFilming", "false");
        defaultConfig.put("filmAutomaticaly", "true");

        defaultConfig.put("deleteRecordingsAfterAddingToAllureReport", "true");
        defaultConfig.put("appendAllRecordingsToReport", "false");

        defaultConfig.put("imageScaleFactor", "1.0");
        defaultConfig.put("loop", "false");
    }

    @Test
    public void testEnableFilmingDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("enableFilming")), ConfigFactory.create(configClass).enableFilming());
    }

    @Test
    public void testEnableFilming()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".enableFilming", "true");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(true, ConfigFactory.create(configClass).enableFilming());
    }

    @Test
    public void testFilmAutomaticalyDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("filmAutomaticaly")), ConfigFactory.create(configClass).filmAutomaticaly());
    }

    @Test
    public void testFilmAutomaticaly()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".filmAutomaticaly", "false");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(false, ConfigFactory.create(configClass).filmAutomaticaly());
    }

    @Test
    public void testOneImagePerMillisecondsDefault()
    {
        Assert.assertEquals(defaultConfig.get("oneImagePerMilliseconds"),
                            ConfigFactory.create(configClass).oneImagePerMilliseconds() + "");
    }

    @Test
    public void testOneImagePerMilliseconds()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".oneImagePerMilliseconds", "200");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("200", ConfigFactory.create(configClass).oneImagePerMilliseconds() + "");
    }

    @Test
    public void testTempFolderToStoreRecoringDefault()
    {
        Assert.assertEquals(defaultConfig.get("tempFolderToStoreRecording"),
                            ConfigFactory.create(configClass).tempFolderToStoreRecording());
    }

    @Test
    public void testTempFolderToStoreRecording()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".tempFolderToStoreRecording", "build/");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("build/", ConfigFactory.create(configClass).tempFolderToStoreRecording());
    }

    @Test
    public void testDeleteRecordingsAfterAddingToAllureReportDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("deleteRecordingsAfterAddingToAllureReport")),
                            ConfigFactory.create(configClass).deleteRecordingsAfterAddingToAllureReport());
    }

    @Test
    public void testDeleteRecordingsAfterAddingToAllureReport()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".deleteRecordingsAfterAddingToAllureReport", "false");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(false, ConfigFactory.create(configClass).deleteRecordingsAfterAddingToAllureReport());
    }

    @Test
    public void testAppendAllRecordingsToReportDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("appendAllRecordingsToReport")),
                            ConfigFactory.create(configClass).appendAllRecordingsToReport());
    }

    @Test
    public void testAppendAllRecordingsToReport()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".appendAllRecordingsToReport", "true");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(true, ConfigFactory.create(configClass).appendAllRecordingsToReport());
    }

    @Test
    public void testImageQualityDefault()
    {
        Assert.assertEquals(defaultConfig.get("imageQuality"), ConfigFactory.create(configClass).imageQuality() + "");
    }

    @Test
    public void testImageQuality()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".imageQuality", "0.5");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("0.5", ConfigFactory.create(configClass).imageQuality() + "");
    }

    @Test
    public void testImageScaleFactorDefault()
    {
        Assert.assertEquals(defaultConfig.get("imageScaleFactor"),
                            ConfigFactory.create(configClass).imageScaleFactor() + "");
    }

    @Test
    public void testImageScaleFactor()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".imageScaleFactor", "0.5");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("0.5", ConfigFactory.create(configClass).imageScaleFactor() + "");
    }

    @Test
    public void testFormatDefault()
    {
        Assert.assertEquals(defaultConfig.get("format"), ConfigFactory.create(configClass).format());
    }

    @Test
    public void testLoopDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("loop")), ConfigFactory.create(configClass).loop());
    }

    @Test
    public void testFFmpegBinaryPathDefault()
    {
        Assert.assertEquals(defaultConfig.get("ffmpegBinaryPath"), ConfigFactory.create(configClass).ffmpegBinaryPath());
    }

    @Test
    public void testFFmpegLogFileDefault()
    {
        Assert.assertEquals(defaultConfig.get("ffmpegLogFile"), ConfigFactory.create(configClass).ffmpegLogFile());
    }
}
