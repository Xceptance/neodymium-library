// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//
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

package com.xceptance.neodymium.visual.ai.image;

import java.io.Serializable;

import com.xceptance.neodymium.visual.ai.core.FeaturePoint;

/**
 * Gather statistics about image in Gray or RGB color space.
 * 
 * @author Diego Catalano edited by Thomas Vokmann
 */
public class ImageStatistics implements Serializable
{
    /**
     * Auto generated serial number.
     */
    private static final long serialVersionUID = 1L;

    private ImageHistogram gray;

    private ImageHistogram red;

    private ImageHistogram green;

    private ImageHistogram blue;

    private int pixels;

    /**
     * Image histogram of gray channel.
     * 
     * @return Histogram.
     */
    public ImageHistogram getHistogramGray()
    {
        if (gray == null)
            throw new IllegalArgumentException("Histogram gray is null");

        return gray;
    }

    /**
     * Image histogram of red channel.
     * 
     * @return Histogram.
     */
    public ImageHistogram getHistogramRed()
    {
        if (red == null)
            throw new IllegalArgumentException("Histogram red is null");

        return red;
    }

    /**
     * Image histogram of green channel.
     * 
     * @return Histogram.
     */
    public ImageHistogram getHistogramGreen()
    {
        if (green == null)
            throw new IllegalArgumentException("Histogram green is null");

        return green;
    }

    /**
     * Image histogram of blue channel.
     * 
     * @return Histogram.
     */
    public ImageHistogram getHistogramBlue()
    {
        if (blue == null)
            throw new IllegalArgumentException("Histogram blue is null");

        return blue;
    }

    /**
     * Initialize a new instance of the ImageStatistics class.
     * 
     * @param fastBitmap
     *            Image to be processed.
     */
    public ImageStatistics(FastBitmap fastBitmap)
    {
        this(fastBitmap, 256);
    }

    /**
     * Initialize a new instance of the ImageStatistics class. Calculate all necessary values for the histograms in the
     * range to min and max {@link FeaturePoint} from the group.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @param bins
     *            Number of bins.
     * @param min
     *            Minimum {@link FeaturePoint} from the found group.
     * @param max
     *            Maximum {@link FeaturePoint} from the found group.
     */
    public ImageStatistics(FastBitmap fastBitmap, int bins, FeaturePoint min, FeaturePoint max)
    {
        pixels = 0;
        red = green = blue = gray = null;

        int width = max.x - min.x;
        int height = max.y - min.y;

        int size = width * height;
        if (fastBitmap.isGrayscale())
        {
            int[] g = new int[bins];

            int G;

            for (int i = min.x; i < size; i++)
            {
                G = fastBitmap.getGray(i);

                g[G * bins / 256]++;
                pixels++;
            }

            gray = new ImageHistogram(g);

        }
        // generate the histograms for each canal
        else if (fastBitmap.isRGB())
        {
            int[] r = new int[bins];
            int[] g = new int[bins];
            int[] b = new int[bins];

            int R, G, B;

            for (int i = min.x; i < size; i++)
            {
                R = fastBitmap.getRed(i);
                G = fastBitmap.getGreen(i);
                B = fastBitmap.getBlue(i);

                r[R * bins / 256]++;
                g[G * bins / 256]++;
                b[B * bins / 256]++;
                pixels++;
            }
            red = new ImageHistogram(r);
            green = new ImageHistogram(g);
            blue = new ImageHistogram(b);
        }
    }

    /**
     * Initialize a new instance of the ImageStatistics class.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @param bins
     *            Number of bins.
     */
    public ImageStatistics(FastBitmap fastBitmap, int bins)
    {
        pixels = 0;
        red = green = blue = gray = null;

        int size = fastBitmap.getWidth() * fastBitmap.getHeight();
        if (fastBitmap.isGrayscale())
        {
            int[] g = new int[bins];

            int G;

            for (int i = 0; i < size; i++)
            {
                G = fastBitmap.getGray(i);

                g[G * bins / 256]++;
                pixels++;
            }

            gray = new ImageHistogram(g);

        }
        // generate the histograms for each canal
        else if (fastBitmap.isRGB())
        {
            int[] r = new int[bins];
            int[] g = new int[bins];
            int[] b = new int[bins];

            int R, G, B;

            for (int i = 0; i < size; i++)
            {
                R = fastBitmap.getRed(i);
                G = fastBitmap.getGreen(i);
                B = fastBitmap.getBlue(i);

                r[R * bins / 256]++;
                g[G * bins / 256]++;
                b[B * bins / 256]++;
                pixels++;
            }
            red = new ImageHistogram(r);
            green = new ImageHistogram(g);
            blue = new ImageHistogram(b);
        }
    }

    /**
     * Count pixels.
     * 
     * @return amount of pixels.
     */
    public int PixelsCount()
    {
        return pixels;
    }

    /**
     * Calculate Mean value.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @return Mean.
     */
    public static float Mean(FastBitmap fastBitmap)
    {
        return Mean(fastBitmap, 0, 0, fastBitmap.getWidth(), fastBitmap.getHeight());
    }

    /**
     * Calculate Mean value.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @param startX
     *            Initial X axis coordinate.
     * @param startY
     *            Initial Y axis coordinate.
     * @param width
     *            Width.
     * @param height
     *            Height.
     * @return Mean.
     */
    public static float Mean(FastBitmap fastBitmap, int startX, int startY, int width, int height)
    {
        float mean = 0;
        if (fastBitmap.isGrayscale())
        {
            for (int i = startX; i < height; i++)
            {
                for (int j = startY; j < width; j++)
                {
                    mean += fastBitmap.getGray(i, j);
                }
            }
            return mean / (width * height);
        }
        else
        {
            throw new IllegalArgumentException("ImageStatistics: Only compute mean in grayscale images.");
        }
    }

    /**
     * Calculate Variance.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @return Variance.
     */
    public static float Variance(FastBitmap fastBitmap)
    {
        float mean = Mean(fastBitmap);
        return Variance(fastBitmap, mean);
    }

    /**
     * Calculate Variance.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @param mean
     *            Mean.
     * @return Variance.
     */
    public static float Variance(FastBitmap fastBitmap, float mean)
    {
        return Variance(fastBitmap, mean, 0, 0, fastBitmap.getWidth(), fastBitmap.getHeight());
    }

    /**
     * Calculate Variance.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @param mean
     *            Mean.
     * @param startX
     *            Initial X axis coordinate.
     * @param startY
     *            Initial Y axis coordinate.
     * @param width
     *            Width.
     * @param height
     *            Height.
     * @return Variance.
     */
    public static float Variance(FastBitmap fastBitmap, float mean, int startX, int startY, int width, int height)
    {
        float sum = 0;
        if (fastBitmap.isGrayscale())
        {
            for (int i = startX; i < height; i++)
            {
                for (int j = startY; j < width; j++)
                {
                    sum += Math.pow(fastBitmap.getGray(i, j) - mean, 2);
                }
            }
            return sum / (float) ((width * height) - 1);
        }
        else
        {
            throw new IllegalArgumentException("ImageStatistics: Only compute variance in grayscale images.");
        }
    }

    /**
     * Get maximum gray value in the image.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @return Maximum gray.
     */
    public static int Maximum(FastBitmap fastBitmap)
    {
        return Maximum(fastBitmap, 0, 0, fastBitmap.getWidth(), fastBitmap.getHeight());
    }

    /**
     * Get maximum gray value in the image.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @param startX
     *            Initial X axis coordinate.
     * @param startY
     *            Initial Y axis coordinate.
     * @param width
     *            Width.
     * @param height
     *            Height.
     * @return Maximum gray.
     */
    public static int Maximum(FastBitmap fastBitmap, int startX, int startY, int width, int height)
    {
        int max = 0;
        for (int i = startX; i < height; i++)
        {
            for (int j = startY; j < width; j++)
            {
                int gray = fastBitmap.getGray(i, j);
                if (gray > max)
                {
                    max = gray;
                }
            }
        }
        return max;
    }

    /**
     * Get minimum gray value in the image.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @return minimum gray.
     */
    public static int Minimum(FastBitmap fastBitmap)
    {
        return Minimum(fastBitmap, 0, 0, fastBitmap.getWidth(), fastBitmap.getHeight());
    }

    /**
     * Get minimum gray value in the image.
     * 
     * @param fastBitmap
     *            Image to be processed.
     * @param startX
     *            Initial X axis coordinate.
     * @param startY
     *            Initial Y axis coordinate.
     * @param width
     *            Width.
     * @param height
     *            Height.
     * @return Minimum gray.
     */
    public static int Minimum(FastBitmap fastBitmap, int startX, int startY, int width, int height)
    {
        int min = 255;
        for (int i = startX; i < height; i++)
        {
            for (int j = startY; j < width; j++)
            {
                int gray = fastBitmap.getGray(i, j);
                if (gray < min)
                {
                    min = gray;
                }
            }
        }
        return min;
    }
}