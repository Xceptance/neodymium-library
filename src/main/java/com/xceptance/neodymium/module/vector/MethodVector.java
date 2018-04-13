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

    private TestClass testKlass;

    @Override
    public boolean shouldRun(TestClass testKlass)
    {
        this.testKlass = testKlass;
        // if the class is annotated to be ignored then all methods should be ignored too
        Ignore ignoreAnnotation = testKlass.getAnnotation(Ignore.class);

        if (ignoreAnnotation != null)
            return false;

        List<FrameworkMethod> methods = testKlass.getAnnotatedMethods(Test.class);
        for (FrameworkMethod method : methods)
        {
            // check if method is ignored
            Ignore ignore = method.getAnnotation(Ignore.class);
            if (ignore == null)
            {
                // no ignore annoation, add method to the list
                methodsToRun.add(method);
            }
        }

        if (methodsToRun.isEmpty())
        {
            LOGGER.debug("No test methods found");
            return false;
        }
        else
        {
            LOGGER.debug(MessageFormat.format("Found {0} methods to run", methodsToRun.size()));
            return true;
        }
    }

    @Override
    public void createRunners(MethodExecutionContext methodExecutionContext)
    {
        // create actual method runners that will later call the methods to test
        for (FrameworkMethod method : methodsToRun)
        {
            LOGGER.trace(MessageFormat.format("{0}::{1}", testKlass.getJavaClass().getCanonicalName(), method.getName()));
            try
            {
                runner.add(new NeodymiumMethodRunner(testKlass.getJavaClass(), method, methodExecutionContext));
            }
            catch (Exception e)
            {
                // TODO: decide to extend the function definition to throw exceptions or just catch and rethrow them as
                // unchecked exception
                throw new RuntimeException(e);
            }
        }
    }
}
