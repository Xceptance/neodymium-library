package com.xceptance.neodymium.junit4.tests.visual.pixel.fuzzy;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.neodymium.junit4.tests.visual.pixel.ImageTest;
import com.xceptance.neodymium.junit4.tests.visual.pixel.TestComparator;
import com.xceptance.neodymium.visual.image.algorithm.ComparisonAlgorithm;
import com.xceptance.neodymium.visual.image.algorithm.PixelFuzzy;
import com.xceptance.neodymium.visual.image.util.RectangleMask;

public class FuzzyTest extends ImageTest
{
    ComparisonAlgorithm a = new PixelFuzzy(0.1, 0.1, 10);

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

    /**
     * Test default, no difference
     * 
     * @throws IOException
     */
    @Test
    public void sameSimple()
    {
        T.match("fuzzy/blank.png").to("fuzzy/blank.png").isEqual();
    }

    /**
     * Test default, no difference
     * 
     * @throws IOException
     */
    @Test
    public void samePhoto()
    {
        T.match("fuzzy/photo.png").to("fuzzy/photo.png").isEqual();
    }

    /**
     * Test default, no difference limit reached
     * 
     * @throws IOException
     */
    @Test
    public void diffPixel_01_10of100()
    {
        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-10diff.png").isEqual();
    }

    /**
     * Test default, difference limit reached
     * 
     * @throws IOException
     */
    @Test
    public void diffPixel_01_11of100()
    {
        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-11diff.png").isNotEqual().hasMarking("fuzzy/diffPixel_01_11of100.png");
    }

    /**
     * Test default, color difference limit not reached
     * 
     * @throws IOException
     */
    @Test
    public void diffPixel_01_10of100_colorUnderLimit()
    {
        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-10diff-colorUnderLimit.png").isEqual();
    }

    /**
     * Test default, no difference limit reached
     * 
     * @throws IOException
     */
    @Test
    public void diffPixel_01_11of100_colorUnderLimit()
    {
        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-11diff-colorUnderLimit.png").isEqual();
    }

    /**
     * Test default, no difference limit reached
     * 
     * @throws IOException
     */
    @Test
    public void diffPixel_01_100of100_colorUnderLimit()
    {
        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-100diff-colorUnderLimit.png").isEqual();
    }

    /**
     * Test default, no difference limit reached
     * 
     * @throws IOException
     */
    @Test
    public void diffPixel_01_100of100_colorUnderLimit_10of100_over()
    {
        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-100of100_colorUnderLimit_10of100_over.png").isEqual();
    }

    /**
     * Test default, no difference limit reached
     * 
     * @throws IOException
     */
    @Test
    public void diffPixel_01_100of100_colorUnderLimit_11of100_over()
    {
        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-100of100_colorUnderLimit_11of100_over.png").isNotEqual();
    }

    /**
     * Test no tolerance
     * 
     * @throws IOException
     */
    @Test
    public void pixel_00_color_00()
    {
        T = new TestComparator(new PixelFuzzy(0, 0, 10), m, mX, mY);

        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10.png").isEqual();
    }

    /**
     * Test no tolerance, one pixel diff
     * 
     * @throws IOException
     */
    @Test
    public void pixel_00_color_00_1_pixeldiff()
    {
        T = new TestComparator(new PixelFuzzy(0, 0, 10), m, mX, mY);

        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-1diff-coloroverlimit.png").isNotEqual();
    }

    /**
     * Test no tolerance, one pixel diff, very low diff
     * 
     * @throws IOException
     */
    @Test
    public void pixel_00_color_00_1_pixeldiff_low_color()
    {
        T = new TestComparator(new PixelFuzzy(0, 0, 10), m, mX, mY);

        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-1diff-colordiffverylowlimit.png").isNotEqual();
    }

    /**
     * Test more tolerance but on a smaller area
     * 
     * @throws IOException
     */
    @Test
    public void pixel_02_color_02_dimension_3_noDifference()
    {
        T = new TestComparator(new PixelFuzzy(0.1, 0.1, 10), m, 3, 3);

        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10.png").isEqual();
    }

    /**
     * Test more tolerance but on a smaller area
     * 
     * @throws IOException
     */
    @Test
    public void pixel_02_color_02_dimension_3_difference_1_pixel()
    {
        // 0.9 pixels diff
        T = new TestComparator(new PixelFuzzy(0.1, 0.1, 3), m, mX, mY);
        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-1pixel.png").isNotEqual();

        // 1/9 pixels diff
        T = new TestComparator(new PixelFuzzy(0.11, 0.1, 3), m, mX, mY);
        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-1pixel.png").isNotEqual();

        // > 1/9 pixels diff
        T = new TestComparator(new PixelFuzzy(0.12, 0.1, 3), m, mX, mY);
        T.match("fuzzy/white-10x10.png").to("fuzzy/white-10x10-1pixel.png").isEqual();

    }
}
