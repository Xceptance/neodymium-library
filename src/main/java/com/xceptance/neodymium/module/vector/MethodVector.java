package com.xceptance.neodymium.module.vector;

public class MethodVector
{

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
