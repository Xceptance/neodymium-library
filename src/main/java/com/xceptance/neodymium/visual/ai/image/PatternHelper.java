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
 * Helper class for storing the found pattern.
 * 
 * @author Thomas Volkmann
 */
public class PatternHelper implements Serializable
{
    /**
     * Auto generated serial number.
     */
    private static final long serialVersionUID = 1L;

    /***
     * Constructor.
     * 
     * @param tagName
     *            String name of the image.
     */
    public PatternHelper(String tagName)
    {
        this.tagName = tagName;
        patternList = new ArrayList<>();
    }

    /***
     * Add one element to the pattern Array.
     * 
     * @param element
     *            int value of the pattern.
     */
    public void addElementToPattern(int element)
    {
        patternList.add(element);
    }

    /***
     * Secure the size of the pattern array.
     * 
     * @param size
     *            int size to reserve.
     */
    public void ensureCapacitySize(int size)
    {
        patternList.ensureCapacity(size);
    }

    /***
     * Get access to the pattern list.
     * 
     * @return patternList ArrayList pattern of the image.
     */
    public ArrayList<Integer> getPatternList()
    {
        return patternList;
    }

    /***
     * Set the value of one element at a given position,
     * 
     * @param index
     *            int value index key.
     * @param element
     *            int value to set.
     */
    public void setElement(int index, int element)
    {
        patternList.set(index, element);
    }

    /***
     * Get one specific element of the list.
     * 
     * @param index
     *            int value index key.
     * @return int element.
     */
    public int getElement(int index)
    {
        return patternList.get(index);
    }

    /***
     * Size of the pattern list.
     * 
     * @return int Size.
     */
    public int getSize()
    {
        return patternList.size();
    }

    /***
     * Get the name of the image.
     * 
     * @return String image name.
     */
    public String getTagName()
    {
        return tagName;
    }

    @Override
    public boolean equals(Object v)
    {
        boolean retVal = false;

        if (v instanceof PatternHelper)
        {
            PatternHelper pH = (PatternHelper) v;
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
     * Holds the value for the found pattern (0,1).
     */
    private ArrayList<Integer> patternList;

    /***
     * Image name.
     */
    private String tagName;
}
