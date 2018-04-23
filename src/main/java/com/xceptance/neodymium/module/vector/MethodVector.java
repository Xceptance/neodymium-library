package com.xceptance.neodymium.module.vector;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;

public class MethodVector implements RunVector
{

    private int vectorHashCode;

    private FrameworkMethod testMethod;

    private List<FrameworkMethod> beforeMethodMethods;

    private List<FrameworkMethod> afterMethodMethods;

    private Object testClassInstance;

    public MethodVector(FrameworkMethod testMethod, List<FrameworkMethod> beforeMethodMethods, List<FrameworkMethod> afterMethodMethods,
                        int hashCode)
    {
        this.testMethod = testMethod;
        this.beforeMethodMethods = beforeMethodMethods;
        this.afterMethodMethods = afterMethodMethods;
        this.vectorHashCode = hashCode;
    }

    @Override
    public void beforeMethod()
    {
        // run befores
        for (FrameworkMethod beforeMethod : beforeMethodMethods)
        {
            try
            {
                beforeMethod.invokeExplosively(testClassInstance);
            }
            catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        }

        // run the actual test methods
        try
        {
            testMethod.invokeExplosively(testClassInstance);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterMethod()
    {
        // run afters
        for (FrameworkMethod afterMethod : afterMethodMethods)
        {
            try
            {
                afterMethod.invokeExplosively(null);
            }
            catch (Throwable e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getTestName()
    {
        return testMethod.getName();
    }

    @Override
    public int vectorHashCode()
    {
        return vectorHashCode;
    }

    @Override
    public void setTestClassInstance(Object testClassInstance)
    {
        this.testClassInstance = testClassInstance;
    }

    // private static final Logger LOGGER = LoggerFactory.getLogger(MethodVector.class);
    //
    // private List<FrameworkMethod> methodsToRun = new LinkedList<>();
    //
    // public MethodVector()
    // {
    // isMultiplying = false;
    // }
    //
    // @Override
    // public boolean shouldRun()
    // {
    // // if the class is annotated to be ignored then all methods should be ignored too
    // Ignore ignoreAnnotation = testClass.getAnnotation(Ignore.class);
    //
    // if (ignoreAnnotation != null)
    // return false;
    //
    // List<FrameworkMethod> methods = testClass.getAnnotatedMethods(Test.class);
    // for (FrameworkMethod method : methods)
    // {
    // // check if method is ignored
    // Ignore ignore = method.getAnnotation(Ignore.class);
    // if (ignore == null)
    // {
    // // no ignore annoation, add method to the list
    // methodsToRun.add(method);
    // }
    // }
    //
    // if (methodsToRun.isEmpty())
    // {
    // LOGGER.debug("No test methods found");
    // return false;
    // }
    // else
    // {
    // LOGGER.debug(MessageFormat.format("Found {0} methods to run", methodsToRun.size()));
    // return true;
    // }
    // }
    //
    // @Override
    // public void createRunners()
    // {
    //
    // // create actual method runners that will later call the methods to test
    // for (FrameworkMethod method : methodsToRun)
    // {
    // LOGGER.trace(MessageFormat.format("{0}::{1}", testClass.getJavaClass().getCanonicalName(), method.getName()));
    // try
    // {
    // runner.add(new NeodymiumMethodRunner(testClass.getJavaClass(), method, null));
    // }
    // catch (Exception e)
    // {
    // // TODO: decide to extend the function definition to throw exceptions or just catch and rethrow them as
    // // unchecked exception
    // throw new RuntimeException(e);
    // }
    // }
    // }
}
