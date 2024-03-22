package com.xceptance.neodymium.tests.recording.config;

import java.io.File;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.neodymium.recording.FilmTestExecution;
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

    protected abstract RecordingConfigurations getContext();

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
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("enableFilming")), getContext().enableFilming());
    }

    @Test
    public void testEnableFilming()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".enableFilming", "true");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(true, getContext().enableFilming());
    }

    @Test
    public void testFilmAutomaticalyDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("filmAutomaticaly")), getContext().filmAutomaticaly());
    }

    @Test
    public void testFilmAutomaticaly()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".filmAutomaticaly", "false");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(false, getContext().filmAutomaticaly());
    }

    @Test
    public void testOneImagePerMillisecondsDefault()
    {
        Assert.assertEquals(defaultConfig.get("oneImagePerMilliseconds"),
                            getContext().oneImagePerMilliseconds() + "");
    }

    @Test
    public void testOneImagePerMilliseconds()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".oneImagePerMilliseconds", "200");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("200", getContext().oneImagePerMilliseconds() + "");
    }

    @Test
    public void testTempFolderToStoreRecoringDefault()
    {
        Assert.assertEquals(defaultConfig.get("tempFolderToStoreRecording"),
                            getContext().tempFolderToStoreRecording());
    }

    @Test
    public void testTempFolderToStoreRecording()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".tempFolderToStoreRecording", "build/");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("build/", getContext().tempFolderToStoreRecording());
    }

    @Test
    public void testDeleteRecordingsAfterAddingToAllureReportDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("deleteRecordingsAfterAddingToAllureReport")),
                            getContext().deleteRecordingsAfterAddingToAllureReport());
    }

    @Test
    public void testDeleteRecordingsAfterAddingToAllureReport()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".deleteRecordingsAfterAddingToAllureReport", "false");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(false, getContext().deleteRecordingsAfterAddingToAllureReport());
    }

    @Test
    public void testAppendAllRecordingsToReportDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("appendAllRecordingsToReport")),
                            getContext().appendAllRecordingsToReport());
    }

    @Test
    public void testAppendAllRecordingsToReport()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".appendAllRecordingsToReport", "true");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(true, getContext().appendAllRecordingsToReport());
    }

    @Test
    public void testImageQualityDefault()
    {
        Assert.assertEquals(defaultConfig.get("imageQuality"), getContext().imageQuality() + "");
    }

    @Test
    public void testImageQuality()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".imageQuality", "0.5");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("0.5", getContext().imageQuality() + "");
    }

    @Test
    public void testImageScaleFactorDefault()
    {
        Assert.assertEquals(defaultConfig.get("imageScaleFactor"),
                            getContext().imageScaleFactor() + "");
    }

    @Test
    public void testImageScaleFactor()
    {
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".imageScaleFactor", "0.5");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals("0.5", getContext().imageScaleFactor() + "");
    }

    @Test
    public void testFormatDefault()
    {
        Assert.assertEquals(defaultConfig.get("format"), getContext().format());
    }

    @Test
    public void testLoopDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("loop")), getContext().loop());
    }

    @Test
    public void testFFmpegBinaryPathDefault()
    {
        Assert.assertEquals(defaultConfig.get("ffmpegBinaryPath"), getContext().ffmpegBinaryPath());
    }

    @Test
    public void testFFmpegLogFileDefault()
    {
        Assert.assertEquals(defaultConfig.get("ffmpegLogFile"), getContext().ffmpegLogFile());
    }
}
