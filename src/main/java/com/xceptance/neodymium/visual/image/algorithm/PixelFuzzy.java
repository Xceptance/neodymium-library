package com.xceptance.neodymium.visual.image.algorithm;

public class PixelFuzzy extends ComparisonAlgorithm
{
    public PixelFuzzy(double pixelTolerance, double colorTolerance, int fuzzyBlockSize)
    {
        super(ComparisonType.PIXELFUZZY, pixelTolerance, colorTolerance, fuzzyBlockSize);
    }

    public PixelFuzzy()
    {
        super(ComparisonType.PIXELFUZZY, 0.1, 0.1, 10);
    }
}
