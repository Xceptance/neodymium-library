package com.xceptance.neodymium.tests.visual.pixel.exact;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.xceptance.neodymium.tests.visual.pixel.ImageTest;
import com.xceptance.neodymium.tests.visual.pixel.TestComparator;
import com.xceptance.neodymium.visual.image.algorithm.ComparisonAlgorithm;
import com.xceptance.neodymium.visual.image.algorithm.ExactMatch;
import com.xceptance.neodymium.visual.image.util.RectangleMask;

public class ExactMatchTest extends ImageTest
{
    ComparisonAlgorithm a = new ExactMatch();

    // how should a difference maSked during training
    RectangleMask m = new RectangleMask(10, 10);

    // how difference should maRked in difference file
    int mX = 10;

    int mY = 10;

    TestComparator T;

    @Before
    public void setup()
    {
        T = new TestComparator(a, m, mX, mY);
    }

    // TODO find reason for failure
    @Test
    @Ignore
    public void blank()
    {
        T.match("exact/blank.png").to("exact/blank.png").isEqual().hasNoMarking();
    }

    // TODO find reason for failure
    @Test
    @Ignore
    public void photo()
    {
        T.match("exact/photo.png").to("exact/photo.png").isEqual().hasNoMarking();
    }

    // TODO find reason for failure
    @Test
    @Ignore
    public void photoSameButDifferentFile()
    {
        T.match("exact/photo.png").to("exact/photo2.png").isEqual().hasNoMarking();
    }

    @Test
    public void noMatchPixelDiff()
    {
        T.match("exact/blank.png").to("exact/oneblackpixel.png").isNotEqual().hasMarking("exact/noMatchPixelDiff.png");
    }

    @Test
    public void noMatchDifferentSize()
    {
        T.match("exact/photo.png").to("exact/photo-205x205.png").isNotEqual().hasMarking("exact/photo-205x205-MaskExpected.png");
    }

    @Test
    public void noMatchDifferentSizeReversed()
    {
        T.match("exact/photo-205x205.png").to("exact/photo.png").isNotEqual().hasMarking("exact/photo-205x205-ReversedMaskExpected.png");
    }

    @Test
    public void noMatchNegated()
    {
        T.match("exact/blank.png").to("exact/negated-blank.png").isNotEqual().hasMarking("exact/negated-MaskExpected.png");
    }
}
