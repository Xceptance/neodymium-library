package com.xceptance.neodymium;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xceptance.neodymium.module.order.DefaultVectorRunOrder;
import com.xceptance.neodymium.module.vector.RunVector;
import com.xceptance.neodymium.module.vector.RunVectorBuilder;
import com.xceptance.neodymium.module.vector.RunVectorNode;
import com.xceptance.neodymium.multibrowser.Browser;

/**
 * This class executes {@link JUnit4} test classes (aka JUnit Runner) and adds several features to test execution e.g.
 * multi {@link Browser browser} and
 * <a href="https://github.com/Xceptance/neodymium-library/wiki/Test-data-provider">test data</a>. Vanilla JUnit
 * parameterized tests are supported as well but only with parameter injection (as described here: <a href=
 * "https://github.com/junit-team/junit4/wiki/parameterized-tests#using-parameter-for-field-injection-instead-of-constructor">Using @Parameter
 * for Field injection instead of Constructor</a>). In order to run a {@link JUnit4} test with this runner the class or
 * its super-class has to be annotated with {@link RunWith}
 * <p>
 * <b>Example</b>
 * 
 * <pre>
 * &#64;RunWith(NeodymiumRunner.class)
 * public class MyTests
 * {
 *     &#64;Test
 *     public void testMethod()
 *     {
 *     }
 * }
 * </pre>
 * 
 * <b>Example</b>
 * 
 * <pre>
 * public class MyTests extends BaseTestClass
 * {
 *     &#64;Test
 *     public void testMethod()
 *     {
 *     }
 * }
 * 
 * &#64;RunWith(NeodymiumRunner.class)
 * public class BaseTestClass
 * {
 * }
 * </pre>
 * 
 * @author m.kaufmann
 */
public class NeodymiumRunner extends Runner implements Filterable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NeodymiumRunner.class);

    List<List<Runner>> testRunner = new LinkedList<>();

    private TestClass testClass;

    private Description testDescription;

    private Map<FrameworkMethod, List<List<RunVector>>> orderedTestRunner;

    public NeodymiumRunner(Class<?> testKlass, RunnerBuilder rb) throws Throwable
    {
        LOGGER.debug(testKlass.getCanonicalName());
        // create JUnit class util
        testClass = new TestClass(testKlass);
        List<FrameworkMethod> testMethods = new LinkedList<>();
        buildTestMethodList(testMethods);

        // for now assume always default run order. we can change this by annotating the test later
        List<Class<? extends RunVectorBuilder>> vectorRunOrder = new DefaultVectorRunOrder().getVectorRunOrder();

        orderedTestRunner = new HashMap<>();
        for (Class<? extends RunVectorBuilder> vectorBuildClass : vectorRunOrder)
        {
            for (FrameworkMethod method : testMethods)
            {
                RunVectorBuilder vectorBuilder = createVectorBuilder(vectorBuildClass);
                vectorBuilder.create(testClass, method);
                List<RunVector> buildRunVectors = vectorBuilder.buildRunVectors();
                if (buildRunVectors.size() > 0)
                    orderedTestRunner.computeIfAbsent(method, (key) -> new LinkedList<>()).add(buildRunVectors);
            }
        }

        RunVectorNode runNode = null;
        for (List<List<RunVector>> list : orderedTestRunner.values())
        {
            if (runNode == null)
            {
                runNode = new RunVectorNode(list);
            }
            else
            {
                runNode.add(list);
            }
        }

        testDescription = Description.createSuiteDescription(testClass.getJavaClass());
        for (RunVectorNode childNode : runNode.childNodes)
        {
            createChildTestDescription(childNode, testDescription);
        }

        // VectorRunOrder defaultVectorRunOrder = new MethodOnlyRunOrder(); // new DefaultVectorRunOrder();
        // // VectorRunOrder defaultVectorRunOrder = new DefaultVectorRunOrder();
        // List<RunnerVector> vectorRunOrder = defaultVectorRunOrder.getVectorRunOrder();
        //
        // List<List<Runner>> vectors = new LinkedList<>();
        // methodExecutionContext = new MethodExecutionContext();
        //
        // for (RunnerVector runVector : vectorRunOrder)
        // {
        // if (runVector.shouldRun(testClass))
        // {
        // runVector.createRunners(methodExecutionContext);
        // if (runVector.getRunnerList().size() > 0)
        // vectors.add(runVector.getRunnerList());
        // }
        // }
        //
        // testRunner = buildTestRunnerLists(vectors);
        // testDescription = createTestDescription();

        //
        //
        //

        // List<List<Runner>> vectors = new LinkedList<>();
        // List<Runner> runners = new LinkedList<>();
        //
        //
        // // find test vectors
        // // scan for Browser and Parameters annotation
        // // later on we could add handler for any annotation that should influence test run
        //
        // // lookup Browser annotation
        // Browser browser = testClass.getAnnotation(Browser.class);
        // if (browser != null)
        // {
        // LOGGER.debug("Found browser annotation");
        // runners.add(new BrowserRunner(testKlass));
        // }
        //
        // // scan for JUnit Parameters
        // List<FrameworkMethod> parameterMethods = testClass.getAnnotatedMethods(Parameters.class);
        // if (parameterMethods.size() > 0)
        // {
        // LOGGER.debug("Found parameters annotation");
        // setFinalStatic(Parameterized.class.getDeclaredField("DEFAULT_FACTORY"),
        // new NeodymiumParameterRunnerFactory(methodExecutionContext));
        // runners.add(new Parameterized(testKlass));
        // }
        //
        // try
        // {
        // runners.add(new NeodymiumDataRunner(testClass, methodExecutionContext));
        // }
        // catch (IllegalArgumentException e)
        // {
        // // no test data found, proceed
        // }
        // catch (NoSuchFieldException e)
        // {
        // // test data was found
        // // if the message is empty then no field was defined (annotated) and we drop the exception
        // // if the message is not empty then a correct annotated field was found but
        // // its either not a map or not public
        // if (StringUtils.isNotBlank(e.getMessage()))
        // throw e;
        // }
        //
        // // collect children of ParentRunner sub classes
        // doMagic(runners, vectors);
        //
        // // create method runners that actually execute the methods annotated with @Test
        // List<Runner> methodVector = new LinkedList<>();
        // List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods(Test.class);
        // if (annotatedMethods.size() > 0)
        // {
        // LOGGER.debug("Found methods to run");
        // }
        // else
        // {
        // LOGGER.debug("No test methods found");
        // }
        //
        // for (FrameworkMethod method : annotatedMethods)
        // {
        // NeodymiumMethodRunner methodRunner = new NeodymiumMethodRunner(testKlass, method, methodExecutionContext);
        // LOGGER.debug("\t" + methodRunner.getDescription().getDisplayName());
        // methodVector.add(methodRunner);
        // }
        // vectors.add(methodVector);
        //
        // testRunner = buildTestRunnerLists(vectors);
        //
        // LOGGER.debug("Build " + testRunner.size() + " test runner");
        //
        // testDescription = createTestDescription();
    }

    private void buildTestMethodList(List<FrameworkMethod> testMethods)
    {
        // if the class is annotated to be ignored then all methods should be ignored too
        Ignore ignoreAnnotation = testClass.getAnnotation(Ignore.class);

        if (ignoreAnnotation != null)
            return;

        List<FrameworkMethod> methods = testClass.getAnnotatedMethods(Test.class);
        for (FrameworkMethod method : methods)
        {
            // check if method is ignored
            Ignore ignore = method.getAnnotation(Ignore.class);
            if (ignore == null)
            {
                // no ignore annoation, add method to the list
                testMethods.add(method);
            }
        }

        if (testMethods.isEmpty())
        {
            LOGGER.debug("No test methods found");
        }
        else
        {
            LOGGER.debug(MessageFormat.format("Found {0} methods to run", testMethods.size()));
        }
    }

    private RunVectorBuilder createVectorBuilder(Class<? extends RunVectorBuilder> vectorBuilderClass)
    {
        RunVectorBuilder vector;
        try
        {
            vector = vectorBuilderClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }

        return vector;
    }

    private void createChildTestDescription(RunVectorNode runNode, Description parentDescription)
    {
        boolean nodeHasChilds = (runNode.childNodes.size() == 0) ? false : true;
        for (RunVector runVector : runNode.runVectors)
        {
            if (nodeHasChilds)
            {
                Description suiteDescription = Description.createSuiteDescription(runVector.getTestName());
                parentDescription.addChild(suiteDescription);
                for (RunVectorNode childNode : runNode.childNodes)
                {
                    createChildTestDescription(childNode, suiteDescription);
                }
            }
            else
            {
                Description testDescription = Description.createTestDescription(testClass.getJavaClass().getName(), runVector.getTestName(),
                                                                                runVector.getTestName());
                parentDescription.addChild(testDescription);
            }
        }
    }

    @Override
    public void run(RunNotifier notifier)
    {
        // since before/after class invocation is JUnit default we just hardcode that
        List<FrameworkMethod> beforeClassMethods = testClass.getAnnotatedMethods(BeforeClass.class);
        List<FrameworkMethod> afterClassMethods = testClass.getAnnotatedMethods(AfterClass.class);

        Object testClassInstance = new Object();

        try
        {
            testClassInstance = testClass.getOnlyConstructor().newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // run before class methods
        try
        {
            invokeMethods(testClassInstance, beforeClassMethods);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
        //
        // {
        // // run before method methods
        // invokeMethods(testInstance, beforeMethodMethods);
        //
        // // TODO: run the test methods
        //
        // // run after method methods
        // invokeMethods(testInstance, afterMethodMethods);
        // }
        //
        // run after class methods
        try
        {
            invokeMethods(testClassInstance, afterClassMethods);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    private void invokeMethods(Object testInstance, List<FrameworkMethod> methods) throws Throwable
    {
        for (FrameworkMethod m : methods)
        {
            m.invokeExplosively(testInstance);
        }
    }

    // private List<List<Runner>> regroupTests(List<List<Runner>> testRunner, List<Class<?>> groupsToExecute, boolean
    // matchAny)
    // {
    // Map<FrameworkMethod, Set<Class<?>>> testMethodsWithTestGroups = getTestMethodsWithCategories();
    //
    // List<List<Runner>> groupedRunner = new LinkedList<>();
    //
    // for (List<Runner> runners : testRunner)
    // {
    // FrameworkMethod method = null;
    // // the last runner in the list should always be an NeodymiumMethodRunner
    // // get this method
    // Runner runner = runners.get(runners.size() - 1);
    // if (runner instanceof NeodymiumMethodRunner)
    // {
    // method = ((NeodymiumMethodRunner) runner).getMethod();
    // }
    // else
    // {
    // throw new RuntimeException("This shouldn't happen");
    // }
    //
    // if (testCategoryMatch(testMethodsWithTestGroups.get(method), groupsToExecute, matchAny))
    // {
    // groupedRunner.add(runners);
    // }
    // }
    //
    // return groupedRunner;
    // }
    //
    // private boolean testCategoryMatch(Set<Class<?>> annotatedGroups, List<Class<?>> groupsToExecute, boolean
    // matchAny)
    // {
    // // if not matchAny then it's matchAll
    // boolean match;
    // if (matchAny)
    // {
    // match = false;
    // }
    // else
    // {
    // match = true;
    // }
    // for (Class<?> annotatedGroup : annotatedGroups)
    // {
    // boolean executionGroupsContainsAnnotatedGroup = groupsToExecute.contains(annotatedGroup);
    // if (matchAny)
    // {
    // match |= executionGroupsContainsAnnotatedGroup;
    // }
    // else
    // {
    // match &= executionGroupsContainsAnnotatedGroup;
    // }
    // }
    //
    // return match;
    // }
    //
    // private Map<FrameworkMethod, Set<Class<?>>> getTestMethodsWithCategories()
    // {
    // Map<FrameworkMethod, Set<Class<?>>> testMethods = new HashMap<>();
    //
    // Category classCategory = testClass.getAnnotation(Category.class);
    // List<Class<?>> classCategories = new ArrayList<>();
    // if (classCategory != null)
    // {
    // classCategories = Arrays.asList(classCategory.value());
    // }
    //
    // // method grouping belongs only to test methods so check that first
    // for (FrameworkMethod annotatedMethod : testClass.getAnnotatedMethods(Test.class))
    // {
    // Category categoryAnnotation = annotatedMethod.getAnnotation(Category.class);
    //
    // Set<Class<?>> categories = new HashSet<>();
    // if (categoryAnnotation != null)
    // {
    // categories.addAll(Arrays.asList(categoryAnnotation.value()));
    // }
    //
    // // add categories from class to every method
    // categories.addAll(classCategories);
    // // ensure that DefaultGroup is set for all methods that makes it easier afterwards
    // categories.add(DefaultGroup.class);
    // categories.remove(null);
    //
    // testMethods.put(annotatedMethod, categories);
    // }
    //
    // return testMethods;
    // }

    // private void setFinalStatic(Field field, Object newValue) throws Exception
    // {
    // field.setAccessible(true);
    //
    // Field modifiersField = Field.class.getDeclaredField("modifiers");
    // modifiersField.setAccessible(true);
    // modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    //
    // field.set(null, newValue);
    // }
    // private Description createTestDescription_old()
    // {
    // Description description = Description.createSuiteDescription(testClass.getJavaClass());
    //
    // for (List<Runner> runners : testRunner)
    // {
    // List<String> displayNames = new LinkedList<>();
    // for (Runner runner : runners)
    // {
    // Description runnerDescription = runner.getDescription();
    // String displayName = "";
    // if (runner instanceof NeodymiumParameterRunner)
    // {
    // displayName = ((NeodymiumParameterRunner) runner).getName();
    // }
    // else if (runner instanceof BlockJUnit4ClassRunner)
    // {
    // displayName = runner.getDescription().getDisplayName();
    // }
    // else if (runner instanceof NeodymiumDataRunnerRunner)
    // {
    // NeodymiumDataRunnerRunner dataRunner = (NeodymiumDataRunnerRunner) runner;
    // if (dataRunner.hasDataSets())
    // {
    // displayName = runnerDescription.getDisplayName();
    // }
    // else
    // {
    // displayName = null;
    // }
    // }
    // else
    // {
    // displayName = runnerDescription.getDisplayName();
    // }
    //
    // if (displayName != null)
    // displayNames.add(displayName);
    // }
    //
    // // necessary to preserve JUnit view feature which lead you to the test method on double click the entry
    // //
    // https://github.com/eclipse/eclipse.jdt.ui/blob/0e4ddb8f4fd1d3c22748423acba36397e5f020e7/org.eclipse.jdt.junit/src/org/eclipse/jdt/internal/junit/ui/OpenTestAction.java#L108-L122
    // Collections.reverse(displayNames);
    //
    // Set<Annotation> methodCategoryAnnotations = new HashSet<>();
    // List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods();
    // for (FrameworkMethod fm : annotatedMethods)
    // {
    // methodCategoryAnnotations.add(fm.getAnnotation(Category.class));
    // }
    // methodCategoryAnnotations.remove(null);
    //
    // Description childDescription = Description.createTestDescription(testClass.getJavaClass(), String.join(" :: ",
    // displayNames),
    // methodCategoryAnnotations.toArray(new Annotation[0]));
    // description.addChild(childDescription);
    // }
    //
    // return description;
    // }
    // private List<List<Runner>> buildTestRunnerLists(List<List<Runner>> vectors)
    // {
    //
    // List<List<Runner>> runner = new LinkedList<>();
    // runner.add(new LinkedList<>());
    //
    // // iterate over all vectors to build the cross product . Last vector should only consist of
    // // method runners
    // for (int i = vectors.size() - 1; i >= 0; i--)
    // {
    // List<List<Runner>> newTestRunners = new LinkedList<>();
    // for (Runner r : vectors.get(i))
    // {
    // List<List<Runner>> testRunnerCopy = deepCopy(runner);
    // for (List<Runner> list : testRunnerCopy)
    // {
    // list.add(0, r);
    // }
    // newTestRunners.addAll(testRunnerCopy);
    // }
    // // overwrite previous list of runners
    // runner = newTestRunners;
    // }
    //
    // return runner;
    // }
    //
    // private List<List<Runner>> deepCopy(List<List<Runner>> original)
    // {
    // List<List<Runner>> copy = new LinkedList<>();
    // for (List<Runner> entry : original)
    // {
    // copy.add(new LinkedList<>(entry));
    // }
    //
    // return copy;
    // }
    //
    // @SuppressWarnings("unchecked")
    // private void doMagic(List<Runner> runners, List<List<Runner>> vectors)
    // throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
    // InvocationTargetException
    // {
    // // due to the mostly used protected modifier of getChildren method we have to do some magic here
    //
    // for (Runner runner : runners)
    // {
    // if (runner instanceof ParentRunner<?>)
    // {
    // Method m = runner.getClass().getDeclaredMethod("getChildren");
    // if (m.getName().equals("getChildren"))
    // {
    // if (!m.isAccessible())
    // {
    // m.setAccessible(true);
    // }
    // List<Runner> childs = (List<Runner>) m.invoke(runner);
    // LOGGER.debug(runner.getClass().getSimpleName() + " added " + childs.size() + " childs");
    //
    // for (Runner r : childs)
    // {
    // LOGGER.debug("\t" + r.getDescription().getDisplayName());
    // }
    //
    // vectors.add(childs);
    // }
    // }
    // else
    // {
    // LOGGER.debug(runner.getClass().getCanonicalName());
    // List<Runner> child = new LinkedList<>();
    // child.add(runner);
    // vectors.add(child);
    // }
    // }
    // }

    // LOGGER.debug("Run " + testRunner.size() + " tests");
    // for (int i = 0; i < testRunner.size(); i++)
    // {
    // LOGGER.debug("Run test " + (i + 1) + "/" + testRunner.size());
    // boolean firstIteration = (i == 0) ? true : false;
    // boolean lastIteration = (i == testRunner.size() - 1) ? true : false;
    //
    // List<Runner> runners = testRunner.get(i);
    // Description description = testDescription.getChildren().get(Math.min(i, Math.max(0,
    // testDescription.getChildren().size() - 1)));
    //
    // if (checkIgnored(runners))
    // {
    // LOGGER.debug("Test ignored");
    // notifier.fireTestIgnored(description);
    // }
    // else
    // {
    // Object classInstance;
    // try
    // {
    // classInstance = testClass.getOnlyConstructor().newInstance();
    // }
    // catch (Exception e)
    // {
    // throw new RuntimeException(e);
    // }
    //
    // NeodymiumBrowserRunner browserRunner = null;
    // notifier.fireTestStarted(description);
    // Failure testFailure = null;
    //
    // for (int r = 0; r < runners.size(); r++)
    // {
    // Runner runner = runners.get(r);
    //
    // if (runner instanceof NeodymiumBrowserRunner)
    // {
    // // remember browser runner to close the web driver after test
    // browserRunner = (NeodymiumBrowserRunner) runner;
    // }
    //
    // methodExecutionContext.setRunBeforeClass(firstIteration);
    // methodExecutionContext.setRunAfterClass(lastIteration);
    // methodExecutionContext.setRunnerDescription(description);
    // methodExecutionContext.setTestClassInstance(classInstance);
    //
    // LOGGER.debug("Execute runner " + runner.getClass().getSimpleName());
    // try
    // {
    // runner.run(notifier);
    // }
    // catch (Throwable e)
    // {
    // LOGGER.debug("Test failed", e);
    // // mark test as failed and try the next one
    // testFailure = new Failure(description, e);
    // notifier.fireTestFailure(testFailure);
    // break;
    // }
    // }
    // if (testFailure == null)
    // LOGGER.debug("Test passed");
    //
    // if (browserRunner != null)
    // {
    // browserRunner.teardown();
    // }
    // notifier.fireTestFinished(description);
    // }
    // }
    // }
    //
    // private boolean checkIgnored(List<Runner> runners)
    // {
    // for (Runner runner : runners)
    // {
    // if (runner instanceof NeodymiumMethodRunner)
    // {
    // NeodymiumMethodRunner methodRunner = (NeodymiumMethodRunner) runner;
    // return (methodRunner.getChildren().get(0).getAnnotation(Ignore.class) != null);
    // }
    // }
    //
    // return false;
    // }

    @Override
    public Description getDescription()
    {
        return testDescription;
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException
    {
        // // this method will be called by surefire and gradle among others
        // // any include/exclude groups defined in maven or gradle build process result in an filter object
        // LOGGER.debug("Filter type: " + filter.getClass());
        // LOGGER.debug("Runner size before filter: " + testRunner.size());
        //
        // List<List<Runner>> newTestRunner = new LinkedList<>();
        //
        // for (List<Runner> runners : testRunner)
        // {
        // try
        // {
        // NeodymiumMethodRunner runner = (NeodymiumMethodRunner) runners.get(runners.size() - 1);
        // filter.apply(runner);
        // newTestRunner.add(runners);
        // }
        // catch (NoTestsRemainException e)
        // {
        // // doesn't matter
        // }
        // }
        //
        // testRunner = newTestRunner;
        //
        // // groupsToExecute.add(DefaultGroup.class);
        // // testRunner = regroupTests(testRunner, groupsToExecute, true);
        // testDescription = createTestDescription();
        //
        // LOGGER.debug("Runner size after filter: " + testRunner.size());
    }
}
