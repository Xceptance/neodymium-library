package com.xceptance.neodymium.visual.image.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Util class which provides the necessary function to manipulate and create images for the comparison algorithms.
 */
public class ImageHelper
{
    // transparent white
    protected final static Color WHITE_TRANSPARENT = new Color(255, 255, 255, 0);

    // black
    protected final static Color BLACK = new Color(0, 0, 0);

    protected final static int SCALING_FACTOR = 10;

    /**
     * Creates another image, which is a copy of the source image
     * 
     * @param source
     *            the image to copy
     * @return a copy of that image as <b>BufferedImage</b>
     */
    protected static BufferedImage copyImage(final BufferedImage source)
    {
        // Creates a fresh BufferedImage that has the same size and content of
        // the source image

        // if source has an image type of 0 set ARGB as type
        int imageType = source.getType();
        if (imageType == 0)
        {
            imageType = BufferedImage.TYPE_INT_ARGB;
        }

        final BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), imageType);
        final Graphics g = copy.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return copy;
    }

    /**
     * Returns a image of the same size as the given image filled with the given color
     * 
     * @param image
     *            Image which sets the size for the new image
     * @param c
     *            Color for the image
     * @return A monochrome BufferedImage with the given color
     */
    protected static BufferedImage createPlainImage(final BufferedImage image, final Color c)
    {
        final BufferedImage copy = copyImage(image);

        final Graphics2D g = copy.createGraphics();
        g.setColor(c);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();

        return copy;
    }

    /**
     * The method weights the red, green and blue values and determines the difference as humans would see it. Based on
     * a comparison algorithm from http://www.compuphase.com/cmetric.htm . The algorithm is based on experiments with
     * people, not theoretics. It is thereby not certain.
     * 
     * @param rgb1
     *            color number 1
     * @param rgb2
     *            color number 2
     * @return the difference between the colors as percent from 0.0 to 1.0
     */
    protected static double calculatePixelRGBDiff(final int rgb1, final int rgb2)
    {
        final double MAX = 721.2489168102785;

        // Initialize the red, green, blue values
        final int r1 = (rgb1 >> 16) & 0xFF;
        final int g1 = (rgb1 >> 8) & 0xFF;
        final int b1 = rgb1 & 0xFF;
        final int r2 = (rgb2 >> 16) & 0xFF;
        final int g2 = (rgb2 >> 8) & 0xFF;
        final int b2 = rgb2 & 0xFF;
        final int rDiff = r1 - r2;
        final int gDiff = g1 - g2;
        final int bDiff = b1 - b2;

        // Initialize the weight parameters
        final int rLevel = (r1 + r2) / 2;
        final double rWeight = 2 + rLevel / 256;
        final double gWeight = 4.0;
        final double bWeight = 2 + ((255 - rLevel) / 256);

        final double cDiff = Math.sqrt(rWeight * rDiff * rDiff + gWeight * gDiff * gDiff + bWeight * bDiff * bDiff);

        return cDiff / MAX;
    }

    /**
     * Calculates whether the current block exceeds either the x or y coordinates of the image. Checks one axis of the
     * image at a time.
     * 
     * @param axisPixelCount
     *            The number of pixels in this block for axis that should be checked
     * @param n
     *            index of the current block
     * @param imageSpan
     *            the width/ height of the image
     * @return The number of pixels that can be used for the mask either in the x or y axis
     */
    protected static int calcBlockLength(final int axisPixelCount, final int n, final int imageSpan)
    {
        if (axisPixelCount * (n + 1) > imageSpan)
        {
            return imageSpan % axisPixelCount;
        }

        return axisPixelCount;
    }

    /**
     * Exact pixel by pixel compare. Images must have the same size
     * 
     * @param img1
     *            First image for the comparison
     * @param img2
     *            Second image for the comparison
     * @return Point[] array that contains the coordinates of pixels that are different
     */
    protected static Point[] compareImages(final BufferedImage img1, final BufferedImage img2)
    {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight())
        {
            return null;
        }

        final ArrayList<Point> pixels = new ArrayList<>();

        for (int x = 0; x < img1.getWidth(); x++)
        {
            for (int y = 0; y < img1.getHeight(); y++)
            {
                // if the RGB values of 2 pixels differ
                if (img1.getRGB(x, y) != img2.getRGB(x, y))
                {
                    pixels.add(new Point(x, y));
                }
            }
        }

        return pixels.toArray(new Point[pixels.size()]);
    }

    /**
     * Method for the color based comparison of pixels. The method compares pixel by pixel with a threshold for the
     * difference in color. Small deviations are permitted.
     * 
     * @param img1
     *            The first image for the comparison
     * @param img2
     *            The second image for the comparison
     * @param colorTolerance
     *            A threshold value that calculates the allowed difference in color between two pixels [0-1[
     * @return Point[] array that contains the coordinates of pixels that are different
     */
    protected static Point[] colorFuzzyCompare(final BufferedImage img1, final BufferedImage img2, final double colorTolerance)
    {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight())
        {
            return null;
        }

        final ArrayList<Point> pixels = new ArrayList<>();

        for (int x = 0; x < img1.getWidth(); x++)
        {
            for (int y = 0; y < img2.getHeight(); y++)
            {
                // calculates difference and adds the coordinates to
                // the relevant ArrayList if the difference is above the
                // colTolerance
                final double difference = calculatePixelRGBDiff(img1.getRGB(x, y), img2.getRGB(x, y));
                if (difference > colorTolerance)
                {
                    pixels.add(new Point(x, y));
                }
            }
        }

        return pixels.toArray(new Point[pixels.size()]);
    }

    /**
     * Compares two images by partitioning them into blocks and checking the number of different pixels in each block.
     * Therefore the difference in color with the given color threshold is calculated. If the number of pixels that are
     * found as different in one block exceeds a number threshold the images are treated as differently and the pixel
     * coordinates are saved as Point objects.
     * 
     * @param img1
     *            The first image for the comparison
     * @param img2
     *            The second image for the comparison
     * @param colorTolerance
     *            A threshold value that calculates the allowed difference in color between two pixels [0-1[
     * @param pixelTolerance
     *            A threshold value that calculates the allowed number of different pixels per block [0-1[
     * @param fuzzyBlockDimension
     *            The x and y dimension d of one block of pixels(d*d), which are validated together
     * @return Point[] array that contains the coordinates of pixels that are different
     */
    protected static Point[] fuzzyCompare(final BufferedImage img1, final BufferedImage img2, final double colorTolerance,
                                          final double pixelTolerance, final int fuzzyBlockDimension)
    {
        final ArrayList<Point> pixels = new ArrayList<>();

        // Calculate the number of blocks for each axis
        final int horizontalBlockCount = img1.getWidth() / fuzzyBlockDimension;
        final int verticalBlockCount = img1.getHeight() / fuzzyBlockDimension;

        // For each block
        for (int x = 0; x < horizontalBlockCount; x++)
        {
            for (int y = 0; y < verticalBlockCount; y++)
            {
                final ArrayList<Point> tempCoordinates = new ArrayList<>();
                final int horizontalBlockWidth = calcBlockLength(fuzzyBlockDimension, x, img1.getWidth());
                final int verticalBlockHeight = calcBlockLength(fuzzyBlockDimension, y, img1.getHeight());
                final int differencesAllowed = (int) Math.floor(horizontalBlockWidth * verticalBlockHeight * pixelTolerance);
                int differencesPerBlock = 0;

                // For each pixel in this block, check for differences
                for (int w = 0; w < horizontalBlockWidth; w++)
                {
                    for (int h = 0; h < verticalBlockHeight; h++)
                    {
                        final int xCoord = x * fuzzyBlockDimension + w;
                        final int yCoord = y * fuzzyBlockDimension + h;

                        // calculate the difference and draw the differenceImage
                        // if needed
                        final double difference = calculatePixelRGBDiff(img1.getRGB(xCoord, yCoord), img2.getRGB(xCoord, yCoord));

                        // If there is a notable difference
                        if (difference > colorTolerance)
                        {

                            // Increment differencesPerBlock and add the
                            // coordinates to the
                            // temporary arraylists
                            differencesPerBlock++;
                            tempCoordinates.add(new Point(xCoord, yCoord));
                        }
                    }
                }

                // If the number of differences exceeds the threshold, save the coordinates of the pixels
                // that are different
                if (differencesPerBlock > differencesAllowed)
                {
                    pixels.addAll(tempCoordinates);
                }

                // clear the temporary coordinates
                tempCoordinates.clear();
            }
        }

        return pixels.toArray(new Point[pixels.size()]);
    }

    /**
     * Scales a binary image down to the given size. Does not innately preserve Width/ Height ratio. Used in closeImage.
     * Divides the bigger image into blocks. If there are some pixels leftover, the last blocks gets them, no matter how
     * many they are. It sets a pixel to the foreground color if any any pixel in the corresponding block had the
     * foreground color.
     * 
     * @param img
     *            The original image
     * @param newWidth
     *            The width of the new image after scaling
     * @param newHeight
     *            The height of the new image after scaling
     * @param scalingFactor
     *            The scaling factor for the image size
     * @param rgbForegroundColor
     *            The foreground color
     * @return A downscaled BufferedImage copy of the original image
     */
    protected static BufferedImage scaleDownMaskImage(final BufferedImage img, final int newWidth, final int newHeight,
                                                      final int scalingFactor, final int rgbForegroundColor)
    {

        final BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        boolean hasForegroundColor;

        // Go through every pixel of the scaled image
        for (int w = 0; w < scaledImage.getWidth(); w++)
        {
            for (int h = 0; h < scaledImage.getHeight(); h++)
            {
                hasForegroundColor = false;

                // Check if the corresponding block in the image to scale has a
                // black pixel
                for (int x = w * scalingFactor; x < (w + 1) * scalingFactor; x++)
                {
                    for (int y = h * scalingFactor; y < (h + 1) * scalingFactor; y++)
                    {
                        // Check if it isn't over the border
                        if (x < img.getWidth() && y < img.getHeight())
                        {
                            if (img.getRGB(x, y) == rgbForegroundColor)
                            {
                                hasForegroundColor = true;
                                break;
                            }
                        }
                    }
                }

                // And set the pixel of the scaled image black if the
                // corresponding block had any black pixel
                if (hasForegroundColor)
                {
                    scaledImage.setRGB(w, h, rgbForegroundColor);
                }
            }
        }
        return scaledImage;
    }

    /**
     * Scales a binary image up to the given size. Does not innately preserve Width/ Height ratio. Used in closeImage.
     * Divides the bigger image into blocks. Sets all the pixels in the block to the foreground color if the
     * corresponding pixel has the foreground color. If there are some pixels leftover, the last blocks gets them, no
     * matter how many there are.
     * 
     * @param img
     *            The original image
     * @param newWidth
     *            The width of the new image after scaling
     * @param newHeight
     *            The height of the new image after scaling
     * @param scalingFactor
     *            The scaling factor for the image size
     * @param rgbForegroundColor
     *            The foreground color
     * @return A upscaled BufferedImage copy of the original image
     */
    protected static BufferedImage scaleUpMaskImage(final BufferedImage img, final int newWidth, final int newHeight,
                                                    final int scalingFactor, final int rgbForegroundColor)
    {

        final BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        // Go through every pixel of the image to scale
        for (int w = 0; w < img.getWidth(); w++)
        {
            for (int h = 0; h < img.getHeight(); h++)
            {
                // Check if it has the foreground color
                if (img.getRGB(w, h) == rgbForegroundColor)
                {
                    // And set every pixel in the corresponding block true if it
                    // does
                    for (int x = w * scalingFactor; x < w * scalingFactor + scalingFactor; x++)
                    {
                        for (int y = h * scalingFactor; y < h * scalingFactor + scalingFactor; y++)
                        {
                            // So long as it doesn't go over the border
                            if (x < scaledImage.getWidth() && y < scaledImage.getHeight())
                            {
                                scaledImage.setRGB(x, y, rgbForegroundColor);
                            }
                        }
                    }
                }
            }
        }

        return scaledImage;
    }

    /**
     * Increases an images width and height, the old image will be in the top left corner of the new image; the rest
     * will be transparent black
     * 
     * @param img
     *            The original image
     * @param width
     *            The width of the new image
     * @param height
     *            The height of the new image
     * @return A new BufferedImage that holds the original image in the top left corner and the rest is filled with
     *         black
     */
    protected static BufferedImage increaseImageSize(final BufferedImage img, final int width, final int height)
    {
        final BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics g = newImg.createGraphics();

        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, width, height);

        g.drawImage(img, 0, 0, null);
        g.dispose();
        return newImg;
    }

    /**
     * Creates a new image with a changed image size if it doesn't match the given width and height
     * 
     * @param img
     *            The original image
     * @param width
     *            The width that the image should have
     * @param height
     *            The height that the image should have
     * @return A BufferedImage copy of the original image that has been adjusted to the given width and height
     */
    protected static BufferedImage adaptImageSize(final BufferedImage img, final int width, final int height)
    {
        if (img.getWidth() != width || img.getHeight() != height)
        {
            return increaseImageSize(img, width, height);
        }

        return img;
    }

    /**
     * Overlays the black areas of one image over another image. Doesn't actually use transparency.
     * 
     * @param image
     *            The original image
     * @param overlay
     *            the image that will be placed over the original image
     * @param rgbForegroundColor
     *            The color that is used for the originally black areas
     * @return A BufferedImage copy of the original image in which the black areas of the overlay are marked
     */
    protected static BufferedImage overlayMaskImage(final BufferedImage image, final BufferedImage overlay,
                                                    final int rgbForegroundColor)
    {
        final BufferedImage copy = copyImage(image);

        // Go through every pixel of the image
        for (int x = 0; x < copy.getWidth(); x++)
        {
            for (int y = 0; y < copy.getHeight(); y++)
            {
                if (overlay.getRGB(x, y) == rgbForegroundColor)
                {
                    copy.setRGB(x, y, rgbForegroundColor);
                }
            }
        }

        return copy;
    }

    /**
     * Creates and returns an erosion image, using the algorithm from morphological image processing.
     * <p>
     * Assumes the structuring element is filled with ones and thereby only needs it's width and height. The origin is
     * placed in the middle of the structuring element. If width and/ or height are even, they are incremented to make
     * sure there is a middle pixel.
     * 
     * @param img
     *            the image to erode
     * @param structElementWidth
     *            Width of the structure element mask
     * @param structElementHeight
     *            Height of the structure element mask
     * @param rgbForegroundColor
     *            Foreground color
     * @param rgbBackgroundColor
     *            Background color
     * @return The eroded image as BufferedImage
     */
    protected static BufferedImage erodeImage(final BufferedImage img, int structElementWidth, int structElementHeight,
                                              final int rgbForegroundColor, final int rgbBackgroundColor)
    {

        final BufferedImage erosionedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

        boolean fits;

        // The origin of the structuring element will be it's middle pixel
        // Therefore make sure there is a middle pixel, ie make width and height
        // uneven.
        if ((structElementWidth % 2) == 0)
        {
            structElementWidth++;
        }
        if ((structElementHeight % 2) == 0)
        {
            structElementHeight++;
        }

        // Metaphorically places the structure element
        // In every possible position
        for (int w = 0; w < img.getWidth(); w++)
        {
            for (int h = 0; h < img.getHeight(); h++)
            {

                fits = true;

                // The origin of the structuring element is it's middle pixel
                for (int x = w - (structElementWidth / 2); x <= w + (structElementWidth / 2); x++)
                {
                    for (int y = h - (structElementHeight / 2); y <= h + (structElementHeight / 2); y++)
                    {

                        // As long as the pixels not over the border
                        if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight())
                        {

                            // Assumes all the pixels in the structureImage are
                            // 1. If the pixel does not have the right color
                            // black, set fits false, set the pixel in the
                            // erosionImage to the foreground color and break
                            // the loop
                            if (img.getRGB(x, y) != rgbForegroundColor)
                            {
                                fits = false;
                                erosionedImage.setRGB(w, h, rgbBackgroundColor);
                                break;
                            }
                        }
                    }
                }

                // After every pixel was checked and if fits is true
                // Set the pixel in the erosionImage black
                // Some room for performance increase with a better break?
                if (fits)
                {
                    erosionedImage.setRGB(w, h, rgbForegroundColor);
                }
            }
        }
        return erosionedImage;
    }

    /**
     * Creates and returns a dilation image using the algorithm for morphological image processing.
     * <p>
     * Assumes the structuring element is filled with ones and thereby only needs it's width and height. The origin is
     * placed in the middle of the structuring element. If width and/ or height are even, they are incremented to make
     * sure there is a middle pixel.
     * <p>
     * Uses the rgbForegroundColor and rgbBackgroundColor instance variables to determine the background and the
     * foreground color.
     * 
     * @param img
     *            the image to dilate
     * @param structElementWidth
     *            the width of the structure element mask
     * @param structElementHeight
     *            the height of the structure element mask
     * @param rgbForegroundColor
     *            the foreground color
     * @param rgbBackgroundColor
     *            the background color
     * @return the dilated image as BufferedImage
     */
    protected static BufferedImage dilateImage(final BufferedImage img, int structElementWidth, int structElementHeight,
                                               final int rgbForegroundColor, final int rgbBackgroundColor)
    {

        final BufferedImage dilationImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        boolean hits;

        // The origin of the structuring element will be it's middle pixel
        // Therefore make sure there is a middle pixel, ie make width and height
        // uneven.
        if ((structElementWidth % 2) == 0)
        {
            structElementWidth++;
        }
        if ((structElementHeight % 2) == 0)
        {
            structElementHeight++;
        }

        // Metaphorically places the structure element
        // In every possible position
        for (int w = 0; w < img.getWidth(); w++)
        {
            for (int h = 0; h < img.getHeight(); h++)
            {

                hits = false;

                // Check every pixel of the structured element
                // against the pixel it metaphorically overlaps
                // There might be some room for performance increase here
                // The origin of the structuring element is it's middle pixel
                for (int x = w - (structElementWidth / 2); x <= w + (structElementWidth / 2); x++)
                {
                    for (int y = h - (structElementHeight / 2); y <= h + (structElementHeight / 2); y++)
                    {

                        // As long as the pixels don't go over the border
                        if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight())
                        {

                            // Assumes all the pixels in the structureImage are
                            // 1. If the pixel is black, set hits true, set the
                            // pixel in the dilationImage to the foreground
                            // color
                            // and break the loop
                            if (img.getRGB(x, y) == rgbForegroundColor)
                            {
                                hits = true;
                                dilationImage.setRGB(w, h, rgbForegroundColor);
                            }
                        }
                    }
                }

                // After every pixel was checked and if hits is false
                // Set the pixel in the dilationImage to he background color
                if (!hits)
                {
                    dilationImage.setRGB(w, h, rgbBackgroundColor);
                }
            }
        }
        return dilationImage;
    }

    /**
     * Shrinks an image using the shrinkImage method, closes it and scales it back up again for performance reasons.
     * Depending on the images size and the compressionfactor, it may still be very performance heavy. Closes an image
     * using the dilation and erosion methods.
     * 
     * @param img
     *            the image to close
     * @param structElementWidth
     *            the width of the structure element for dilation and erosion
     * @param structElementHeight
     *            the height of the structure element for dilation and erosion
     * @param rgbForegroundColor
     *            The foreground color for the marking
     * @param rgbBackgroundColor
     *            The background color for the marking
     * @return the closed image
     */
    protected static BufferedImage closeImage(BufferedImage img, final int structElementWidth, final int structElementHeight,
                                              final int rgbForegroundColor, final int rgbBackgroundColor)
    {

        final int scaledWidth = (int) Math.ceil(img.getWidth() / SCALING_FACTOR);
        final int scaledHeight = (int) Math.ceil(img.getHeight() / SCALING_FACTOR);

        // Scale the image for performance reasons.
        BufferedImage shrunkImg = scaleDownMaskImage(img, scaledWidth, scaledHeight, SCALING_FACTOR, rgbForegroundColor);

        // Close it
        shrunkImg = dilateImage(shrunkImg, structElementWidth, structElementHeight, rgbForegroundColor, rgbBackgroundColor);
        shrunkImg = erodeImage(shrunkImg, structElementWidth, structElementHeight, rgbForegroundColor, rgbBackgroundColor);
        // Scale the image back
        img = scaleUpMaskImage(shrunkImg, img.getWidth(), img.getHeight(), SCALING_FACTOR, rgbForegroundColor);
        return img;
    }

    /**
     * Method to mark areas around the detected differences. Goes through every pixel that was different and marks the
     * marking block it is in, unless it was marked already. <br>
     * If markingX of markingY are 1, it will simply mark the detected differences.
     *
     * @param image
     *            the original image for which the differences were found
     * @param pixels
     *            the array with the differences.
     * @param markingSizeX
     *            Length of the marker on the x axis
     * @param markingSizeY
     *            Length of the marker on the y axis
     * @return Copy of the original image with marked pixels
     */
    protected static BufferedImage markDifferencesWithBoxes(final BufferedImage image, final Point[] pixels,
                                                            final int markingSizeX, final int markingSizeY)
    {
        if (pixels == null)
        {
            return null;
        }

        final BufferedImage copy = copyImage(image);

        // Check if markingX or markingY are 1. If they are, just mark every
        // different pixel,
        // don't bother with rectangles
        if (markingSizeX == 1 || markingSizeY == 1)
        {
            for (final Point pixel : pixels)
            {
                colorPixel(copy, pixel.x, pixel.y, null);
            }

            return copy;
        }

        final int imageWidth = copy.getWidth();
        final int imageHeight = copy.getHeight();
        // And if markingX and markingY are above one, paint rectangles!
        // Normal case
        final int blocksX = imageWidth / markingSizeX;
        final int blocksY = imageHeight / markingSizeY;

        final boolean[][] markedBlocks = new boolean[blocksX + 1][blocksY + 1];

        int xBlock, yBlock, subImageWidth, subImageHeight;

        for (final Point pixel : pixels)
        {
            xBlock = pixel.x / markingSizeX;
            yBlock = pixel.y / markingSizeY;

            subImageWidth = calcBlockLength(markingSizeX, xBlock, imageWidth);
            subImageHeight = calcBlockLength(markingSizeY, yBlock, imageHeight);

            if (!markedBlocks[xBlock][yBlock])
            {
                drawBorders(copy, xBlock, yBlock, markingSizeX, markingSizeY, subImageWidth, subImageHeight, null);
                markedBlocks[xBlock][yBlock] = true;
            }
        }

        return copy;
    }

    /**
     * Method to mark areas around the detected differences. Goes through every pixel that was different and marks the
     * marking block it is in, unless it was marked already. <br>
     * If markingX of markingY are 1, it will simply mark the detected differences.
     *
     * @param image
     *            the original image for which the differences were found
     * @param pixels
     *            the array with the differences.
     * @param markingSizeX
     *            Length of the marker on the x axis
     * @param markingSizeY
     *            Length of the marker on the y axis
     * @return Copy of the original image with marked pixels
     */
    protected static BufferedImage markDifferencesWithAMarker(final BufferedImage image, final Point[] pixels,
                                                              final int markingSizeX, final int markingSizeY)
    {
        if (pixels == null)
        {
            return null;
        }

        final BufferedImage imageCopy = copyImage(image);

        final Color highlighterColor = new Color(228, 252, 90, 50);
        final Color pixelEmphasizeColor = new Color(228, 0, 0);

        final Graphics2D g = imageCopy.createGraphics();
        g.setColor(highlighterColor);

        for (final Point pixel : pixels)
        {
            // the middle of the block should be our pixel to make it marker like
            int x = pixel.x - (markingSizeX / 2);
            int y = pixel.y - (markingSizeY / 2);

            // avoid negative values
            x = x < 0 ? 0 : x;
            y = y < 0 ? 0 : y;

            g.fillRect(x, y, markingSizeX, markingSizeY);
        }

        g.dispose();

        // mark the pixels on the new background
        for (final Point pixel : pixels)
        {
            imageCopy.setRGB(pixel.x, pixel.y, pixelEmphasizeColor.getRGB());
        }

        return imageCopy;
    }

    /**
     * Colors a certain pixel using getComplementaryColor. Works directly on imgOut.
     * 
     * @param image
     *            the original image for which certain pixels should be marked
     * @param x
     *            the x coordinate of the pixel to color
     * @param y
     *            the y coordinate of the pixel to color
     * @param c
     *            the color
     */
    protected static void colorPixel(final BufferedImage image, final int x, final int y, final Color c)
    {
        Color newColor;

        if (c == null)
        {
            final Color currentColor = new Color(image.getRGB(x, y));
            final double redLimit = 0.8;

            final int nonRedSum = currentColor.getGreen() + currentColor.getBlue();
            final double results = nonRedSum > 0 ? currentColor.getRed() / nonRedSum : 0;

            if (results > redLimit)
            {
                // red is strong in that one
                newColor = Color.GREEN;
            }
            else
            {
                newColor = Color.RED;
            }
        }
        else
        {
            newColor = c;
        }

        image.setRGB(x, y, newColor.getRGB());
    }

    /**
     * Colors the borders of a certain rectangle. Used to mark blocks. Uses the colorPixel method and subImageHeight/
     * subImageWidth. <br>
     * Works directly on imgOut.
     * 
     * @param image
     *            The image in which something will be marked
     * @param currentX
     *            Starting position
     * @param currentY
     *            Starting position
     * @param width
     *            Vertical length of the rectangle to mark
     * @param height
     *            Horizontal length of the rectangle to mark
     * @param subImageWidth
     *            the width of the partial image
     * @param subImageHeight
     *            the height of the partial image
     * @param c
     *            the color
     */
    protected static void drawBorders(final BufferedImage image, final int currentX, final int currentY, final int width,
                                      final int height, final int subImageWidth, final int subImageHeight, final Color c)
    {
        int x, y;

        for (int a = 0; a < subImageWidth; a++)
        {
            x = currentX * width + a;
            y = currentY * height;
            colorPixel(image, x, y, c);

            y = currentY * height + subImageHeight - 1;
            colorPixel(image, x, y, c);
        }

        for (int b = 1; b < subImageHeight - 1; b++)
        {
            x = currentX * width;
            y = currentY * height + b;
            colorPixel(image, x, y, c);

            x = currentX * width + subImageWidth - 1;
            colorPixel(image, x, y, c);
        }
    }

    /**
     * Fully marks the bottom and right borders of an image transparent (transparent white). Used in isEqual to mark the
     * previously not existent parts of an image.
     * 
     * @param img
     *            the image to mark
     * @param startW
     *            the width from which to start marking
     * @param startH
     *            the height from which the marking starts
     * @return the marked image
     */
    protected static BufferedImage markImageBorders(final BufferedImage img, final int startW, final int startH)
    {
        final BufferedImage copy = copyImage(img);

        final Color markTransparentWhite = new Color(255, 255, 255, 0);
        final Graphics2D g = copy.createGraphics();

        g.setColor(markTransparentWhite);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1.0f));

        if (startW < copy.getWidth())
        {
            g.fillRect(startW, 0, copy.getWidth() - startW, copy.getHeight());
        }
        if (startH < copy.getHeight())
        {
            g.fillRect(0, startH, copy.getWidth(), copy.getHeight() - startH);
        }
        g.dispose();

        return copy;
    }
}
