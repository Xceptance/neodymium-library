package com.xceptance.neodymium.visual.image.algorithm;

public class ComparisonAlgorithm
{
    private ComparisonType type;

    double colorTolerance;

    double pixelTolerance;

    int fuzzyBlockSize;

    protected ComparisonAlgorithm(ComparisonType type, double pixelTolerance, double colorTolerance, int fuzzyBlockSize)
    {
        this.type = type;
        switch (type)
        {
            case COLORFUZZY:
                this.colorTolerance = colorTolerance;
                break;

            case PIXELFUZZY:
                this.colorTolerance = colorTolerance;
                this.pixelTolerance = pixelTolerance;
                this.fuzzyBlockSize = fuzzyBlockSize;
                break;

            case EXACTMATCH:
                break;
        }
    }

    public ComparisonType getType()
    {
        return type;
    }

    public int getFuzzyBlockSize()
    {
        return fuzzyBlockSize;
    }

    public double getColorTolerance()
    {
        return colorTolerance;
    }

    public double getPixelTolerance()
    {
        return pixelTolerance;
    }
}
