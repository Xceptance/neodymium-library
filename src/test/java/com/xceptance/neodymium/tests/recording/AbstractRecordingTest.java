package com.xceptance.neodymium.tests.recording;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.recording.FilmTestExecution;
import com.xceptance.neodymium.recording.config.RecordingConfigurations;
import com.xceptance.neodymium.tests.NeodymiumTest;

@Browser("Chrome_headless")
@RunWith(NeodymiumRunner.class)
public abstract class AbstractRecordingTest extends NeodymiumTest
{
    protected static String uuid;

    public static Class<? extends RecordingConfigurations> configurationsClass;

    protected static Map<String, String> properties1 = new HashMap<>();

    private boolean isGif;

    protected AbstractRecordingTest(boolean isGif)
    {
        this.isGif = isGif;
    }

    public static void beforeClass(String format, boolean filmAutomatically)
    {
        properties1.put(format + ".filmAutomaticaly", Boolean.toString(filmAutomatically));
        properties1.put(format + ".enableFilming", "true");
        properties1.put(format + ".deleteRecordingsAfterAddingToAllureReport", "false");
        File tempConfigFile1 = new File("./config/dev-" + format + "-recording.properties");
        writeMapToPropertiesFile(properties1, tempConfigFile1);
        tempFiles.add(tempConfigFile1);
    }

    @Test
    public void test()
    {
        List<String> uuids = isGif ? FilmTestExecution.getNamesOfAllCurrentGifRecordings() : FilmTestExecution.getNamesOfAllCurrentVideoRecordings();
        Assert.assertEquals(1, uuids.size());
        uuid = uuids.get(0);
        Selenide.open("https://www.xceptance.com/en/");
        Selenide.sleep(FilmTestExecution.getContext(configurationsClass).oneImagePerMilliseconds());
    }

    @AfterClass
    public static void assertRecordingFileExists()
    {
        File recordingFile = new File(FilmTestExecution.getContext(configurationsClass).tempFolderToStoreRecording() + uuid + "."
                                      + FilmTestExecution.getContext(configurationsClass).format());
        Assert.assertTrue(recordingFile.exists());
        recordingFile.delete();
    }
}
