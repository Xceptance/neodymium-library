package com.xceptance.neodymium.junit4.tests.visual.ai;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.neodymium.visual.ai.NetworkTester;
import com.xceptance.neodymium.visual.ai.NetworkTrainer;

public class ValidatorOneElementTest extends AbstractVisualAssertTest
{
    private final static String testFolderName = "Test_Image_OneElement";

    @BeforeClass
    public static void setup()
    {
        // image with one group
        String completeFolderName = testFolderPath + testFolderName;

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
          completeFolderName + ".network",
          completeFolderName
        };

        NetworkTrainer.main(argTR);
        NetworkTester.main(argTE);
    }

    @Test
    public void foundOneElement()
    {
        assertTrue(NetworkTrainer.im.getCurator().get(0).getMetricCurator().metricList.size() == 1);
    }

    @Test
    public void checkAverageMetric()
    {
        assertTrue(NetworkTester.an.getAverageMetric().size() == 1);
    }
}
