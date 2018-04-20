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
import java.util.ArrayList;

import com.xceptance.neodymium.visual.ai.core.FeaturePoint;
import com.xceptance.neodymium.visual.ai.core.FloatPoint;

/***
 * Representation of the merged metrics of different Images, which where analyzed and compared. This class represent one
 * found group and the comparison in an Image and in reference to all other images where this group exist. Through the
 * possibility to update and weight the seen data this class represent a part of machine learning.
 * 
 * @author Thomas Volkmann
 */
public class AverageMetric implements Serializable
{
    /**
     * Auto generated serial number.
     */
    private static final long serialVersionUID = 1L;

    /***
     * Constructor for the average metric.
     * 
     * @param GroupSize
     *            Size of the found group.
     * @param BoundingBox
     *            Amount of distance between the two points which are at the opposite side of the cloud.
     * @param DistMin
     *            Distance from the point closest to coordinate origin (0.0 Screen coordinates)
     * @param DistMax
     *            Distance from the point farthest away from coordinate origin (0.0 Screen coordinates)
     * @param CenterOfGrav
     *            Average center of all group elements.
     * @param histoRedMean
     *            Mean value for the red histogram.
     * @param histoGreenMean
     *            Mean value for the green histogram.
     * @param histoBlueMean
     *            Mean value for the blue histogram.
     */
    public AverageMetric(int GroupSize, double BoundingBox, double DistMin, double DistMax, FloatPoint CenterOfGrav, double histoRedMean, double histoGreenMean,
        double histoBlueMean)
    {
        averageGroupSize = GroupSize;
        averageBoundingBoxSize = BoundingBox;
        averageDistanceMin = DistMin;
        averageDistanceMax = DistMax;
        averageCenterOfGravity = CenterOfGrav;
        averageHistoRedMean = histoRedMean;
        averageHistoGreenMean = histoGreenMean;
        averageHistoBlueMean = histoBlueMean;
        boundingBox = new ArrayList<>();
        boundingBox.add(new FeaturePoint(0, 0));
        boundingBox.add(new FeaturePoint(0, 0));
        itemCounter = 2;
    }

    /**
     * Compute the average value in respective the already seen data.
     * 
     * @param GroupSize
     *            Size of the found group.
     * @param BoundingBox
     *            Amount of distance between the two points which are at the opposite side of the cloud.
     * @param DistMin
     *            Distance from the point closest to coordinate origin (0.0 Screen coordinates)
     * @param DistMax
     *            Distance from the point farthest away from coordinate origin (0.0 Screen coordinates)
     * @param CenterOfGrav
     *            Average center of all group elements.
     * @param histoRedMean
     *            Mean value for the red histogram.
     * @param histoGreenMean
     *            Mean value for the green histogram.
     * @param histoBlueMean
     *            Mean value for the blue histogram.
     */
    public void update(int GroupSize, double BoundingBox, double DistMin, double DistMax, FloatPoint CenterOfGrav, double histoRedMean, double histoGreenMean,
                       double histoBlueMean)
    {
        averageGroupSize += GroupSize;
        averageBoundingBoxSize += BoundingBox;
        averageDistanceMin += DistMin;
        averageDistanceMax += DistMax;
        averageHistoRedMean += histoRedMean;
        averageHistoGreenMean += histoGreenMean;
        averageHistoBlueMean += histoBlueMean;
        averageCenterOfGravity.Add(CenterOfGrav);

        averageGroupSize /= itemCounter;
        averageBoundingBoxSize /= itemCounter;
        averageDistanceMin /= itemCounter;
        averageDistanceMax /= itemCounter;
        averageHistoRedMean = (histoRedMean == 0 ? averageHistoRedMean : averageHistoRedMean / itemCounter);
        averageHistoGreenMean = (histoGreenMean == 0 ? averageHistoGreenMean : averageHistoGreenMean / itemCounter);
        averageHistoBlueMean = (histoBlueMean == 0 ? averageHistoBlueMean : averageHistoBlueMean / itemCounter);
        averageCenterOfGravity.Divide(itemCounter);
    }

    public void newBoundingBox(FeaturePoint min, FeaturePoint max)
    {
        boundingBox.set(0, new FeaturePoint(min.x, min.y));
        boundingBox.set(1, new FeaturePoint(max.x, max.y));
    }

    public void updateBoundingBox(FeaturePoint min, FeaturePoint max)
    {
        boundingBox.set(0, new FeaturePoint(((boundingBox.get(0).x + min.x) / itemCounter), (boundingBox.get(0).y + min.y) / itemCounter));
        boundingBox.set(1, new FeaturePoint(((boundingBox.get(1).x + max.x) / itemCounter), (boundingBox.get(1).y + max.y) / itemCounter));
    }

    public ArrayList<FeaturePoint> getBoundingBox()
    {
        return boundingBox;
    }

    /***
     * Get the average group size.
     * 
     * @return averageGroupSize.
     */
    public int getAverageGroupSize()
    {
        return averageGroupSize;
    }

    /***
     * Get the average bounding box.
     * 
     * @return averageBoundingBoxSize.
     */
    public double getAverageBoundingBoxSize()
    {
        return averageBoundingBoxSize;
    }

    /***
     * Get the average distance minimum.
     * 
     * @return averageDistanceMin.
     */
    public double getAverageDistanceMin()
    {
        return averageDistanceMin;
    }

    /***
     * Get the average distance maximum.
     * 
     * @return averageDistanceMax.
     */
    public double getAverageDistanceMax()
    {
        return averageDistanceMax;
    }

    /***
     * Get the average center of the group.
     * 
     * @return averageCenterOfGravity.
     */
    public FloatPoint getAverageCenterOfGravity()
    {
        return averageCenterOfGravity;
    }

    /***
     * Get the average mean histogram from all red values.
     * 
     * @return averageHistoRedMean.
     */
    public double getAverageHistogramRedMean()
    {
        return averageHistoRedMean;
    }

    /***
     * Get the average mean histogram from all green values.
     * 
     * @return averageHistoRedMean.
     */
    public double getAverageHistogramGreenMean()
    {
        return averageHistoGreenMean;
    }

    /***
     * Get the average mean histogram from all blue values.
     * 
     * @return averageHistoRedMean.
     */
    public double getAverageHistogramBlueMean()
    {
        return averageHistoBlueMean;
    }

    /***
     * Average group size value in int.
     */
    private int averageGroupSize;

    /***
     * Average bounding box size.
     */
    private double averageBoundingBoxSize;

    /***
     * Average minimum distance to coordinate origin (0.0 screen coordinates).
     */
    private double averageDistanceMin;

    /***
     * Average maximum distance to coordinate origin (0.0 screen coordinates).
     */
    private double averageDistanceMax;

    /***
     * Average center point of the group.
     */
    private FloatPoint averageCenterOfGravity;

    /***
     * Average histogram mean value from red elements.
     */
    private double averageHistoRedMean;

    /***
     * Average histogram mean value from green elements.
     */
    private double averageHistoGreenMean;

    /***
     * Average histogram mean value from blue elements.
     */
    private double averageHistoBlueMean;

    /***
     * Value for dividing and create the average value.
     */
    private int itemCounter;

    private ArrayList<FeaturePoint> boundingBox;
}
