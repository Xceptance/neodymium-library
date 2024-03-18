package com.xceptance.neodymium.tests.recording.writer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.neodymium.recording.config.RecordingConfigurations;
import com.xceptance.neodymium.recording.writers.Writer;

public abstract class AbstractWriterTest
{
    private RecordingConfigurations configurations;

    private Class<? extends Writer> writerClass;

    private String pathToFile;

    private Writer writer;

    protected AbstractWriterTest(RecordingConfigurations configurations, Class<? extends Writer> writerClass)
    {
        this.configurations = configurations;
        this.writerClass = writerClass;
    }

    @Before
    public void calculatePath() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException
    {
        if (!new File(configurations.tempFolderToStoreRecording()).exists())
        {
            new File(configurations.tempFolderToStoreRecording()).mkdir();
        }
        pathToFile = configurations.tempFolderToStoreRecording() + UUID.randomUUID().toString() + "." + configurations.format();
        writer = Writer.instantiate(writerClass, configurations, pathToFile);
    }

    @Test
    public void testWriting() throws IOException
    {
        writer.start();
        writer.write(new File("imagesForVideo/img1.png"));
        writer.write(new File("imagesForVideo/img2.png"));
        writer.write(new File("imagesForVideo/img3.png"));
        writer.stop();
        Assert.assertTrue("writer haven't created a file", new File(pathToFile).exists());
        new File(pathToFile).delete();
    }

    @Test
    public void testResizing() throws IOException
    {
        File fileCopy = new File("target/" + UUID.randomUUID().toString() + ".png");
        fileCopy.deleteOnExit();

        FileUtils.copyFile(new File("imagesForVideo/img1.png"), fileCopy);
        int originalLength = (int) fileCopy.length();
        BufferedImage fileCopyImg = ImageIO.read(fileCopy);

        fileCopyImg = writer.resizeImage(fileCopyImg, 0.2);
        ImageIO.write(fileCopyImg, "jpg", fileCopy);

        int compressedLength = (int) fileCopy.length();
        Assert.assertTrue(originalLength > compressedLength);
    }

    @Test
    public void testQualityChange() throws IOException
    {
        File fileCopy = new File("target/" + UUID.randomUUID().toString() + ".png");
        fileCopy.deleteOnExit();

        FileUtils.copyFile(new File("imagesForVideo/img1.png"), fileCopy);
        int originalLength = (int) fileCopy.length();
        BufferedImage fileCopyImg = ImageIO.read(fileCopy);

        fileCopyImg = writer.changeImageQuality(fileCopyImg, 0.2);
        ImageIO.write(fileCopyImg, "jpg", fileCopy);

        int compressedLength = (int) fileCopy.length();
        Assert.assertTrue(originalLength > compressedLength);
    }
}
