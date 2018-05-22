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

package com.xceptance.neodymium.tests.visual.ai;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.neodymium.visual.ai.NetworkTester;
import com.xceptance.neodymium.visual.ai.NetworkTrainer;
import com.xceptance.neodymium.visual.ai.image.PatternHelper;

public class ValidatorOneImageContained
{
    @BeforeClass
    public static void setup()
    {
        URL location = NetworkTrainer.class.getProtectionDomain().getCodeSource().getLocation();
        File file = new File(location.getPath()).getParentFile().getParentFile();

        // /neodymium-library/config
        String propertieFile = file.toString() + File.separator +
                               "config" + File.separator +
                               "ai.properties";

        // /neodymium-library/src/test/java/com/xceptance/neodymium/tests/visual/ai
        String testFolderPath = file.toString() + File.separator +
                                "src" + File.separator +
                                "test" + File.separator +
                                "java" + File.separator +
                                "com" + File.separator +
                                "xceptance" + File.separator +
                                "neodymium" + File.separator +
                                "tests" + File.separator +
                                "visual" + File.separator +
                                "ai" + File.separator;

        // images for the Exact-Same-Folder (ESF) test
        String testFolderName = "Test_Images_OIC";
        String testFolderNameValidation = "Test_Images_OICV";
        String completteFolderName = testFolderPath + testFolderName;
        String completeFolderNameValidation = testFolderPath + testFolderNameValidation;

        String[] argTR =
        {
          completteFolderName,
          testFolderPath,
          propertieFile,
          testFolderName
        };

        String[] argTE =
        {
          completteFolderName + ".network",
          completeFolderNameValidation
        };

        NetworkTrainer.main(argTR);
        NetworkTester.main(argTE);
    }

    @Test
    public void CreateTrainerAndTester()
    {
        assertTrue(NetworkTrainer.an != null);
        assertTrue(NetworkTrainer.im != null);

        assertTrue(NetworkTester.an != null);
        assertTrue(NetworkTester.im != null);
    }

    @Test
    public void ImageGroupsComparator()
    {
        for (int index = 0; index < NetworkTrainer.im.getCurator().size(); ++index)
        {
            if (NetworkTrainer.im.getCurator().get(index).getMetricCurator().getTagName()
                                 .equals(NetworkTester.im.getCurator().get(0).getMetricCurator().getTagName()))
            {
                assertTrue(NetworkTrainer.im.getCurator().get(index)
                                            .getMetricCurator().metricList.size() == NetworkTester.im.getCurator().get(0).getMetricCurator().metricList.size());
                for (int ind = 0; ind < NetworkTrainer.im.getCurator().get(index).getMetricCurator().metricList.size(); ++ind)
                {
                    assertTrue(NetworkTrainer.im.getCurator().get(index)
                                                .getMetricCurator().metricList.get(ind)
                                                                              .getGroupSize() == NetworkTester.im.getCurator().get(0)
                                                                                                                 .getMetricCurator().metricList.get(ind)
                                                                                                                                               .getGroupSize());
                    assertTrue(NetworkTrainer.im.getCurator().get(index)
                                                .getMetricCurator().metricList.get(ind)
                                                                              .getBoundingBoxDistance() == NetworkTester.im.getCurator().get(0)
                                                                                                                           .getMetricCurator().metricList.get(ind)
                                                                                                                                                         .getBoundingBoxDistance());
                    if (NetworkTrainer.im.getCurator().get(index).getMetricCurator().metricList.get(ind).getImageStatistic() != null)
                    {
                        assertTrue(NetworkTrainer.im.getCurator().get(index)
                                                    .getMetricCurator().metricList.get(ind).getImageStatistic()
                                                                                  .getHistogramBlue()
                                                                                  .getMean() == NetworkTester.im.getCurator().get(0)
                                                                                                                .getMetricCurator().metricList.get(ind)
                                                                                                                                              .getImageStatistic()
                                                                                                                                              .getHistogramBlue()
                                                                                                                                              .getMean());
                        assertTrue(NetworkTrainer.im.getCurator().get(index)
                                                    .getMetricCurator().metricList.get(ind).getImageStatistic()
                                                                                  .getHistogramRed()
                                                                                  .getMean() == NetworkTester.im.getCurator().get(0)
                                                                                                                .getMetricCurator().metricList.get(ind)
                                                                                                                                              .getImageStatistic()
                                                                                                                                              .getHistogramRed()
                                                                                                                                              .getMean());
                        assertTrue(NetworkTrainer.im.getCurator().get(index)
                                                    .getMetricCurator().metricList.get(ind).getImageStatistic()
                                                                                  .getHistogramGreen()
                                                                                  .getMean() == NetworkTester.im.getCurator().get(0)
                                                                                                                .getMetricCurator().metricList.get(ind)
                                                                                                                                              .getImageStatistic()
                                                                                                                                              .getHistogramGreen()
                                                                                                                                              .getMean());
                    }
                }
            }
        }
    }

    @Test
    public void NetworkAverageMetric()
    {
        assertTrue(NetworkTrainer.an.getInputsCount() == NetworkTester.an.getInputsCount());
        assertTrue(NetworkTrainer.an.getAverageMetric().keySet().size() == NetworkTester.an.getAverageMetric().keySet().size());

        int size = NetworkTrainer.an.getAverageMetric().keySet().size();

        for (int index = 0; index < size; ++index)
        {
            assertTrue(NetworkTrainer.an.getAverageMetric().get(index).getAverageGroupSize() == NetworkTester.an.getAverageMetric().get(index)
                                                                                                                .getAverageGroupSize());
            assertTrue(NetworkTrainer.an.getAverageMetric().get(index).getAverageBoundingBoxSize() == NetworkTester.an.getAverageMetric().get(index)
                                                                                                                      .getAverageBoundingBoxSize());
            assertTrue(NetworkTrainer.an.getAverageMetric().get(index).getAverageHistogramRedMean() == NetworkTester.an.getAverageMetric().get(index)
                                                                                                                       .getAverageHistogramRedMean());
            assertTrue(NetworkTrainer.an.getAverageMetric().get(index).getAverageHistogramGreenMean() == NetworkTester.an.getAverageMetric().get(index)
                                                                                                                         .getAverageHistogramGreenMean());
            assertTrue(NetworkTrainer.an.getAverageMetric().get(index).getAverageHistogramBlueMean() == NetworkTester.an.getAverageMetric().get(index)
                                                                                                                        .getAverageHistogramBlueMean());
        }
    }

    @Test
    public void ActivationNetworkParameter()
    {
        assertTrue(NetworkTrainer.an.getLayer().inputsCount == NetworkTester.an.getLayer().inputsCount);
        assertTrue(NetworkTrainer.an.getLayer().getActivationNeuron().getInputCount() == NetworkTester.an.getLayer().getActivationNeuron().getInputCount());
        assertTrue(NetworkTrainer.an.threshold == NetworkTester.an.getLayer().getActivationNeuron().getThreshold());
        assertTrue(NetworkTrainer.an.getLayer().getActivationNeuron().getWeight() == NetworkTester.an.getLayer().getActivationNeuron().getWeight());
    }

    @Test
    public void PatternCalculatedResult()
    {
        ArrayList<PatternHelper> patternListTrainer = NetworkTrainer.im.updateInternalPattern(NetworkTrainer.im.getAverageMetric(),
                                                                                              NetworkTrainer.im.getCurator());
        ArrayList<PatternHelper> patternListTester = NetworkTester.im.updateInternalPattern(NetworkTester.im.getAverageMetric(), NetworkTester.im.getCurator());
        int recognized = 0;

        for (int index = 0; index < patternListTrainer.size(); ++index)
        {
            System.out.println(NetworkTester.an.checkForRecognitionAsString(patternListTrainer.get(index).getPatternList()) + " "
                               + NetworkTester.an.checkForRecognitionAsString(patternListTester.get(0).getPatternList()));
            if (NetworkTester.an.checkForRecognitionAsString(patternListTrainer.get(index).getPatternList()).equals(
                                                                                                                    NetworkTester.an.checkForRecognitionAsString(patternListTester.get(0)
                                                                                                                                                                                  .getPatternList())))
            {
                ++recognized;
            }
        }
        assertTrue(recognized == 1);
    }

    @Test
    public void CompareNeuronWeights()
    {
        assertTrue(NetworkTrainer.an.neurons.size() == NetworkTester.an.getLayer().getActivationNeuron().getNeurons().size());

        int size = NetworkTester.an.getLayer().getActivationNeuron().getNeurons().size();

        for (int index = 0; index < size; ++index)
        {
            assertTrue(NetworkTester.an.getLayer().getActivationNeuron().getNeurons().get(index).getWeight() == NetworkTrainer.an.neurons.get(index)
                                                                                                                                         .getWeight());
        }
    }
}
