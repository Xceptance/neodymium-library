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
        FilmTestExecution.clearThreadContexts();
        HashMap<String, String> properties = new HashMap<>();
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        
        defaultConfig.put("enableFilming", "false");
        defaultConfig.put("filmAutomatically", "true");
        defaultConfig.put("deleteRecordingsAfterAddingToAllureReport", "true");
        defaultConfig.put("appendAllRecordingsToReport", "false");        
        defaultConfig.put("imageScaleFactor", "1.0");
    }

    @Test
    public void testEnableFilmingDefault()
    {
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("enableFilming")), getContext().enableFilming());
    }

    @Test
    public void testEnableFilming()
    {
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
        Assert.assertEquals(Boolean.valueOf(defaultConfig.get("filmAutomatically")), getContext().filmAutomatically());
    }

    @Test
    public void testFilmAutomaticaly()
    {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(prefix + ".filmAutomatically", "false");
        File tempConfigFile1 = new File("./config/dev-" + prefix + "-recording.properties");
        writeMapToPropertiesFile(properties, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
        Assert.assertEquals(false, getContext().filmAutomatically());
    }

    @Test
    public void testOneImagePerMillisecondsDefault()
    {
        Assert.assertEquals(defaultConfig.get("oneImagePerMilliseconds"), getContext().oneImagePerMilliseconds() + "");
    }

    @Test
    public void testOneImagePerMilliseconds()
    {
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
        Assert.assertEquals(defaultConfig.get("imageScaleFactor"), getContext().imageScaleFactor() + "");
    }

    @Test
    public void testImageScaleFactor()
    {
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
}
