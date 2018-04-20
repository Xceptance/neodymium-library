// Catalano Imaging Library
// The Catalano Framework
//
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

/**
 * Image Histogram for random values.
 * 
 * @author Diego Catalano edited by Thomas Volkmann
 */
public class ImageHistogram implements Serializable
{
    /**
     * Auto generated serial number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Compare two histograms values to each other.
     * 
     * @param histA
     *            Integer array of all values.
     * @param histB
     *            Integer array of all values.
     * @return f Integer array with all equal entries from both parameters.
     */
    public static int[] MatchHistograms(int[] histA, int[] histB)
    {
        int length = histA.length;
        double[] PA = CDF(histA);
        double[] PB = CDF(histB);
        int[] f = new int[length];

        for (int a = 0; a < length; a++)
        {
            int j = length - 1;
            do
            {
                f[a] = j;
                j--;
            }
            while (j >= 0 && PA[a] <= PB[j]);
        }
        return f;
    }

    /**
     * Compare two histograms to each other.
     * 
     * @param histA
     *            ImageHistogram first histogram.
     * @param histB
     *            ImageHistogram second histogram.
     * @return {@link #MatchHistograms(int[], int[])}
     */
    public static int[] MatchHistograms(ImageHistogram histA, ImageHistogram histB)
    {
        return MatchHistograms(histA.values, histB.values);
    }

    /**
     * Cumulative distribution function of values.
     * 
     * @param values
     *            Integer array of all values.
     * @return p Double array result of the CDF.
     */
    public static double[] CDF(int[] values)
    {
        int length = values.length;
        int n = 0;

        for (int i = 0; i < length; i++)
        {
            n += values[i];
        }

        double[] p = new double[length];
        int c = values[0];
        p[0] = (double) c / n;
        for (int i = 1; i < length; i++)
        {
            c += values[i];
            p[i] = (double) c / n;
        }
        return p;
    }

    /**
     * Cumulative distribution function of the histogram.
     * 
     * @param hist
     *            ImageHistogram
     * @return {@link #CDF(int[])}
     */
    public static double[] CDF(ImageHistogram hist)
    {
        return CDF(hist.values);
    }

    /**
     * Normalize histogram.
     * 
     * @param values
     *            Values.
     * @return Normalized histogram.
     */
    public static double[] Normalize(int[] values)
    {
        int sum = 0;
        for (int i = 0; i < values.length; i++)
        {
            sum += values[i];
        }

        double[] norm = new double[values.length];
        for (int i = 0; i < norm.length; i++)
        {
            norm[i] = values[i] / (double) sum;
        }
        return norm;
    }

    /**
     * Initializes a new instance of the Histogram class.
     * 
     * @param values
     *            Values.
     */
    public ImageHistogram(int[] values)
    {
        this.values = values;
        update();
    }

    /**
     * Get values of the histogram.
     * 
     * @return values Integer array of all values.
     */
    public int[] getValues()
    {
        return values;
    }

    /**
     * Get mean value.
     * 
     * @return mean.
     */
    public double getMean()
    {
        return mean;
    }

    /**
     * Get standard deviation value.
     * 
     * @return stdDev.
     */
    public double getStdDev()
    {
        return stdDev;
    }

    /**
     * Get entropy value.
     * 
     * @return entropy.
     */
    public double getEntropy()
    {
        return entropy;
    }

    /**
     * Get kurtosis value.
     * 
     * @return Kurtosis.
     */
    public double getKurtosis()
    {
        return kurtosis;
    }

    /**
     * Get skewness value.
     * 
     * @return Skewness.
     */
    public double getSkewness()
    {
        return skewness;
    }

    /**
     * Get median value.
     * 
     * @return Median.
     */
    public int getMedian()
    {
        return median;
    }

    /**
     * Get mode value.
     * 
     * @return Mode.
     */
    public int getMode()
    {
        return mode;
    }

    /**
     * Get minimum value.
     * 
     * @return Minimum.
     */
    public int getMin()
    {
        return min;
    }

    /**
     * Get maximum value.
     * 
     * @return Maximum.
     */
    public int getMax()
    {
        return max;
    }

    /**
     * Get the sum of pixels.
     * 
     * @return total
     */
    public long getTotal()
    {
        return total;
    }

    /**
     * Update histogram.
     */
    private void update()
    {
        total = 0;
        for (int i = 0; i < values.length; i++)
        {
            total += values[i];
        }

        mean = HistogramStatistics.Mean(values);
        // stdDev = HistogramStatistics.StdDev( values, mean );
        // kurtosis = HistogramStatistics.Kurtosis(values, mean, stdDev);
        // skewness = HistogramStatistics.Skewness(values, mean, stdDev);
        // median = HistogramStatistics.Median( values );
        // mode = HistogramStatistics.Mode(values);
        // entropy = HistogramStatistics.Entropy(values);
    }

    /**
     * Normalize histogram.
     * 
     * @return Normalized histogram.
     */
    public double[] Normalize()
    {
        double[] h = new double[values.length];
        for (int i = 0; i < h.length; i++)
        {
            h[i] = values[i] / (double) total;
        }
        return h;
    }

    // Array for all values.
    private int[] values;

    // mean value
    private double mean = 0;

    // Standard derivation value.
    private double stdDev = 0;

    // Entropy value.
    private double entropy = 0;

    // Kurtosis value.
    private double kurtosis = 0;

    // Skewness value.
    private double skewness = 0;

    // Median value.
    private int median = 0;

    // Mode value.
    private int mode;

    // Minimum value.
    private int min;

    // Maximum value.
    private int max;

    // Total amount of elements.
    private long total;
}