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
package com.xceptance.neodymium.visual.ai.core;

import java.io.Serializable;

/**
 * Represents a float range with minimum and maximum values.
 * 
 * @author Diego Catalano edited by Thomas Volkmann
 */
public class FloatRange implements Serializable
{
    /**
     * Auto genreated serial number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initializes a new instance of the FloatRange class.
     */
    public FloatRange()
    {
    }

    /**
     * Initializes a new instance of the FloatRange class.
     * 
     * @param min
     *            Minimum value of the range.
     * @param max
     *            Maximum value of the range.
     */
    public FloatRange(float min, float max)
    {
        this.min = min;
        this.max = max;
    }

    /**
     * Get minimum value of the range.
     * 
     * @return Minimum value.
     */
    public float getMin()
    {
        return min;
    }

    /**
     * Set minimum value of the range.
     * 
     * @param min
     *            Minimum value.
     */
    public void setMin(float min)
    {
        this.min = min;
    }

    /**
     * Get maximum value of the range.
     * 
     * @return Maximum value.
     */
    public float getMax()
    {
        return max;
    }

    /**
     * Set maximum value of the range.
     * 
     * @param max
     *            Maximum value.
     */
    public void setMax(float max)
    {
        this.max = max;
    }

    /**
     * Length of the range (difference between maximum and minimum values).
     * 
     * @return Length.
     */
    public float length()
    {
        return max - min;
    }

    /**
     * Check if the specified range is inside of the range.
     * 
     * @param x
     *            Value.
     * @return True if the value is inside of the range, otherwise returns false.
     */
    public boolean isInside(float x)
    {
        return (x >= min) && (x <= max);
    }

    /**
     * Check if the specified range overlaps with the range.
     * 
     * @param range
     *            FloatRange.
     * @return True if the range overlaps with the range, otherwise returns false.
     */
    public boolean IsOverlapping(FloatRange range)
    {
        return ((isInside(range.min)) || (isInside(range.max)) ||
                (range.isInside(min)) || (range.isInside(max)));
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj.getClass().isAssignableFrom(FloatRange.class))
        {
            FloatRange range = (FloatRange) obj;
            return this.min == range.getMin() && this.max == range.getMax();
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 19 * hash + Float.floatToIntBits(this.min);
        hash += 19 * hash + Float.floatToIntBits(this.max);
        return hash;
    }

    @Override
    public String toString()
    {
        return "Minimum: " + this.min + " Maximum: " + this.max;
    }

    /***
     * Minimum Value.
     */
    private float min;

    /***
     * Maximum Value.
     */
    private float max;
}