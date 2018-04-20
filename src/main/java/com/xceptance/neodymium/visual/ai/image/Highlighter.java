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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import com.xceptance.neodymium.visual.ai.core.FeaturePoint;

public class Highlighter
{
    // orig image + bounding boxes + pattern
    public Highlighter(FastBitmap origImage, Map<Integer, AverageMetric> averMet, PatternHelper pattern)
    {
        markedImage = origImage.toBufferedImage();
        determineBoundingBoxes(averMet, pattern);
    }

    private void determineBoundingBoxes(Map<Integer, AverageMetric> averMet, PatternHelper pattern)
    {
        final Graphics2D g = markedImage.createGraphics();
        for (int index = 0; index < pattern.getSize(); ++index)
        {
            if (pattern.getElement(index) == 1)
            {
                FeaturePoint min = averMet.get(index).getBoundingBox().get(0);
                FeaturePoint max = averMet.get(index).getBoundingBox().get(1);

                int width = max.x - min.x;
                int height = max.y - min.y;

                g.setColor(new Color(0, 255, 0));
                // top left -> top right
                g.drawLine(min.y, min.x, min.y, min.x + width);
                // top left -> bottom left
                g.drawLine(min.y, min.x, min.y + height, min.x);
                // bottom right -> top right
                g.drawLine(min.y + height, min.x + width, min.y, min.x + width);
                // bottom right -> bottom left
                g.drawLine(min.y + height, min.x + width, min.y + height, min.x);
            }
        }
        g.dispose();

    }

    public BufferedImage getMarkedImage()
    {
        return markedImage;
    }

    private BufferedImage markedImage;
}
