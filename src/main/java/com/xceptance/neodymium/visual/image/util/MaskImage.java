package com.xceptance.neodymium.visual.image.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.sun.istack.internal.Nullable;
import com.xceptance.neodymium.visual.image.algorithm.ComparisonAlgorithm;

/**
 * Mask image that is used in the comparison of two pictures. The mask is trained with permitted differences between the
 * two pictures, so that those difference will be ignored in the actual comparison.
 */
public class MaskImage
{
    private final BufferedImage reference;

    private BufferedImage mask;

    /**
     * Initializes a mask with the reference image, that is used to train it and the mask image itself.
     * 
     * @param referenceImage
     *            The reference image for the mask training
     * @param maskImage
     *            The mask image, @Nullable -> Creates a blank mask image with the dimensions of the reference image
     */
    public MaskImage(final BufferedImage referenceImage, @Nullable final BufferedImage maskImage)
    {
        this.reference = ImageHelper.copyImage(referenceImage);

        if (maskImage == null)
        {
            // create a new mask with same dimensions as reference image
            this.mask = new BufferedImage(referenceImage.getWidth(), referenceImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // fill the mask with transparent white
            this.mask = ImageHelper.createPlainImage(this.mask, ImageHelper.WHITE_TRANSPARENT);
        }
        else
        {
            this.mask = ImageHelper.copyImage(maskImage);
        }
    }

    /**
     * Initializes the mask with the given reference image, a blank mask image is created with the dimensions of the
     * reference image. <br>
     * Calls MaskImage(referenceImage, null)<br>
     * 
     * @param referenceImage
     *            The reference image for the mask training
     */
    public MaskImage(final BufferedImage referenceImage)
    {
        this(referenceImage, null);
    }

    /**
     * Returns the mask image
     * 
     * @return mask image as BufferedImage
     */
    public BufferedImage getMask()
    {
        return ImageHelper.copyImage(mask);
    }

    /**
     * Trains the mask on the differences between the reference and the given image with the differences calculated by
     * the algorithm. The mask already holds the reference image for comparison.
     * 
     * @param image
     *            The image to compare the reference two
     * @param algorithm
     *            The algorithm that calculates the differences between the two images
     * @param markerMask
     *            The size of the area that will be marked around a detected difference
     */
    public void train(final BufferedImage image, final ComparisonAlgorithm algorithm, final RectangleMask markerMask)
    {
        Point[] differences = null;

        switch (algorithm.getType())
        {
            case PIXELFUZZY:
                differences = ImageHelper.fuzzyCompare(reference, image, algorithm.getColorTolerance(),
                                                       algorithm.getPixelTolerance(), algorithm.getFuzzyBlockSize());
                break;

            case COLORFUZZY:
                differences = ImageHelper.colorFuzzyCompare(reference, image, algorithm.getColorTolerance());
                break;

            case EXACTMATCH:
                differences = ImageHelper.compareImages(reference, image);
                break;
        }

        mask = maskDifferences(mask, differences, markerMask, ImageHelper.BLACK);
    }

    /**
     * Very close to markDifferences. Goes through every pixel that was different and masks the marking block it is in,
     * unless it was marked already. Works directly on the mask image.
     * 
     * @param pixels
     *            pixel positions of the pixels that where detected as different
     * @return A BufferedImage in which the pixels at the given positions have been marked in BLACK
     */
    private BufferedImage maskDifferences(final BufferedImage image, final Point[] pixels,
                                          final RectangleMask markerMask, final Color maskingColor)
    {
        final BufferedImage copy = ImageHelper.copyImage(image);

        if (pixels == null)
            return copy;

        final Graphics2D g = copy.createGraphics();
        g.setColor(maskingColor);

        for (Point pixel : pixels)
        {
            int x = Math.max(0, pixel.x - markerMask.getXDistance());
            int y = Math.max(0, pixel.y - markerMask.getYDistance());

            g.fillRect(x, y, markerMask.getWidth(), markerMask.getHeight());
        }
        g.dispose();

        return copy;
    }

    /**
     * Closes the mask to better cover an area that is allowed to be different
     * 
     * @param structureElementWidth
     *            Width of the structure element
     * @param structureElementHeight
     *            Height of the structure element
     */
    public void closeMask(final int structureElementWidth, final int structureElementHeight)
    {
        mask = ImageHelper.closeImage(mask, structureElementWidth, structureElementHeight, ImageHelper.BLACK.getRGB(),
                                      ImageHelper.WHITE_TRANSPARENT.getRGB());
    }
}
