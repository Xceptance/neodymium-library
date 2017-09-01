package com.xceptance.xrunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

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
    List<List<Runner>> vectors = new LinkedList<>();

    private TestClass testClass;

    public XCRunner(Class<?> testKlass, RunnerBuilder rb) throws Throwable
    {
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
            runners.add(new Parameterized(testKlass));
        }

        // collect children of ParentRunner sub classes
        doMagic(runners);

        // check for existence of method runners
        Runner lastVectorRunner = (vectors.size() > 0) ? vectors.get(vectors.size() - 1).get(0) : null;
        if (!(lastVectorRunner instanceof BlockJUnit4ClassRunner))
        {
            // the last vector does not contain a runner that would run @Test annotated methods
            // we have to build a new vector that contains those runners
            List<Runner> methodVector = new LinkedList<>();
            methodVector.add(new BlockJUnit4ClassRunner(testKlass));
            vectors.add(methodVector);
        }
    }

    @SuppressWarnings("unchecked")
    private void doMagic(List<Runner> runners)
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
    public Description getDescription()
    {
        return recursive_describe(Description.createSuiteDescription(testClass.getJavaClass()), vectors, 0);
    }

    private Description recursive_describe(Description description, List<List<Runner>> vectors, int index)
    {
        // check for recursion end
        if (index == vectors.size())
            return description;

        List<Runner> vector = vectors.get(index);

        for (Runner v : vector)
        {
            Description childDescription = v.getDescription();

            recursive_describe(childDescription, vectors, index + 1);
            description.addChild(childDescription);
        }

        return description;
    }

    @Override
    public void run(RunNotifier notifier)
    {
        recursive_run(notifier, vectors, 0);
    }

    private void recursive_run(RunNotifier notifier, List<List<Runner>> vectors, int index)
    {
        if (index == vectors.size())
            return;
        List<Runner> vector = vectors.get(index);

        for (Runner runner : vector)
        {
            runner.run(notifier);
            recursive_run(notifier, vectors, index + 1);
        }
    }
}
