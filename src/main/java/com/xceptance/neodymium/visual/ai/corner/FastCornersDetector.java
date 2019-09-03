// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

package com.xceptance.neodymium.visual.ai.corner;

import java.util.List;

import com.xceptance.neodymium.visual.ai.core.FeaturePoint;
import com.xceptance.neodymium.visual.ai.image.FastBitmap;

/**
 * Features from Accelerated Segment Test (FAST) corners detector.
 * 
 * @author Diego Catalano edited by Thomas Volkmann
 */
public class FastCornersDetector implements ICornersFeatureDetector
{
    public static enum Algorithm
    {
        FAST_9
    };

    /**
     * Get Threshold. A number denoting how much brighter or darker the pixels surrounding the pixel in question should
     * be in order to be considered a corner.
     * 
     * @return Threshold.
     */
    public int getThreshold()
    {
        return threshold;
    }

    /**
     * Set Threshold. A number denoting how much brighter or darker the pixels surrounding the pixel in question should
     * be in order to be considered a corner.
     * 
     * @param threshold
     *            Threshold.
     */
    public void setThreshold(int threshold)
    {
        this.threshold = threshold;
    }

    /**
     * Check if needs apply a non-maximum suppression algorithm on the results, to allow only maximal corners.
     * 
     * @return If true, allow only maximal corners, otherwise false.
     */
    public boolean isSuppressed()
    {
        return suppress;
    }

    /**
     * Set suppression if needs apply a non-maximum suppression algorithm on the results, to allow only maximal corners.
     * 
     * @param suppress
     *            If true, allow only maximal corners, otherwise false.
     */
    public void setSuppression(boolean suppress)
    {
        this.suppress = suppress;
    }

    /**
     * Get Fast algorithm.
     * 
     * @return Fast algorithm.
     */
    public Algorithm getAlgorithm()
    {
        return algorithm;
    }

    /**
     * Set Fast algorithm.
     * 
     * @param algorithm
     *            Fast algorithm.
     */
    public void setAlgorithm(Algorithm algorithm)
    {
        this.algorithm = algorithm;
    }

    /**
     * Initializes a new instance of the FastCornersDetector class.
     */
    public FastCornersDetector()
    {
    }

    /**
     * Initializes a new instance of the FastCornersDetector class.
     * 
     * @param threshold
     *            Threshold.
     */
    public FastCornersDetector(int threshold)
    {
        this.threshold = threshold;
    }

    /**
     * Initializes a new instance of the FastCornersDetector class.
     * 
     * @param algorithm
     *            Algorithm.
     */
    public FastCornersDetector(Algorithm algorithm)
    {
        this.algorithm = algorithm;
    }

    /**
     * Initializes a new instance of the FastCornersDetector class.
     * 
     * @param threshold
     *            Threshold.
     * @param suppress
     *            Suppress.
     */
    public FastCornersDetector(int threshold, boolean suppress)
    {
        this.threshold = threshold;
        this.suppress = suppress;
    }

    /**
     * Initializes a new instance of the FastCornersDetector class.
     * 
     * @param threshold
     *            Threshold.
     * @param suppress
     *            Suppress.
     * @param algorithm
     *            Algorithm.
     */
    public FastCornersDetector(int threshold, boolean suppress, Algorithm algorithm)
    {
        this.threshold = threshold;
        this.suppress = suppress;
        this.algorithm = algorithm;
    }

    @Override
    public List<FeaturePoint> ProcessImage(FastBitmap fastBitmap)
    {
        Fast9 fast9 = new Fast9(threshold, suppress);
        return fast9.ProcessImage(fastBitmap);
    }

    /***
     * Integer value for the algorithm threshold.
     */
    private int threshold = 40;

    /***
     * Boolean flag for standard settings of suppress.
     */
    private boolean suppress = true;

    /***
     * Standard choose of the enum Algorithm.
     */
    private Algorithm algorithm = Algorithm.FAST_9;
}