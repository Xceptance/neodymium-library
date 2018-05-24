package com.xceptance.neodymium.tests.visual.ai;

import java.io.File;

import org.junit.AfterClass;

public abstract class AbstractVisualAssertTest
{

    protected static final String propertiesFilePath = "config" + File.separator + "ai.properties";

    protected static final String testFolderPath = "src" + File.separator +
                                                   "test" + File.separator +
                                                   "java" + File.separator +
                                                   "com" + File.separator +
                                                   "xceptance" + File.separator +
                                                   "neodymium" + File.separator +
                                                   "tests" + File.separator +
                                                   "visual" + File.separator +
                                                   "ai" + File.separator;

    protected static String networkPath;

    @AfterClass
    public static void tearDown()
    {
        new File(networkPath).delete();
    }
}
