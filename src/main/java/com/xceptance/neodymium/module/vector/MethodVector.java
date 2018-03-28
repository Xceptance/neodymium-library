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

import com.xceptance.neodymium.MethodExecutionContext;
import com.xceptance.neodymium.NeodymiumMethodRunner;

public class MethodVector extends RunnerVector
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodVector.class);

    private List<FrameworkMethod> methodsToRun = new LinkedList<>();

    @Override
    public boolean init(TestClass testKlass, MethodExecutionContext methodExecutionContext)
    {
        // if the class is annotated to be ignored then all methods should be ignored too
        Ignore ignoreAnnotation = testKlass.getAnnotation(Ignore.class);

        if (ignoreAnnotation != null)
            return false;

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

        if (methodsToRun.isEmpty())
        {
            LOGGER.debug("No test methods found");
            return false;
        }
        else
        {
            // create actual method runner that will later call the methods to test
            LOGGER.debug(MessageFormat.format("Found {0} methods to run", methodsToRun.size()));
            for (FrameworkMethod method : methodsToRun)
            {
                LOGGER.trace(MessageFormat.format("{0}::{1}", testKlass.getJavaClass().getCanonicalName(), method.getName()));
                try
                {
                    runner.add(new NeodymiumMethodRunner(testKlass.getJavaClass(), method, methodExecutionContext));
                }
                catch (Exception e)
                {
                    // TODO: decide to extend the function definition to throw exceptions or just catch and rethrow them as unchekced
                    // exception
                    throw new RuntimeException(e);
                }
            }
            return true;
        }
    }
}
