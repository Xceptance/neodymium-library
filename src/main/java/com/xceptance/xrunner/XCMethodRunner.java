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

    private MethodExecutionContext methodExecutionContext;

    private XCMethodRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    public XCMethodRunner(Class<?> klass, FrameworkMethod method, MethodExecutionContext methodExecutionContext) throws InitializationError
    {
        super(klass);
        this.methodExecutionContext = methodExecutionContext;
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
            notifier = new RunNotifier();
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
                if (methodExecutionContext.isRunBeforeClass())
                {
                    statement = withBeforeClasses(statement);
                }

                if (methodExecutionContext.isRunAfterClass())
                {
                    statement = withAfterClasses(statement);
                }
            }
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
}
