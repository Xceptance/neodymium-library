package com.xceptance.neodymium.recording.writers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.xceptance.neodymium.recording.config.RecordingConfigurations;

/**
 * Interface for all the writers. Provides a static method to instantiate the writer and the methods every writer should
 * have (start, write and stop). It also implements the methods for image compression as this algorithm is the same for
 * every writer.
 * 
 * @author olha
 */
public interface Writer
{
    /**
     * Instantiates the writer.
     * 
     * @param writer
     *            {@link Class} of the {@link Writer} to instantiate
     * @param recordingConfigurations
     *            {@link RecordingConfigurations} for the {@link Writer}
     * @param fileName
     *            {@link String} name of the file writer shouls write into (with absolute or relative path)
     * @return created {@link Writer} instance
     */
    public static Writer instantiate(Class<? extends Writer> writer, RecordingConfigurations recordingConfigurations, String fileName)
    {
        Writer writerObject = null;
        try
        {
            Constructor<? extends Writer> constructor = writer.getDeclaredConstructor(RecordingConfigurations.class, String.class);
            writerObject = constructor.newInstance(recordingConfigurations, fileName);
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return writerObject;
    }

    /**
     * Compresses image if it' required.
     * 
     * @param imageFile
     *            {@link File} of the image to compress
     * @param imageScaleFactor
     *            {@link Double} scale factor of the image (does nothing on 1.0 value)
     * @param imageQuality
     *            {@link Double} image quality value (does nothing on 1.0 value)
     * @return compressed image {@link File}
     */
    public default File compressImageIfNeeded(File imageFile, double imageScaleFactor, double imageQuality)
    {
        // compress image if needed
        boolean isResizeNeeded = imageScaleFactor != 1.0;
        boolean isCompressionNeeded = imageQuality != 1.0;
        if (isResizeNeeded || isCompressionNeeded)
        {
            try
            {
                BufferedImage img = ImageIO.read(imageFile);
                if (isResizeNeeded)
                {
                    img = resizeImage(img, imageScaleFactor);
                }
                if (isCompressionNeeded)
                {
                    img = changeImageQuality(img, imageQuality);
                }
                ImageIO.write(img, "jpg", imageFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return imageFile;
    }

    /**
     * Changes quality of the image
     * 
     * @param image
     *            {@link BufferedImage} to compress
     * @param imageQuality
     *            {@link Double} value of desired quality percentage
     * @return {@link BufferedImage} with changed quality
     */
    public default BufferedImage changeImageQuality(BufferedImage image, double imageQuality)
    {
        ByteArrayOutputStream compressed = new ByteArrayOutputStream();

        try (ImageOutputStream outputStream = ImageIO.createImageOutputStream(compressed))
        {

            // NOTE: The rest of the code is just a cleaned up version of your code

            // Obtain writer for JPEG format
            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("png").next();
            ImageIO.setUseCache(false);
            // Configure JPEG compression: 70% quality
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality((float) imageQuality);

            // Set your in-memory stream as the output
            jpgWriter.setOutput(outputStream);

            // Write image as JPEG w/configured settings to the in-memory stream
            // (the IIOImage is just an aggregator object, allowing you to associate
            // thumbnails and metadata to the image, it "does" nothing)
            jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);

            // Dispose the writer to free resources
            jpgWriter.dispose();
            return ImageIO.read(new ByteArrayInputStream(compressed.toByteArray()));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Resizes the image
     * 
     * @param originalImage
     *            {@link BufferedImage} to resize
     * @param scaleFactor
     *            {@link Double} of scale factor
     * @return resized {@link BufferedImage}
     */
    public default BufferedImage resizeImage(BufferedImage originalImage, double scaleFactor)
    {
        int targetWidth = (int) Math.round(originalImage.getWidth() * scaleFactor);
        int targetHeight = (int) Math.round(originalImage.getHeight() * scaleFactor);
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    public void start() throws IOException;

    public void write(File image);

    public void stop();
}
