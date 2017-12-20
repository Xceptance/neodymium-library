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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.xceptance.neodymium.visual.ai.image.PatternHelper;
import com.xceptance.neodymium.visual.ai.machine_learning.ActivationNetwork;
import com.xceptance.neodymium.visual.ai.machine_learning.BipolarSigmoidFunction;
import com.xceptance.neodymium.visual.ai.machine_learning.PerceptronLearning;
import com.xceptance.neodymium.visual.ai.pre_processing.ImageTransformation;
import com.xceptance.neodymium.visual.ai.util.Constants;
import com.xceptance.neodymium.visual.ai.util.Helper;

/**
 * The network trainer is a tool for creating networks. A network is the result from loading and analyzing images. The
 * analyzing is the learning of the structure within the images. The network trainer need three parameters to work: 1.
 * path to folder where the images are saved 2. path to properties file (by default under
 * git_folder/xlt-vissual-asser/config/ai.properties) 3. optional network name, could also be changed afterwards The
 * network trainer learn every images under the first argument using the parameters in the properties file. After
 * processing all images the network trainer test himself, with all images and deliver a percentage recognized value for
 * each image. The network is saved under NetworkTrainer.results.
 * 
 * @author Thomas Volkmann
 */
public class NetworkTrainer
{
    public static ActivationNetwork an;

    public static ImageTransformation im;

    // args[0] = what to learn
    // args[1] = where to store the result
    // args[2] = the properties
    // args[3] = network name

    /**
     * Entry point for running the network trainer.
     * 
     * @param args
     *            String array first two arguments are mandatory, third argument is optional.
     */
    public static void main(String[] args)
    {
        // --------------------------------------------------------------------------------
        // Get Properties and convert them from String if necessary
        // --------------------------------------------------------------------------------
        if (args.length < 4)
        {
            System.err.println("Call with NetworkTrainer <data-dir> <result-dir> <property-file> <network-name>");
            System.exit(-1);
        }

        try
        {
            Helper.readProperties(args[2]);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        final File networkDirectoryPath = new File(args[1]);
        networkDirectoryPath.mkdirs();

        // Path to working folder
        if (args.length == 0)
        {
            System.out.println("No directory path was given as Parameter 1");
            return;
        }

        String networkName = args[3] + ".network";

        final File networkFile = new File(networkDirectoryPath, networkName);

        // initialization
        an = new ActivationNetwork(new BipolarSigmoidFunction(), 1);
        ArrayList<PatternHelper> patternList = new ArrayList<>();

        im = new ImageTransformation(args[0]);
        im.computeAverageMetric();

        // updated list for self testing and image confirmation
        patternList = im.updateInternalPattern(im.getAverageMetric(), im.getCurator());

        PerceptronLearning pl = new PerceptronLearning(an);
        pl.setLearningRate(Constants.LEARNING_RATE);

        double resultVerfication = 0.0;
        for (PatternHelper pattern : patternList)
        {
            pl.Run(pattern.getPatternList());
        }

        for (PatternHelper pattern : patternList)
        {
            resultVerfication += an.checkForRecognitionAsDouble(pattern.getPatternList());
            System.out.println("Recognized value of image " + pattern.getTagName() + " = " + an.checkForRecognitionAsString(pattern.getPatternList()) + " %");
        }

        System.out.println("Selftest value summed: " + (resultVerfication / patternList.size()));

        an.setInternalParameter(im.getAverageMetric());
        an.Save(networkFile.toString());
    }
}
