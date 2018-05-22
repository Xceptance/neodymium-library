package com.xceptance.neodymium.tests.visual.pixel;

import java.awt.image.BufferedImage;
import java.text.MessageFormat;

import org.junit.Assert;

import com.xceptance.neodymium.visual.image.algorithm.ComparisonAlgorithm;
import com.xceptance.neodymium.visual.image.algorithm.ExactMatch;
import com.xceptance.neodymium.visual.image.util.ImageComparison;
import com.xceptance.neodymium.visual.image.util.MaskImage;
import com.xceptance.neodymium.visual.image.util.RectangleMask;

public class TestComparator extends ImageTest
{
    private final RectangleMask differenceMarker;

    private final ComparisonAlgorithm algorithm;

    private ImageComparison comperator;

    private MaskImage masker;

    private BufferedImage referenceImage;

    private BufferedImage comparisonImage;

    private int markingSizeY;

    private int markingSizeX;

    public TestComparator()
    {
        this.algorithm = new ExactMatch();
        this.differenceMarker = new RectangleMask(10, 10);
    }

    public TestComparator(final ComparisonAlgorithm algorithm, final RectangleMask differenceMarker, final int markingSizeX, final int markingSizeY)
    {
        this.algorithm = algorithm;
        this.differenceMarker = differenceMarker;
        this.markingSizeX = markingSizeX;
        this.markingSizeY = markingSizeY;
    }

    public TestComparator match(final String referenceImageFile)
    {
        return match(load(referenceImageFile));
    }

    public TestComparator match(final BufferedImage referenceImage)
    {
        this.referenceImage = referenceImage;
        masker = new MaskImage(this.referenceImage);

        return this;
    }

    public TestComparator to(final String comparisonImageFile)
    {
        return to(load(comparisonImageFile));
    }

    public TestComparator to(final BufferedImage comparisonImage)
    {
        this.comparisonImage = comparisonImage;
        comperator = new ImageComparison(referenceImage);

        return this;
    }

    public TestComparator isEqual()
    {
        Assert.assertTrue(comperator.isEqual(comparisonImage, masker, algorithm));
        return this;
    }

    public TestComparator isNotEqual()
    {
        Assert.assertFalse(comperator.isEqual(comparisonImage, masker, algorithm));
        return this;
    }

    public TestComparator train(final String trainImageFile)
    {
        return train(load(trainImageFile));
    }

    public TestComparator train(final BufferedImage trainImage)
    {
        masker.train(trainImage, algorithm, differenceMarker);
        return this;
    }

    public TestComparator hasMarking(final String markingFile)
    {
        return hasMarking(load(markingFile));
    }

    public TestComparator hasMarking(final BufferedImage marking)
    {
        final BufferedImage comperatorDifference = comperator.getMarkedImageWithBoxes(markingSizeX, markingSizeY);

        final long now = System.currentTimeMillis();
        writeToTmp(comperatorDifference, MessageFormat.format("actual.{0}.png", String.valueOf(now)));
        writeToTmp(marking, MessageFormat.format("expected.{0}.png", String.valueOf(now)));
        Assert.assertTrue(imageEqual(comperatorDifference, marking));

        return this;
    }

    public TestComparator hasNoMarking()
    {
        final BufferedImage comperatorDifference = comperator.getMarkedImageWithBoxes(markingSizeX, markingSizeY);
        Assert.assertNull(comperatorDifference);

        return this;
    }

    public MaskImage getMasker()
    {
        return masker;
    }

    public ImageComparison getComperator()
    {
        return comperator;
    }
}
