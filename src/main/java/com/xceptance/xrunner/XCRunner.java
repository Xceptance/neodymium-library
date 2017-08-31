package com.xceptance.xrunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.TestClass;

import com.xceptance.multibrowser.Browser;
import com.xceptance.multibrowser.BrowserRunner;

public class XCRunner extends Runner
{
    List<List<Runner>> vectors = new LinkedList<>();

    private TestClass testClass;

    // private Class<?> testClass;

    public XCRunner(Class<?> testClass, RunnerBuilder rb) throws Throwable
    {
        this.testClass = new TestClass(testClass);
        List<Runner> runners = new LinkedList<>();
        // lookup multi-browser configuration
        Browser browser = testClass.getAnnotation(Browser.class);
        if (browser != null)
        {
            runners.add(new BrowserRunner(testClass));
        }

        // scan for JUnit parameters
        for (Method method : testClass.getMethods())
        {
            Parameters parameters = method.getAnnotation(Parameters.class);
            if (parameters != null)
            {
                runners.add(new Parameterized(testClass));
            }
        }

        doMagic(runners);
    }

    private void doMagic(List<Runner> runners)
        throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        for (Runner runner : runners)
        {
            if (runner instanceof ParentRunner<?>)
            {
                Method m = runner.getClass().getDeclaredMethod("getChildren", null);
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
        System.out.println("run: " + notifier);
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
