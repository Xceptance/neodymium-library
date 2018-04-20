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

/***
 * Curator of all metrics generated from the image.
 * 
 * @author Thomas Volkmann
 */
public class MetricCurator implements Serializable
{
    public ArrayList<Metric> metricList;

    /**
     * Auto generated serial number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param tag
     *            String name of the image
     */
    public MetricCurator(String tag)
    {
        tagName = tag;
        metricList = new ArrayList<>();
    }

    /**
     * Get the name of the image represented by the MetricCurator.
     * 
     * @return tagName String name of the image
     */
    public String getTagName()
    {
        return tagName;
    }

    @Override
    public boolean equals(Object v)
    {
        boolean retVal = false;

        if (v instanceof MetricCurator)
        {
            MetricCurator pH = (MetricCurator) v;
            retVal = pH.getTagName().hashCode() == this.getTagName().hashCode();
        }
        return retVal;
    }

    @Override
    public int hashCode()
    {
        return tagName.hashCode();
    }

    /***
     * Name tag of the image.
     */
    private final String tagName;
}
