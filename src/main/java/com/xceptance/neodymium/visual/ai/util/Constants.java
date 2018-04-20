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

package com.xceptance.neodymium.visual.ai.util;

/***
 * Used constants for the program.
 * 
 * @author Thomas Volkmann
 */
public class Constants
{
    // State of the network to work in training or in classification mode.
    public static boolean NETWORK_MODE = true;

    // Determine if the name of the current test case is used for folder creation or not.
    public static boolean TESTCASE_BOUND = true;

    // Name of the not attached network folder.
    public static String TESTCASE_BOUND_NAME = "unbound";

    // Desired percentage value for the self test, until this barrier is confirmed the network will
    // still learn (use several images which where already seen)
    public static Double INTENDED_PERCENTAGE_MATCH = 0.80;

    // Color will also be used for image comparison, not recommended for websites with many images
    // or consequently changing content
    public static boolean USE_COLOR_FOR_COMPARISON = false;

    // Parameter for enabling down scaling from screenshots or not, this can drastically change
    // the performance of the algorithm
    public static boolean USE_ORIGINAL_SIZE = true;

    // Fixed Size for the histogram creation in Metric
    public static final int BINSIZE = 10;

    // Distance from current point for grouping
    public static int THRESHOLD = 20;

    // Points cloud minimum value
    public static int MINGROUPSIZE = 300;

    // procedural value for the difference level, to compare images
    public static double PERCENTAGE_DIFFERENCE = 0.1;

    // value for the learning algorithm allowed values are between 0.0 - 1.0
    // default is 0.2
    public static Double LEARNING_RATE = 0.2;

    // image format for saving
    public static String FORMAT = "png";

    // value for the height of the image
    public static int IMAGE_HEIGHT = 800;

    // value for the width of the image
    public static int IMAGE_WIDTH = 600;

    // allowed file extensions for loading from folder
    static final String[] EXTENSIONS = new String[]
    {
      "jpg", "png", "bmp", "jpeg"
    };

    // time to wait until the website is loaded and the screenshot is taken
    public static final int WAITINGTIME = 1000;

}
