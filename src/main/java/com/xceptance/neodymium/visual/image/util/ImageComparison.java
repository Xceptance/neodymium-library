package com.xceptance.neodymium.visual.image.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import org.junit.Assert;

import com.xceptance.neodymium.visual.image.algorithm.ComparisonAlgorithm;

public class ImageComparison
{
    private Point[] lastDifferences = null;

    private BufferedImage reference;

    private BufferedImage lastCompareImage;

    private boolean resized = false;

    /**
     * Creates a new instance of ImageComparison that uses the given reference image
     * 
     * @param reference
     *            Reference image for all comparison functions
     */
    public ImageComparison(final BufferedImage reference)
    {
        this.reference = reference;
    }

    /**
     * Checks whether two images can be considered equal as determined by the given algorithm
     * 
     * @param compareImage
     *            The image that is compared to the reference image
     * @param mask
     *            The mask that sets the dynamic content areas, which are ignored in the comparison
     * @param algorithm
     *            The algorithm with which the assertion is calculated
     * @return true if the two images are calculated as equal, false if not
     */
    public boolean isEqual(final BufferedImage compareImage, final BufferedImage mask, final ComparisonAlgorithm algorithm)
    {
        resized = false;

        lastCompareImage = ImageHelper.copyImage(compareImage);
        BufferedImage maskCopy = ImageHelper.copyImage(mask);

        final int maxWidth = Math.max(reference.getWidth(), lastCompareImage.getWidth());
        final int maxHeight = Math.max(reference.getHeight(), lastCompareImage.getHeight());

        final int minWidth = Math.min(reference.getWidth(), lastCompareImage.getWidth());
        final int minHeight = Math.min(reference.getHeight(), lastCompareImage.getHeight());

        if (maxWidth != minWidth || maxHeight != minHeight)
        {
            resized = true;
            reference = ImageHelper.adaptImageSize(reference, maxWidth, maxHeight);
            lastCompareImage = ImageHelper.adaptImageSize(lastCompareImage, maxWidth, maxHeight);
            maskCopy = ImageHelper.adaptImageSize(maskCopy, maxWidth, maxHeight);

        }

        final BufferedImage maskedReference = ImageHelper.overlayMaskImage(reference, maskCopy, ImageHelper.BLACK.getRGB());
        final BufferedImage maskedCompareImage = ImageHelper.overlayMaskImage(lastCompareImage, maskCopy, ImageHelper.BLACK.getRGB());

        switch (algorithm.getType())
        {
            case EXACTMATCH:
                lastDifferences = ImageHelper.compareImages(maskedReference, maskedCompareImage);
                break;

            case COLORFUZZY:
                lastDifferences = ImageHelper.colorFuzzyCompare(maskedReference, maskedCompareImage, algorithm.getColorTolerance());
                break;

            case PIXELFUZZY:
                lastDifferences = ImageHelper.fuzzyCompare(maskedReference, maskedCompareImage, algorithm.getColorTolerance(),
                                                           algorithm.getPixelTolerance(), algorithm.getFuzzyBlockSize());
                break;
        }

        if (lastDifferences == null)
        {
            Assert.fail("The dimensions of the two images don't match!");
        }

        return lastDifferences.length == 0;

    }

    /**
     * Checks whether two images can be considered equal as determined by the given algorithm
     * 
     * @param compareImage
     *            The image that is compared to the reference image
     * @param mask
     *            The mask as instance of MaskImage that sets the dynamic content areas, which are ignored in the
     *            comparison
     * @param algorithm
     *            The algorithm with which the assertion is calculated
     * @return true if the two images are calculated as equal, false if not
     */
    public boolean isEqual(final BufferedImage compareImage, final MaskImage mask, final ComparisonAlgorithm algorithm)
    {
        return isEqual(compareImage, mask.getMask(), algorithm);
    }

    /**
     * Creates a copy of the originally with isEqual tested image in which the found differences are highlighted in a
     * different color scheme
     * 
     * @param markingSizeX
     *            The size of the marking on the x axis
     * @param markingSizeY
     *            The size of the marking of the y axis
     * @return BufferedImage with the originally found differences highlighted
     */
    public BufferedImage getMarkedImageWithAMarker(final int markingSizeX, final int markingSizeY)
    {
        return ImageHelper.markDifferencesWithAMarker(lastCompareImage, lastDifferences, markingSizeX, markingSizeY);
    }

    /**
     * Creates a copy of the originally with isEqual tested image in which the found differences are marked with red
     * boxes.
     * 
     * @param markingSizeX
     *            The size of the marking on the x axis
     * @param markingSizeY
     *            The size of the marking of the y axis
     * @return BufferedImage with the originally found differences marked with boxes
     */
    public BufferedImage getMarkedImageWithBoxes(final int markingSizeX, final int markingSizeY)
    {
        return ImageHelper.markDifferencesWithBoxes(lastCompareImage, lastDifferences, markingSizeX, markingSizeY);
    }

    /**
     * Creates a new image in which only the found differences are displayed on a black background. The differences are
     * drawn in the exact locations where they were found in the original image
     * 
     * @return BufferedImage with differences in grey on a black background
     */
    public BufferedImage getDifferenceImage()
    {
        if (lastDifferences.length == 0)
            return null;

        // create a difference picture based on reference and paint it black
        BufferedImage difference = ImageHelper.createPlainImage(reference, Color.BLACK);

        // mark differences in greyscale
        Color greyscale;
        int x, y, diffColor;
        double pixelColorDiff;
        for (final Point point : lastDifferences)
        {
            x = point.x;
            y = point.y;
            pixelColorDiff = ImageHelper.calculatePixelRGBDiff(reference.getRGB(x, y), lastCompareImage.getRGB(x, y));

            diffColor = (int) Math.round(255 * pixelColorDiff);
            greyscale = new Color(diffColor, diffColor, diffColor, 255);
            difference.setRGB(x, y, greyscale.getRGB());
        }

        // draw borders on the differences if compared images differed in size
        if (resized)
            difference = ImageHelper.markImageBorders(difference, 0, 0);

        return difference;
    }
}
