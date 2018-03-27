package com.xceptance.neodymium.module.vector;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodVector extends RunnerVector
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodVector.class);

    private List<FrameworkMethod> methodsToRun = new LinkedList<>();

    @Override
    public void init(TestClass testKlass)
    {
        // if the class is annotated to be ignored then all methods should be ignored too
        Ignore ignoreAnnotation = testKlass.getAnnotation(Ignore.class);

        if (ignoreAnnotation != null)
            return;

        List<FrameworkMethod> testAnnotatedMethods = testKlass.getAnnotatedMethods(Test.class);
        for (FrameworkMethod testAnnotatedMethod : testAnnotatedMethods)
        {
            // check if method is ignored
            Ignore methodIgnoreAnnotation = testAnnotatedMethod.getAnnotation(Ignore.class);
            if (methodIgnoreAnnotation == null)
            {
                // no ignore annoation, add them to the list
                methodsToRun.add(testAnnotatedMethod);
            }
        }

        // just logging
        if (methodsToRun.isEmpty())
        {
            LOGGER.debug("No test methods found");
        }
        else
        {
            LOGGER.debug(MessageFormat.format("Found {0} methods to run", methodsToRun.size()));
            for (FrameworkMethod method : methodsToRun)
            {
                LOGGER.trace(MessageFormat.format("{0}::{1}", testKlass.getJavaClass().getCanonicalName(), method.getName()));
            }
        }
    }
}
