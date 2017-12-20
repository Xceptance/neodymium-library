// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
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

/**
 * Convolution is applied to images for filtering. In dependence of the used kernel the effects can vary.
 * <p>
 * Supported types: Grayscale, RGB. <br>
 * Coordinate System: Matrix.
 * 
 * @author Diego Catalano edited by Thomas Volkmann
 */
public class Convolution implements IApplyInPlace
{
    /**
     * Structuring element.
     * 
     * @return Structuring element.
     */
    public int[][] getKernel()
    {
        return kernel;
    }

    /**
     * Structuring element.
     * 
     * @param kernel
     *            Structuring element.
     */
    public void setKernel(int[][] kernel)
    {
        this.kernel = kernel;
    }

    /**
     * Sets division.
     * 
     * @param division
     *            Division.
     */
    public void setDivision(int division)
    {
        this.division = division;
        useDiv = true;
    }

    /**
     * Verify if needs replicate pixels when is out of border.
     * 
     * @return Replicate.
     */
    public boolean isReplicate()
    {
        return replicate;
    }

    /**
     * Replicate pixels out of border.
     * 
     * @param replicate
     *            Replicate.
     */
    public void setReplicate(boolean replicate)
    {
        this.replicate = replicate;
    }

    /**
     * Initialize a new instance of the Convolution class.
     */
    public Convolution()
    {
    }

    /**
     * Initialize a new instance of the Convolution class.
     * 
     * @param kernel
     *            Structuring element.
     */
    public Convolution(int[][] kernel)
    {
        this.kernel = kernel;
    }

    /**
     * Initialize a new instance of the Convolution class.
     * 
     * @param kernel
     *            Structuring element.
     * @param replicate
     *            Replicate pixels out of border.
     */
    public Convolution(int[][] kernel, boolean replicate)
    {
        this.kernel = kernel;
        this.replicate = replicate;
    }

    /**
     * Initialize a new instance of the Convolution class.
     * 
     * @param kernel
     *            Structuring element.
     * @param division
     *            Divides the result of convolution.
     */
    public Convolution(int[][] kernel, int division)
    {
        this.kernel = kernel;
        this.division = division;
        useDiv = true;
    }

    /**
     * Initialize a new instance of the Convolution class.
     * 
     * @param kernel
     *            Structuring element.
     * @param division
     *            Divides the result of convolution.
     * @param replicate
     *            Replicate pixels out of border.
     */
    public Convolution(int[][] kernel, int division, boolean replicate)
    {
        this.kernel = kernel;
        this.division = division;
        this.replicate = replicate;
        useDiv = true;
    }

    /***
     * Use the convolution on the Image, in respective to the chosen Kernel. A kernel is always used on the current
     * pixel and the surrounding pixels. Which effect this will have depends on the Image and the kernel.
     * 
     * @param fastBitmap
     *            instead of BufferedImage this use an {@link FastBitmap}
     */
    @Override
    public void applyInPlace(FastBitmap fastBitmap)
    {
        this.width = fastBitmap.getWidth();
        this.height = fastBitmap.getHeight();
        int div;

        int Xline, Yline;
        int lines = CalcLines(kernel);

        FastBitmap copy = new FastBitmap(fastBitmap);

        if (fastBitmap.isGrayscale())
        {
            int gray;
            for (int x = 0; x < height; x++)
            {
                for (int y = 0; y < width; y++)
                {
                    gray = div = 0;
                    for (int i = 0; i < kernel.length; i++)
                    {
                        Xline = x + (i - lines);
                        for (int j = 0; j < kernel[0].length; j++)
                        {
                            Yline = y + (j - lines);
                            if ((Xline >= 0) && (Xline < height) && (Yline >= 0) && (Yline < width))
                            {
                                gray += kernel[i][j] * copy.getGray(Xline, Yline);
                                div += kernel[i][j];
                            }
                            else if (replicate)
                            {
                                int r = x + i - lines;
                                int c = y + j - lines;

                                if (r < 0)
                                    r = 0;
                                if (r >= height)
                                    r = height - 1;

                                if (c < 0)
                                    c = 0;
                                if (c >= width)
                                    c = width - 1;

                                gray += kernel[i][j] * copy.getGray(r, c);
                                div += kernel[i][j];
                            }
                        }
                    }
                    if (div != 0)
                    {
                        if (useDiv)
                        {
                            gray /= division;
                        }
                        else
                        {
                            gray /= div;
                        }
                    }
                    gray = gray > 255 ? 255 : gray;
                    gray = gray < 0 ? 0 : gray;

                    fastBitmap.setGray(x, y, gray);
                }
            }
        }
        else
        {
            int r, g, b;
            for (int x = 0; x < height; x++)
            {
                for (int y = 0; y < width; y++)
                {
                    r = g = b = div = 0;
                    for (int i = 0; i < kernel.length; i++)
                    {
                        Xline = x + (i - lines);
                        for (int j = 0; j < kernel[0].length; j++)
                        {
                            Yline = y + (j - lines);
                            if ((Xline >= 0) && (Xline < height) && (Yline >= 0) && (Yline < width))
                            {
                                r += kernel[i][j] * copy.getRed(Xline, Yline);
                                g += kernel[i][j] * copy.getGreen(Xline, Yline);
                                b += kernel[i][j] * copy.getBlue(Xline, Yline);
                                div += kernel[i][j];
                            }
                            else if (replicate)
                            {
                                int rr = x + i - lines;
                                int cc = y + j - lines;

                                if (rr < 0)
                                    rr = 0;
                                if (rr >= height)
                                    rr = height - 1;

                                if (cc < 0)
                                    cc = 0;
                                if (cc >= width)
                                    cc = width - 1;

                                r += kernel[i][j] * copy.getRed(rr, cc);
                                g += kernel[i][j] * copy.getGreen(rr, cc);
                                b += kernel[i][j] * copy.getBlue(rr, cc);
                                div += kernel[i][j];
                            }
                        }
                    }

                    if (div != 0)
                    {
                        if (useDiv)
                        {
                            r /= division;
                            g /= division;
                            b /= division;
                        }
                        else
                        {
                            r /= div;
                            g /= div;
                            b /= div;
                        }
                    }

                    r = r > 255 ? 255 : r;
                    g = g > 255 ? 255 : g;
                    b = b > 255 ? 255 : b;

                    r = r < 0 ? 0 : r;
                    g = g < 0 ? 0 : g;
                    b = b < 0 ? 0 : b;

                    fastBitmap.setRGB(x, y, r, g, b);
                }
            }
        }
    }

    /***
     * Analyze the kernel for its seize and generate the needed lines for the applying.
     * 
     * @param kernel
     *            int 2D array with the kernel data
     * @return lines number of needed lines
     */
    private int CalcLines(int[][] kernel)
    {
        int lines = (kernel[0].length - 1) / 2;
        return lines;
    }

    /***
     * Image width.
     */
    private int width;

    /***
     * Image height.
     */
    private int height;

    /***
     * Structure element.
     */
    private int[][] kernel;

    /***
     * Divide factor for the convolution.
     */
    private int division;

    /***
     * Flag for division.
     */
    private boolean useDiv = false;

    /***
     * Flag for replication, if the kernel reach over the border of the image.
     */
    private boolean replicate = false;
}