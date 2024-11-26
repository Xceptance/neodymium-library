package com.xceptance.neodymium.common.recording.writers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.common.recording.TakeScreenshotsThread;
import com.xceptance.neodymium.common.recording.config.RecordingConfigurations;
import com.xceptance.neodymium.common.recording.config.VideoRecordingConfigurations;
import com.xceptance.neodymium.util.AllureAddons;

/**
 * Writer to create a video using FFmpeg
 * 
 * @author olha
 */
public class VideoWriter implements Writer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoWriter.class);

    private ProcessBuilder pb;

    private OutputStream ffmpegInput;

    private Process p;

    private boolean logVideoProcessingTime;

    /**
     * Required to instantiate the object in the {@link Writer#instantiate(Class, RecordingConfigurations, String)}
     * method.
     * <p>
     * Prepares the FFmpeg command with {@link ProcessBuilder}.
     * 
     * @param recordingConfigurations
     *            {@link VideoRecordingConfigurations} for the writer
     * @param videoFileName
     *            {@link String} video file name ( including the path)
     * @throws IOException
     */
    protected VideoWriter(RecordingConfigurations recordingConfigurations, String videoFileName) throws IOException
    {
        // check if ffmpeg binary is found
        p = new ProcessBuilder(((VideoRecordingConfigurations) recordingConfigurations).ffmpegBinaryPath(), "-h").start();

        pb = new ProcessBuilder(((VideoRecordingConfigurations) recordingConfigurations).ffmpegBinaryPath(), "-y", "-f", "image2pipe", "-i", "pipe:0", "-c:v", "libx264", "-strict", "-2", "-preset", "slow", "-pix_fmt", "yuv420p", "-vf", "scale=trunc(iw/2)*2:trunc(ih/2)*2", "-f", "mp4", videoFileName);
        pb.redirectErrorStream(true);
        pb.redirectOutput(Redirect.appendTo(new File(((VideoRecordingConfigurations) recordingConfigurations).ffmpegLogFile())));
        logVideoProcessingTime = recordingConfigurations.logInformationAboutRecording();
    }

    /**
     * Starts FFmpeg video generation.
     * 
     * @throws IOException
     *             to stop screenshots loop on {@link TakeScreenshotsThread} in case of start failure
     */
    @Override
    public void start() throws IOException
    {
        p = pb.start();
        ffmpegInput = p.getOutputStream();
    }

    /**
     * Writes {@link File} into the FFmpeg {@link Process}
     */
    @Override
    public void write(File image)
    {
        byte[] imageBytes;
        imageBytes = new byte[(int) image.length()];
        try (FileInputStream fileInputStream = new FileInputStream(image))
        {
            fileInputStream.read(imageBytes);

            ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(imageBytes));
            BufferedImage img = ImageIO.read(iis);

            ImageIO.write(img, "PNG", ffmpegInput);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Flushes and closes the input stream of the FFmpeg {@link Process} to make it start processing the video. Waits
     * until the video processing is done and logs to the allure report the time required for the processing
     */
    @Override
    public void stop()
    {
        try
        {
            ffmpegInput.close();
            ffmpegInput.flush();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        long videoProcessingStart = new Date().getTime();
        while (p.isAlive())
        {
            if (new Date().getTime() - videoProcessingStart > 200000)
            {
                LOGGER.error("something went wrong with video processing");
                break;
            }
            LOGGER.info("process video is processing");
            Selenide.sleep(200);
        }

        if (logVideoProcessingTime)
        {
            AllureAddons.addToReport("video processing took " + (new Date().getTime() - videoProcessingStart + " ms"), "");
        }
    }
}
