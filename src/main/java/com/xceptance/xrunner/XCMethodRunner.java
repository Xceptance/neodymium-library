package com.xceptance.xrunner;

import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class XCMethodRunner extends BlockJUnit4ClassRunner implements ITestClassInjector
{

    List<FrameworkMethod> methodToRun;

    private Object testInstance;

    private boolean runBeforeClass = false;

    private boolean runAfterClass = false;

    private XCMethodRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    public XCMethodRunner(Class<?> klass, FrameworkMethod method) throws InitializationError
    {
        super(klass);
        methodToRun = new LinkedList<>();
        methodToRun.add(method);
    }

    @Override
    protected List<FrameworkMethod> getChildren()
    {
        return methodToRun;
    }

    @Override
    public void run(RunNotifier notifier)
    {
        try
        {
            Statement statement = childrenInvoker(notifier);

            List<FrameworkMethod> annotatedMethods = getTestClass().getAnnotatedMethods(Test.class);
            boolean allTestMethodsIgnored = true;
            for (FrameworkMethod method : annotatedMethods)
            {
                if (method.getAnnotation(Ignore.class) == null)
                {
                    allTestMethodsIgnored = false;
                    break;
                }
            }
            if (!allTestMethodsIgnored)
            {
                if (isRunBeforeClass())
                {
                    statement = withBeforeClasses(statement);
                    setRunBeforeClass(false); // this is ugly but necessary
                }

                if (isRunAfterClass())
                {
                    statement = withAfterClasses(statement);
                    setRunAfterClass(false); // this is ugly but necessary
                }
            }
            // Statement statement = classBlock(new RunNotifier()); // use dummy notifier
            statement.evaluate();
        }
        catch (AssumptionViolatedException e)
        {
        }
        catch (StoppedByUserException e)
        {
            throw e;
        }
        catch (Throwable e)
        {
        }
    }

    @Override
    protected Object createTest() throws Exception
    {
        return testInstance;
    }

    @Override
    public void setTestClass(Object instance)
    {
        testInstance = instance;
    }

    @Override
    public Description getDescription()
    {
        FrameworkMethod method = methodToRun.get(0);
        Description description = Description.createSuiteDescription(method.getName(), getRunnerAnnotations());
        return description;
    }

    public boolean isRunBeforeClass()
    {
        return runBeforeClass;
    }

    public void setRunBeforeClass(boolean runBeforeClass)
    {
        this.runBeforeClass = runBeforeClass;
    }

    public boolean isRunAfterClass()
    {
        return runAfterClass;
    }

    public void setRunAfterClass(boolean runAfterClass)
    {
        this.runAfterClass = runAfterClass;
    }

}
