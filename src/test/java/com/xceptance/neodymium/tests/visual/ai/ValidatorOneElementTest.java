package com.xceptance.neodymium.tests.visual.ai;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.neodymium.visual.ai.NetworkTester;
import com.xceptance.neodymium.visual.ai.NetworkTrainer;

public class ValidatorOneElementTest
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

        // image with one group
        String testFolderName = "Test_Image_OneElement";
        String completeFolderName = testFolderPath + testFolderName;

        String[] argTR =
        {
          completeFolderName,
          testFolderPath,
          propertieFile,
          testFolderName
        };

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
