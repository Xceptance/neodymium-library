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

package com.xceptance.neodymium.junit4.tests.visual.ai;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.neodymium.visual.ai.NetworkTester;
import com.xceptance.neodymium.visual.ai.NetworkTrainer;
import com.xceptance.neodymium.visual.ai.image.AverageMetric;
import com.xceptance.neodymium.visual.ai.image.ImageStatistics;
import com.xceptance.neodymium.visual.ai.image.Metric;
import com.xceptance.neodymium.visual.ai.image.MetricCurator;
import com.xceptance.neodymium.visual.ai.image.PatternHelper;

public class ValidatorOneImageContainedTest extends AbstractVisualAssertTest
{

    private final static String testFolderName = "Test_Images_OIC";

    private final static String testFolderNameValidation = "Test_Images_OICV";

    @BeforeClass
    public static void setup()
    {
        // images for the Exact-Same-Folder (ESF) test
        String completeFolderName = testFolderPath + testFolderName;
        String completeFolderNameValidation = testFolderPath + testFolderNameValidation;

        String[] argTR =
        {
          completeFolderName,
          testFolderPath,
          propertiesFilePath,
          testFolderName
        };

        networkPath = completeFolderName + ".network";
        String[] argTE =
        {
          networkPath,
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
            MetricCurator trainerMetricCurator = NetworkTrainer.im.getCurator().get(index).getMetricCurator();
            MetricCurator testerMetricListCurator = NetworkTester.im.getCurator().get(0).getMetricCurator();

            if (trainerMetricCurator.getTagName().equals(testerMetricListCurator.getTagName()))
            {
                assertTrue(trainerMetricCurator.metricList.size() == testerMetricListCurator.metricList.size());
                for (int ind = 0; ind < trainerMetricCurator.metricList.size(); ++ind)
                {
                    Metric trainerMetric = trainerMetricCurator.metricList.get(ind);
                    Metric testerMetric = testerMetricListCurator.metricList.get(ind);

                    assertTrue(trainerMetric.getGroupSize() == testerMetric.getGroupSize());
                    assertTrue(trainerMetric.getBoundingBoxDistance() == testerMetric.getBoundingBoxDistance());

                    ImageStatistics trainerImageStatistic = trainerMetric.getImageStatistic();
                    if (trainerImageStatistic != null)
                    {
                        ImageStatistics testerImageStatistic = testerMetric.getImageStatistic();
                        assertTrue(trainerImageStatistic.getHistogramBlue().getMean() == testerImageStatistic.getHistogramBlue().getMean());
                        assertTrue(trainerImageStatistic.getHistogramRed().getMean() == testerImageStatistic.getHistogramRed().getMean());
                        assertTrue(trainerImageStatistic.getHistogramGreen().getMean() == testerImageStatistic.getHistogramGreen().getMean());
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
            AverageMetric trainerMetric = NetworkTrainer.an.getAverageMetric().get(index);
            AverageMetric testerMetric = NetworkTester.an.getAverageMetric().get(index);

            assertTrue(trainerMetric.getAverageGroupSize() == testerMetric.getAverageGroupSize());
            assertTrue(trainerMetric.getAverageBoundingBoxSize() == testerMetric.getAverageBoundingBoxSize());
            assertTrue(trainerMetric.getAverageHistogramRedMean() == testerMetric.getAverageHistogramRedMean());
            assertTrue(trainerMetric.getAverageHistogramGreenMean() == testerMetric.getAverageHistogramGreenMean());
            assertTrue(trainerMetric.getAverageHistogramBlueMean() == testerMetric.getAverageHistogramBlueMean());
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
        ArrayList<PatternHelper> pattHelpTrainer = NetworkTrainer.im.updateInternalPattern(NetworkTrainer.im.getAverageMetric(),
                                                                                           NetworkTrainer.im.getCurator());
        ArrayList<PatternHelper> pattHelpTester = NetworkTester.im.updateInternalPattern(NetworkTester.im.getAverageMetric(), NetworkTester.im.getCurator());
        int recognized = 0;

        for (int index = 0; index < pattHelpTrainer.size(); ++index)
        {
            System.out.println(NetworkTrainer.an.checkForRecognitionAsString(pattHelpTrainer.get(index).getPatternList()) + " "
                               + NetworkTester.an.checkForRecognitionAsString(pattHelpTester.get(0).getPatternList()));
            if (NetworkTester.an.checkForRecognitionAsString(pattHelpTrainer.get(index).getPatternList())
                                .equals(NetworkTester.an.checkForRecognitionAsString(pattHelpTester.get(0).getPatternList())))
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
