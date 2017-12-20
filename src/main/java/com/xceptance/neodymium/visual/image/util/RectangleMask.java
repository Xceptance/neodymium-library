package com.xceptance.neodymium.visual.image.util;

/**
 * Represents a mask in form of a rectangle given by width and height.
 */
public class RectangleMask
{
    private int width;

    private int height;

    /**
     * Constructs a RectangleMask with the given width and height.
     * 
     * @param width
     *            Width of the rectangle
     * @param height
     *            Height of the rectangle
     */
    public RectangleMask(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    /**
     * Returns the relative x distance to the middle of the mask
     * 
     * @return width/2 as int
     */
    public int getXDistance()
    {
        return width / 2;
    }

    /**
     * Returns the relative y distance to the middle of the mask
     * 
     * @return height/2 as int
     */
    public int getYDistance()
    {
        return height / 2;
    }
}
