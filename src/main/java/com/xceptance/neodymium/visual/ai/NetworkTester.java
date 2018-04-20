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
package com.xceptance.neodymium.visual.ai;

import java.util.ArrayList;

import com.xceptance.neodymium.visual.ai.image.FastBitmap;
import com.xceptance.neodymium.visual.ai.image.PatternHelper;
import com.xceptance.neodymium.visual.ai.machine_learning.ActivationNetwork;
import com.xceptance.neodymium.visual.ai.machine_learning.BipolarSigmoidFunction;
import com.xceptance.neodymium.visual.ai.pre_processing.ImageTransformation;
import com.xceptance.neodymium.visual.ai.util.Constants;

/**
 * The NetworkTester tool take two arguments. The first argument is for the network location. This network will be
 * loaded and used for all images. This images need to be under the location of the second argument. Both need to be
 * strings.
 * 
 * @author Thomas Volkmann
 */
public class NetworkTester
{
    public static ActivationNetwork an;

    public static ImageTransformation im;

    /**
     * Entry point for running the network tester.
     * 
     * @param args
     *            String array which contains the arguments, only two arguments will be used but are mandatory.
     */
    public static void main(String[] args)
    {
        Constants.NETWORK_MODE = true;

        an = new ActivationNetwork(new BipolarSigmoidFunction(), 1);
        ArrayList<PatternHelper> patternList = new ArrayList<>();
        ArrayList<FastBitmap> imgList = new ArrayList<>();

        if (args.length < 2)
        {
            System.err.println("NetworkTester <network-file> <images-to-test>");
            return;
        }

        if (args.length > 0)
        {
            an = (ActivationNetwork) an.Load(args[0]);
            an.setConstants();
            if (args.length >= 1)
            {
                imgList = an.scanFolderForChanges(args[1]);
                im = new ImageTransformation(imgList, an.getAverageMetric(), false);
                patternList = im.computeAverageMetric();

                for (PatternHelper pattern : patternList)
                {
                    System.out.println("Recognized value of image " + pattern.getTagName() + " = " + an.checkForRecognitionAsString(pattern.getPatternList())
                                       + " %");
                }
            }
        }
    }
}
