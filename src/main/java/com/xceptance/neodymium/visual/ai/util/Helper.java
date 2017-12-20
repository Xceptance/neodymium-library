// Copyright 2017 Thomas Volkmann
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this
// software and associated documentation files (the "Software"), 
// to deal in the Software without restriction, including without limitation the rights 
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
// and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all 
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
// BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.xceptance.neodymium.visual.ai.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.xceptance.neodymium.visual.ai.image.FastBitmap;

/***
 * Different helper for use in the program. All of them are static so there is no instance of this class.
 * 
 * @author Thomas Volkmann
 */
public class Helper
{
    /***
     * Detect if the checkValue is in the range from the referencePoint in respect of the given difference.
     * 
     * @param checkValue
     *            Integer value which will be checked
     * @param referencePoint
     *            Integer value which is used as reference for the checkValue
     * @param difference
     *            Integer difference value
     * @return true if checkValue was near enough, false otherwise
     */
    public static boolean inBetween(int checkValue, int referencePoint, int difference)
    {
        if (checkValue >= referencePoint - difference && checkValue <= referencePoint + difference)
        {
            return true;
        }
        return false;
    }

    /***
     * Check the range around value1 with percentage difference through {@link Helper#getPercentageDifference(double)}
     * to value2.
     * 
     * @param value1
     *            Double value to vary by.
     * @param value2
     *            Double value to check against.
     * @return true if the values are in range to another, false otherwise.
     */
    public static boolean isInRange(double value1, double value2)
    {
        if (value2 > (value1 - getPercentageDifference(value1)) &&
            value2 < (value1 + getPercentageDifference(value1)))
        {
            return true;
        }
        return false;
    }

    /***
     * Apply the percentage difference in the given range around the value. The value will always between 1% - 100% to
     * avoid errors, even if it get changed from outside.
     * 
     * @param value
     *            double value to calculate the difference
     * @return value
     */
    private static double getPercentageDifference(double value)
    {
        double percentageDifference = Constants.PERCENTAGE_DIFFERENCE;
        if (percentageDifference <= 0)
        {
            percentageDifference = 0.1;
        }
        double result = value * percentageDifference;
        // percentage difference is between 1% - 100%
        if (result > value)
        {
            return value;
        }
        return result;
    }

    /**
     * Properties file reader for the ai.properties file. Read and convert all properties for the program.
     * 
     * @param path
     *            String path to the ai.properties file.
     * @throws IOException
     *             Error if the there is an IOException.
     */
    public static void readProperties(String path) throws IOException
    {
        // the property names
        final String PREFIX = "com.xceptance.xlt.ai.";
        final String PROPERTY_ENABLED = PREFIX + "enabled";
        final String PROPERTY_TESTCASE_BOUND = PREFIX + "TESTCASE_BOUND";
        final String PROPERTY_TESTCASE_NAME = PREFIX + "TESTCASE_NAME";
        final String PROPERTY_MODE = PREFIX + "TRAINING";
        final String PROPERTY_USE_ORIGINAL_SIZE = PREFIX + "USE_ORIGINAL_SIZE";
        final String PROPERTY_USE_COLOR_FOR_COMPARISON = PREFIX + "USE_COLOR_FOR_COMPARISON";
        final String PROPERTY_LEARNING_RATE = PREFIX + "LEARNING_RATE";
        final String PROPERTY_INTENDED_PERCENTAGE_MATCH = PREFIX + "INTENDED_PERCENTAGE_MATCH";
        final String PROPERTY_PERCENTAGE_DIFFERENCE = PREFIX + "PERCENTAGE_DIFFERENCE";
        final String PROPERTY_IMAGE_HEIGHT = PREFIX + "IMAGE_HEIGHT";
        final String PROPERTY_IMAGE_WIDTH = PREFIX + "IMAGE_WIDTH";
        final String PROPERTY_FORMAT = PREFIX + "FORMAT";

        Properties props = new Properties();

        try
        {
            // File file = new File(path);
            inputStream = new FileInputStream(path);
            if (inputStream != null)
            {
                props.load(inputStream);
            }
            else
            {
                throw new FileNotFoundException("could not load " + path);
            }

            final String enabled = props.getProperty(PROPERTY_ENABLED, "true");
            if (!enabled.contains("true"))
            {
                // skipped silently
                return;
            }

            // parse all properties to the correct format.
            Constants.TESTCASE_BOUND_NAME = props.getProperty(PROPERTY_TESTCASE_NAME, Constants.TESTCASE_BOUND_NAME);
            Constants.TESTCASE_BOUND = Boolean.parseBoolean(props.getProperty(PROPERTY_TESTCASE_BOUND, Boolean.toString(Constants.TESTCASE_BOUND)));
            Constants.NETWORK_MODE = Boolean.parseBoolean(props.getProperty(PROPERTY_MODE, Boolean.toString(Constants.NETWORK_MODE)));
            Constants.IMAGE_HEIGHT = Integer.parseInt(props.getProperty(PROPERTY_IMAGE_HEIGHT, Integer.toString(Constants.IMAGE_HEIGHT)));
            Constants.IMAGE_WIDTH = Integer.parseInt(props.getProperty(PROPERTY_IMAGE_WIDTH, Integer.toString(Constants.IMAGE_WIDTH)));
            Constants.FORMAT = props.getProperty(PROPERTY_FORMAT, Constants.FORMAT);
            Constants.USE_ORIGINAL_SIZE = Boolean.parseBoolean(props.getProperty(PROPERTY_USE_ORIGINAL_SIZE, Boolean.toString(Constants.USE_ORIGINAL_SIZE)));
            Constants.USE_COLOR_FOR_COMPARISON = Boolean.parseBoolean(props.getProperty(PROPERTY_USE_COLOR_FOR_COMPARISON,
                                                                                        Boolean.toString(Constants.USE_COLOR_FOR_COMPARISON)));
            Constants.LEARNING_RATE = Double.parseDouble(props.getProperty(PROPERTY_LEARNING_RATE, Double.toString(Constants.LEARNING_RATE)));
            Constants.INTENDED_PERCENTAGE_MATCH = Double.parseDouble(props.getProperty(PROPERTY_INTENDED_PERCENTAGE_MATCH,
                                                                                       Double.toString(Constants.INTENDED_PERCENTAGE_MATCH)));
            Constants.PERCENTAGE_DIFFERENCE = Double.parseDouble(props.getProperty(PROPERTY_PERCENTAGE_DIFFERENCE,
                                                                                   Double.toString(Constants.PERCENTAGE_DIFFERENCE)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            inputStream.close();
        }
    }

    /**
     * Set the parameter for the image transformation algorithm, in reference to the image dimensions.
     */
    public static void setImageParameter()
    {
        // values for the parameter THRESHOLD correspondent out of experience and testing
        int tmpMinGrpSize = 300;
        int tmpThreshold = 20;
        tmpMinGrpSize = (Constants.IMAGE_WIDTH + Constants.IMAGE_HEIGHT) / 5;

        if (tmpMinGrpSize > 500)
        {
            tmpMinGrpSize = 400;
            tmpThreshold = 30;
        }

        Constants.MINGROUPSIZE = tmpMinGrpSize;
        Constants.THRESHOLD = tmpThreshold;
    }

    /***
     * Transform a given image to an BufferedImkage with with exact same width and height as the original.
     * 
     * @param img
     *            Image to transform into BuferedImage.
     * @param imageType
     *            Chosen int value of the target ImageType.
     * @return bi BufferedImage
     */
    public static BufferedImage imageToBufferedImage(Image img, int imageType)
    {
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), imageType);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(img, null, null);
        g2.dispose();
        return bi;
    }

    /***
     * Transform Image to BufferedImage with new height and width for scaling. Also change the ImageType to needed type.
     * 
     * @param img
     *            Image
     * @param imageType
     *            int value of the target ImageType
     * @param height
     *            height of the new BufferedImage
     * @param width
     *            width of the new bufferedImage
     * @return resizeBi Resized BufferedImage
     */
    public static BufferedImage imageToBufferedImageScaled(Image img, int imageType, int height, int width)
    {
        BufferedImage resizedBi = new BufferedImage(width, height, imageType);
        Graphics2D g2 = resizedBi.createGraphics();
        g2.drawImage(img, 0, 0, width, height, null);
        g2.dispose();
        return resizedBi;
    }

    /***
     * Transform an image to a {@link FastBitmap} for internal use.
     * 
     * @param img
     *            Image given image to transform.
     * @param tagName
     *            String to identify the image.
     * @param imageType
     *            Integer value for the used ImageType.
     * @return fi FastBitmap
     */
    public static FastBitmap imageToFastBitmap(Image img, String tagName, int imageType)
    {
        FastBitmap fi = new FastBitmap(new BufferedImage(img.getWidth(null), img.getHeight(null), imageType), tagName, Constants.USE_ORIGINAL_SIZE);
        return fi;
    }

    /***
     * Transform image to {@link FastBitmap} with new height and width for scaling.
     * 
     * @param img
     *            Image to transform.
     * @param tagName
     *            String value to identify to image.
     * @return fi FastBitmap.
     */
    public static FastBitmap imageToFastImage(Image img, String tagName)
    {
        FastBitmap fi = new FastBitmap(imageToBufferedImage(img, BufferedImage.TYPE_INT_RGB), tagName, Constants.USE_ORIGINAL_SIZE);
        return fi;
    }

    /***
     * Transform double array in BufferedImage.
     * 
     * @param array
     *            Container which hold the date
     * @param imageType
     *            int value of the target ImageType
     * @return b BufferedImage
     */
    public static BufferedImage ArrayToBufferedImage(double[][] array, int imageType)
    {
        int xLenght = array.length;
        int yLength = array[0].length;
        BufferedImage b = new BufferedImage(xLenght, yLength, imageType);

        for (int x = 0; x < xLenght; x++)
        {
            for (int y = 0; y < yLength; y++)
            {
                int rgb = (int) array[x][y] << 16 | (int) array[x][y] << 8 | (int) array[x][y];
                b.setRGB(x, y, rgb);
            }
        }
        return b;
    }

    /***
     * Write a BufferedImage to HDD as a file, with the chosen format under {@link Constants#FORMAT}.
     * 
     * @param img
     *            BufferedImage
     * @param filename
     *            String name of the file
     */
    public static void saveImage(BufferedImage img, File filename)
    {
        try
        {
            ImageIO.write(img, Constants.FORMAT, filename);
        }
        catch (IOException e)
        {
            System.out.println("File could not be saved");
        }
    }

    /**
     * Read an image file into a BufferedImage.
     * 
     * @param filename
     *            String full path name
     * @return img BufferedImage
     */
    public static BufferedImage loadImage(String filename)
    {
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File(filename));
        }
        catch (IOException e)
        {
            System.out.println("File Not Found");
        }
        return img;
    }

    /**
     * Read an image file into {@link FastBitmap}.
     * 
     * @param filename
     *            String full name to the file location.
     * @return FastBitmap
     */
    public static FastBitmap loadImage_FastBitmap(String filename)
    {
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File(filename));
        }
        catch (IOException e)
        {
            System.out.println("File Not Found");
        }
        return new FastBitmap(img, filename, Constants.USE_ORIGINAL_SIZE);
    }

    /**
     * Read a image file into a BufferedImage and scale this image.
     * 
     * @param filename
     *            String full path name
     * @param width
     *            Integer value for the width of the loaded image.
     * @param height
     *            Integer value for the height of the loaded image.
     * @return img BufferedImage
     */
    public static BufferedImage loadImageScaled(String filename, int width, int height)
    {
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File(filename));
        }
        catch (IOException e)
        {
            System.out.println("File Not Found");
        }
        return imageToBufferedImageScaled(img, 1, height, width);
    }

    /**
     * Read an image file and transform it into a scaled {@link FastBitmap}.
     * 
     * @param filename
     *            String full path name
     * @return BufferedImage
     */
    public static FastBitmap loadImageScaled_FastBitmap(String filename, String name)
    {
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File(filename));
        }
        catch (IOException e)
        {
            System.out.println("File Not Found");
        }
        return imageToFastImage(img, name);
    }

    /***
     * Load all images from the given folder path. Use an image filter for only allowed images and only images.
     * 
     * @param path
     *            String full path name to the folder
     * @return ArrayList all found BufferedImages in the folder
     */
    public static ArrayList<BufferedImage> loadAllImages_BufferedImage(String path)
    {
        ArrayList<BufferedImage> pictureList = new ArrayList<>();
        File[] list = scanFolder(path);
        if (list != null)
        {
            for (File element : list)
            {
                pictureList.add(loadImage(path + File.separator + element.getName()));
            }
        }
        return pictureList;
    }

    /***
     * Load all images from the given folder path. Use an image filter {@link Constants#EXTENSIONS} for only allowed
     * images and only images.
     * 
     * @param path
     *            String full path name to the folder
     * @return ArrayList all found {@link FastBitmap} in the folder
     */
    public static ArrayList<FastBitmap> loadAllImages_FastBitmap(String path)
    {
        ArrayList<FastBitmap> pictureList = new ArrayList<>();
        File[] list = scanFolder(path);
        if (list != null)
        {
            for (File element : list)
            {
                pictureList.add(new FastBitmap(loadImage(path + File.separator + element.getName())));
            }
        }
        return pictureList;
    }

    /***
     * Load all images from the given folder path. Use an image filter {@link Constants#EXTENSIONS} for only allowed
     * images and only images. Images must end with one of the allowed endings {@link Constants#EXTENSIONS}. Takes also
     * a width and height for scaling {@link Helper#imageToBufferedImageScaled(Image, int, int, int)}
     * 
     * @param path
     *            String full path name to the folder
     * @param heigth
     *            int value for scaling
     * @param width
     *            int value for scaling
     * @return ArrayList all found {@link FastBitmap} in the folder
     */
    public static ArrayList<FastBitmap> loadAllImagesScaled_FastBitmap(String path)
    {
        ArrayList<FastBitmap> pictureList = new ArrayList<>();
        File[] list = scanFolder(path);
        if (list != null)
        {
            for (File element : list)
            {
                BufferedImage tempimage = loadImage(path + File.separator + element.getName());
                pictureList.add(new FastBitmap(tempimage, element.getName(), Constants.USE_ORIGINAL_SIZE));
            }
        }
        return pictureList;
    }

    /***
     * Load all Images from the given folder path. Use an image filter for only allowed images and only images. Images
     * must end with one of the allowed endings {@link Constants#EXTENSIONS}. Takes also a width and height for scaling
     * {@link Helper#imageToBufferedImageScaled(Image, int, int, int)}
     * 
     * @param path
     *            String full path name to the folder
     * @param heigth
     *            int value for scaling
     * @param width
     *            int value for scaling
     * @return ArrayList all found BufferedImages in the folder
     */
    public static ArrayList<BufferedImage> loadAllImagesScaled_BufferedImage(String path, int heigth, int width)
    {
        ArrayList<BufferedImage> pictureList = new ArrayList<>();
        File[] list = scanFolder(path);
        if (list != null)
        {
            for (File element : list)
            {
                BufferedImage tempimage = loadImage(path + File.separator + element.getName());
                pictureList.add(imageToBufferedImageScaled(tempimage, BufferedImage.TYPE_INT_RGB, heigth, width));
            }
        }
        return pictureList;
    }

    /**
     * Convert a double value into a string. The string have a format from (##.##) as percentage representation.
     * 
     * @param input
     *            Double value.
     * @return df Format rounded to suit percentage representation.
     */
    public static String numberConverterToPercent(double input)
    {
        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(input * 100);
    }

    /**
     * Convert a double value into a string. The string have a format from (#.##) as time value representation.
     * 
     * @param input
     *            Double value.
     * @return df Format rounded to suit time representation.
     */
    public static String numberConverterToTime(double input)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(input);
    }

    /***
     * Create a new FilenameFilter, to check for image extensions. Allowed extensions are {@link Constants#EXTENSIONS}
     */
    public static final FilenameFilter IMAGE_FILTER = new FilenameFilter()
    {
        @Override
        public boolean accept(final File dir, final String name)
        {
            for (final String ext : Constants.EXTENSIONS)
            {
                if (name.endsWith("." + ext))
                {
                    return (true);
                }
            }
            return (false);
        }
    };

    /**
     * If the option for a destination is used in the test script, this method try to get a match for the destination
     * name. If a match is found everything get sort into the destination. Otherwise there will be new folders with the
     * given destination.
     * 
     * @param path
     *            where the match should be
     * @param currentActionName
     *            per argument given string out of the test script
     * @return result if there is a match the identification number get extracted for further use, null otherwise
     */
    public static String checkFolderForMatch(String path, String currentActionName)
    {
        String result = new String();

        File test = new File(path);
        File[] list = test.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return new File(dir, name).isDirectory();
            }
        });
        // check for matching name, if a match is found the identification number get extracted for further use.
        if (list != null)
        {
            for (File element : list)
            {
                if (element.toString().matches("(.*)" + currentActionName))
                {
                    result = element.toString().replaceAll("[^-?0-9]+", "");
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Progress bar which is used to visualize the progress for the command line tool. Does not work properly in
     * eclipse.
     * 
     * @param progress
     *            Double value of the current progress.
     * @param estimatedTime
     *            long value for used time
     * @param size
     *            Size of the Progress bar.
     * @param index
     *            Current position in the progress bar.
     */
    public static void updatePercentageBar(double progress, long estimatedTime, int size, int index)
    {
        int percent = (int) Math.round(progress * 100);
        if (Math.abs(percent - lastPercent) >= 1)
        {
            StringBuilder template = new StringBuilder("\r[");
            for (int i = 0; i < 50; i++)
            {
                if (i < percent * .5)
                {
                    template.append("=");
                }
                else if (i == percent * .5)
                {
                    template.append(">");
                }
                else
                {
                    template.append(" ");
                }
            }
            template.append("] %s   ");
            if (percent >= 100)
            {
                template.append("%n");
            }
            String approximatedTime = Helper.numberConverterToTime((((double) estimatedTime / 1000000000.0) * (size + 1 - index)));
            System.out.printf(template.toString(), percent + "%  estimated time " + Helper.numberConverterToTime((double) estimatedTime / 1000000000.0) + "s" +
                                                   " approximated time " + approximatedTime + "s");
            lastPercent = percent;
        }
    }

    public static File[] scanFolder(String path)
    {
        File test = new File(path);
        File[] list = test.listFiles(IMAGE_FILTER);
        return list;
    }

    /***
     * Convert a BufferedImage to a 2D double array. This method did not use the getRBG method it calculate the values
     * native with bit shifting of the Integer values of the pixel. The alpha channel is also in consideration and get
     * get special treatment.
     * 
     * @param image
     *            BufferedImage
     * @return result double[][] matrix of the image
     */
    public static double[][] convertTo2DWithoutUsingGetRGB(BufferedImage image)
    {
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;
        double[][] result = new double[height][width];

        if (hasAlphaChannel)
        {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length / 2; pixel += pixelLength)
            {
                int argb = 0;
                // alpha
                argb += (((int) pixels[pixel] & 0xff) << 24);
                // blue
                argb += ((int) pixels[pixel + 1] & 0xff);
                // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 8);
                // red
                argb += (((int) pixels[pixel + 3] & 0xff) << 16);
                result[row][col] = argb;
                col++;
                if (col == width)
                {
                    col = 0;
                    row++;
                }
            }
        }
        else
        {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length / 2; pixel += pixelLength)
            {
                int argb = 0;
                // 255 alpha
                argb += -16777216;
                // blue
                argb += ((int) pixels[pixel] & 0xff);
                // green
                argb += (((int) pixels[pixel + 1] & 0xff) << 8);
                // red
                argb += (((int) pixels[pixel + 2] & 0xff) << 16);

                result[row][col] = argb;
                col++;

                if (col == width)
                {
                    col = 0;
                    row++;
                }
            }
        }
        return result;
    }

    // last percent value for percentage bar.
    private static int lastPercent;

    // input stream for the properties reader.
    private static FileInputStream inputStream;
}
