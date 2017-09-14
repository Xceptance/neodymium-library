package com.xceptance.xrunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
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

public class XCRunner extends Runner
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
            setFinalStatic(Parameterized.class.getDeclaredField("DEFAULT_FACTORY"), new XCParameterRunnerFactory());
            runners.add(new Parameterized(testKlass));
        }

        // collect children of ParentRunner sub classes
        doMagic(runners, vectors);

        // check for existence of method runners
        // Runner lastVectorRunner = (vectors.size() > 0) ? vectors.get(vectors.size() - 1).get(0) : null;
        // if (!(lastVectorRunner instanceof XCMethodRunner) || lastVectorRunner == null)
        // {
        // the last vector does not contain a runner that would run @Test annotated methods
        // we have to build a new vector that contains those runners
        // runners.add(new BlockJUnit4ClassRunner(testKlass));

        methodExecutionContext = new MethodExecutionContext();
        List<Runner> methodVector = new LinkedList<>();
        for (FrameworkMethod method : testClass.getAnnotatedMethods(Test.class))
        {
            methodVector.add(new XCMethodRunner(testKlass, method, methodExecutionContext));
        }
        vectors.add(methodVector);
        // }

        testRunner = buildTestRunnerLists(vectors);
        testDescription = createTestDescription(testRunner, testClass);
    }

    private void setFinalStatic(Field field, Object newValue) throws Exception
    {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    private Description createTestDescription(List<List<Runner>> testRunner, TestClass testClass)
    {
        Description description = Description.createSuiteDescription(testClass.getJavaClass());

        for (List<Runner> runners : testRunner)
        {
            List<String> displayNames = new LinkedList<>();
            for (Runner runner : runners)
            {
                Description runnerDescription = runner.getDescription();

                if (runner instanceof XCParameterRunner)
                {
                    displayNames.add(((XCParameterRunner) runner).getName());
                }
                else if (runner instanceof BlockJUnit4ClassRunner)
                {
                    BlockJUnit4ClassRunner blockRunner = (BlockJUnit4ClassRunner) runner;
                    displayNames.add(blockRunner.getDescription().getDisplayName());
                    // for (Description childDescription : blockRunner.getDescription().getChildren())
                    // {
                    // displayNames.add(childDescription.getDisplayName());
                    // }
                }
                else
                {
                    displayNames.add(runnerDescription.getDisplayName());
                }

            }

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
        }
    }

    @Override
    public void run(RunNotifier notifier)
    {
        for (int i = 0; i < testRunner.size(); i++)
        {
            boolean firstIteration = (i == 0) ? true : false;
            boolean lastIteration = (i == testRunner.size() - 1) ? true : false;

            Object classInstance;
            try
            {
                classInstance = testClass.getOnlyConstructor().newInstance();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

            List<Runner> runners = testRunner.get(i);
            Description description = testDescription.getChildren().get(i);

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

                if (runner instanceof ITestClassInjector)
                {
                    ((ITestClassInjector) runner).setTestClass(classInstance);
                }

                methodExecutionContext.setRunBeforeClass(firstIteration);
                methodExecutionContext.setRunAfterClass(lastIteration);

                runner.run(notifier);
            }
            if (browserRunner != null)
            {
                browserRunner.teardown();
            }
            notifier.fireTestFinished(description);
        }
    }

    @Override
    public Description getDescription()
    {
        return testDescription;
    }

    // private Description recursive_describe_new(Description description, List<List<Runner>> vectors, int index, Runner
    // parent)
    // {
    // // check for recursion end
    // if (index == vectors.size())
    // return description;
    //
    // if (index < vectors.size() - 1)
    // {
    // List<Runner> vector = vectors.get(index);
    //
    // for (Runner v : vector)
    // {
    // recursive_describe_new(description, vectors, index + 1, v);
    // }
    // }
    // else
    // {
    // description.addChild(parent.getDescription());
    // }
    //
    // return description;
    // }

    // private void setDescriptionDisplayName(Description description, String newDisplayName)
    // throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    // {
    // Field field = description.getClass().getDeclaredField("fDisplayName");
    //
    // if (!field.isAccessible())
    // {
    // field.setAccessible(true);
    // }
    //
    // field.set(description, newDisplayName);
    // }

    // private Description recursive_describe(Description description, List<List<Runner>> vectors, int index, String
    // testName)
    // {
    // // check for recursion end
    // if (index == vectors.size())
    // return description;
    //
    // List<Runner> vector = vectors.get(index);
    //
    // for (Runner v : vector)
    // {
    // Description childDescription = v.getDescription();
    //
    // // if (StringUtils.isNotEmpty(testName))
    // // {
    // // try
    // // {
    // // setDescriptionDisplayName(childDescription, testName + " :: " + childDescription.getDisplayName());
    // // }
    // // catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
    // // {
    // // // TODO Auto-generated catch block
    // // e.printStackTrace();
    // // }
    // // }
    //
    // recursive_describe(childDescription, vectors, index + 1, testName + " :: " + childDescription.getDisplayName());
    // if (v instanceof BlockJUnit4ClassRunner)
    // {
    // for (Description child : childDescription.getChildren())
    // {
    // description.addChild(child);
    // }
    // childDescription = description;
    // }
    // else
    // {
    // description.addChild(childDescription);
    // }
    // }
    //
    // return description;
    // }

    // private void recursive_run(RunNotifier notifier, List<List<Runner>> vectors, int index)
    // {
    // if (index == vectors.size())
    // return;
    // List<Runner> vector = vectors.get(index);
    //
    // for (Runner runner : vector)
    // {
    // runner.run(notifier);
    // recursive_run(notifier, vectors, index + 1);
    // }
    // }
}
