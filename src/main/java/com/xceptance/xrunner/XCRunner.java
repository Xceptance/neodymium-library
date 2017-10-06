package com.xceptance.xrunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.TestClass;

import com.xceptance.multibrowser.Browser;
import com.xceptance.multibrowser.BrowserRunner;
import com.xceptance.xrunner.groups.DefaultGroup;

public class XCRunner extends Runner implements Filterable
{
    List<List<Runner>> testRunner = new LinkedList<>();

    private TestClass testClass;

    private Description testDescription;

    private MethodExecutionContext methodExecutionContext;

    public XCRunner(Class<?> testKlass, RunnerBuilder rb) throws Throwable
    {
        List<List<Runner>> vectors = new LinkedList<>();
        testClass = new TestClass(testKlass);
        List<Runner> runners = new LinkedList<>();
        methodExecutionContext = new MethodExecutionContext();

        // find test vectors
        // scan for Browser and Parameters annotation
        // later on we could add handler for any annotation that should influence test run

        // lookup Browser annotation
        Browser browser = testClass.getAnnotation(Browser.class);
        if (browser != null)
        {
            runners.add(new BrowserRunner(testKlass));
        }

        // scan for JUnit Parameters
        List<FrameworkMethod> parameterMethods = testClass.getAnnotatedMethods(Parameters.class);
        if (parameterMethods.size() > 0)
        {
            setFinalStatic(Parameterized.class.getDeclaredField("DEFAULT_FACTORY"), new XCParameterRunnerFactory(methodExecutionContext));
            runners.add(new Parameterized(testKlass));
        }

        // collect children of ParentRunner sub classes
        doMagic(runners, vectors);

        // create method runners that actually execute the methods annotated with @Test
        List<Runner> methodVector = new LinkedList<>();
        for (FrameworkMethod method : testClass.getAnnotatedMethods(Test.class))
        {
            methodVector.add(new XCMethodRunner(testKlass, method, methodExecutionContext));
        }
        vectors.add(methodVector);

        testRunner = buildTestRunnerLists(vectors);

        // group tests
        List<Class<?>> groupsToExecute = new LinkedList<>();
        groupsToExecute.add(DefaultGroup.class); // TODO:
        testRunner = regroupTests(testRunner, groupsToExecute, true);

        testDescription = createTestDescription();
    }

    private List<List<Runner>> regroupTests(List<List<Runner>> testRunner, List<Class<?>> groupsToExecute, boolean matchAny)
    {
        Map<FrameworkMethod, Set<Class<?>>> testMethodsWithTestGroups = getTestMethodsWithCategories();

        List<List<Runner>> groupedRunner = new LinkedList<>();

        for (List<Runner> runners : testRunner)
        {
            FrameworkMethod method = null;
            // the last runner in the list should always be an XCMethodRunner
            // get this method
            Runner runner = runners.get(runners.size() - 1);
            if (runner instanceof XCMethodRunner)
            {
                method = ((XCMethodRunner) runner).getMethod();
            }
            else
            {
                throw new RuntimeException("This shouldn't happen");
            }

            if (testCategoryMatch(testMethodsWithTestGroups.get(method), groupsToExecute, matchAny))
            {
                groupedRunner.add(runners);
            }
        }

        return groupedRunner;
    }

    private boolean testCategoryMatch(Set<Class<?>> annotatedGroups, List<Class<?>> groupsToExecute, boolean matchAny)
    {
        // if not matchAny then it's matchAll
        boolean match;
        if (matchAny)
        {
            match = false;
        }
        else
        {
            match = true;
        }
        for (Class<?> annotatedGroup : annotatedGroups)
        {
            boolean executionGroupsContainsAnnotatedGroup = groupsToExecute.contains(annotatedGroup);
            if (matchAny)
            {
                match |= executionGroupsContainsAnnotatedGroup;
            }
            else
            {
                match &= executionGroupsContainsAnnotatedGroup;
            }
        }

        return match;
    }

    private Map<FrameworkMethod, Set<Class<?>>> getTestMethodsWithCategories()
    {
        Map<FrameworkMethod, Set<Class<?>>> testMethods = new HashMap<>();

        Category classCategory = testClass.getAnnotation(Category.class);
        List<Class<?>> classCategories = new ArrayList<>();
        if (classCategory != null)
        {
            classCategories = Arrays.asList(classCategory.value());
        }

        // method grouping belongs only to test methods so check that first
        for (FrameworkMethod annotatedMethod : testClass.getAnnotatedMethods(Test.class))
        {
            Category categoryAnnotation = annotatedMethod.getAnnotation(Category.class);

            Set<Class<?>> categories = new HashSet<>();
            if (categoryAnnotation != null)
            {
                categories.addAll(Arrays.asList(categoryAnnotation.value()));
            }

            // add categories from class to every method
            categories.addAll(classCategories);
            // ensure that DefaultGroup is set for all methods that makes it easier afterwards
            categories.add(DefaultGroup.class);

            testMethods.put(annotatedMethod, categories);
        }

        return testMethods;
    }

    private void setFinalStatic(Field field, Object newValue) throws Exception
    {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    private Description createTestDescription()
    {
        Description description = Description.createSuiteDescription(testClass.getJavaClass());

        for (List<Runner> runners : testRunner)
        {
            List<String> displayNames = new LinkedList<>();
            for (Runner runner : runners)
            {
                Description runnerDescription = runner.getDescription();
                String displayName = "";
                if (runner instanceof XCParameterRunner)
                {
                    displayName = ((XCParameterRunner) runner).getName();
                }
                else if (runner instanceof BlockJUnit4ClassRunner)
                {
                    displayName = runner.getDescription().getDisplayName();
                }
                else
                {
                    displayName = runnerDescription.getDisplayName();
                }

                displayNames.add(displayName);
            }

            // necessary to preserver JUnit view feature which lead you to the test method on double click the entry
            // https://github.com/eclipse/eclipse.jdt.ui/blob/0e4ddb8f4fd1d3c22748423acba36397e5f020e7/org.eclipse.jdt.junit/src/org/eclipse/jdt/internal/junit/ui/OpenTestAction.java#L108-L122
            Collections.reverse(displayNames);

            Description childDescription = Description.createTestDescription(testClass.getJavaClass(), String.join(" :: ", displayNames));
            description.addChild(childDescription);
        }

        return description;
    }

    private List<List<Runner>> buildTestRunnerLists(List<List<Runner>> vectors)
    {

        List<List<Runner>> runner = new LinkedList<>();
        runner.add(new LinkedList<>());

        // iterate over all vectors to build the cross product . Last vector should only consist of
        // method runners
        for (int i = vectors.size() - 1; i >= 0; i--)
        {
            List<List<Runner>> newTestRunners = new LinkedList<>();
            for (Runner r : vectors.get(i))
            {
                List<List<Runner>> testRunnerCopy = deepCopy(runner);
                for (List<Runner> list : testRunnerCopy)
                {
                    list.add(0, r);
                }
                newTestRunners.addAll(testRunnerCopy);
            }
            // overwrite previous list of runners
            runner = newTestRunners;
        }

        return runner;
    }

    private List<List<Runner>> deepCopy(List<List<Runner>> original)
    {
        List<List<Runner>> copy = new LinkedList<>();
        for (List<Runner> entry : original)
        {
            copy.add(new LinkedList<>(entry));
        }

        return copy;
    }

    @SuppressWarnings("unchecked")
    private void doMagic(List<Runner> runners, List<List<Runner>> vectors)
        throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        // due to the mostly used protected modifier of getChildren method we have to do some magic here

        for (Runner runner : runners)
        {
            if (runner instanceof ParentRunner<?>)
            {
                Method m = runner.getClass().getDeclaredMethod("getChildren");
                if (m.getName().equals("getChildren"))
                {
                    if (!m.isAccessible())
                    {
                        m.setAccessible(true);
                    }
                    vectors.add((List<Runner>) m.invoke(runner));
                }
            }
            else if (runner instanceof XCRunner)
            {
                XCRunner xcr = (XCRunner) runner;
            }
        }
    }

    @Override
    public void run(RunNotifier notifier)
    {
        for (int i = 0; i < testRunner.size(); i++)
        {
            boolean firstIteration = (i == 0) ? true : false;
            boolean lastIteration = (i == testRunner.size() - 1) ? true : false;

            List<Runner> runners = testRunner.get(i);
            Description description = testDescription.getChildren().get(Math.min(i, Math.max(0, testDescription.getChildren().size() - 1)));

            if (checkIgnored(runners))
            {
                notifier.fireTestIgnored(description);
            }
            else
            {
                Object classInstance;
                try
                {
                    classInstance = testClass.getOnlyConstructor().newInstance();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                BrowserRunner browserRunner = null;
                notifier.fireTestStarted(description);
                for (int r = 0; r < runners.size(); r++)
                {
                    Runner runner = runners.get(r);

                    if (runner instanceof BrowserRunner)
                    {
                        // remember browser runner to close the web driver after test
                        browserRunner = (BrowserRunner) runner;
                    }

                    methodExecutionContext.setRunBeforeClass(firstIteration);
                    methodExecutionContext.setRunAfterClass(lastIteration);
                    methodExecutionContext.setRunnerDescription(description);
                    methodExecutionContext.setTestClassInstance(classInstance);

                    try
                    {
                        runner.run(notifier);
                    }
                    catch (Throwable e)
                    {
                        // mark test as failed and try the next one
                        notifier.fireTestFailure(new Failure(description, e));
                        break;
                    }
                }
                if (browserRunner != null)
                {
                    browserRunner.teardown();
                }
                notifier.fireTestFinished(description);
            }
        }
    }

    private boolean checkIgnored(List<Runner> runners)
    {
        for (Runner runner : runners)
        {
            if (runner instanceof XCMethodRunner)
            {
                XCMethodRunner methodRunner = (XCMethodRunner) runner;
                return (methodRunner.getChildren().get(0).getAnnotation(Ignore.class) != null);
            }
        }

        return false;
    }

    @Override
    public Description getDescription()
    {
        return testDescription;
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException
    {
        // TODO Auto-generated method stub
        System.out.println(filter);
    }
}
