package com.xceptance.xrunner;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.TestWithParameters;

public class XCParameterRunner extends BlockJUnit4ClassRunnerWithParameters
{
    private TestWithParameters test;

    private Object[] parameters;

    private Object testInstance;

    private String testName;

    private MethodExecutionContext methodExecutionContext;

    public XCParameterRunner(TestWithParameters test, MethodExecutionContext methodExecutionContext) throws InitializationError
    {
        super(test);
        this.test = test;
        this.methodExecutionContext = methodExecutionContext;
        parameters = test.getParameters().toArray(new Object[0]);
        StringBuilder sb = new StringBuilder(128);
        sb.append("[");
        List<String> stringParameters = new LinkedList<>();
        for (Object o : parameters)
        {
            stringParameters.add(o.toString());
        }
        sb.append(String.join(", ", stringParameters));
        sb.append("]");

        testName = sb.toString();
    }

    @Override
    public Object createTest() throws Exception
    {
        return super.createTest();
    }

    @Override
    public Description getDescription()
    {
        return Description.createTestDescription(test.getTestClass().getClass(), testName);
    }

    @Override
    public void run(RunNotifier notifier)
    {
        testInstance = methodExecutionContext.getTestClassInstance();

        super.run(notifier);
        try
        {
            createTestUsingFieldInjection();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getName()
    {
        return testName;
    }

    @Override
    protected List<FrameworkMethod> getChildren()
    {
        List<FrameworkMethod> dummy = new LinkedList<>();
        return dummy;
    }

    // TODO: copied from Parameterized
    private Object createTestUsingFieldInjection() throws Exception
    {
        List<FrameworkField> annotatedFieldsByParameter = getTestClass().getAnnotatedFields(Parameter.class);
        if (annotatedFieldsByParameter.size() != parameters.length)
        {
            throw new Exception("Wrong number of parameters and @Parameter fields." + " @Parameter fields counted: " +
                                annotatedFieldsByParameter.size() + ", available parameters: " + parameters.length + ".");
        }
        for (FrameworkField each : annotatedFieldsByParameter)
        {
            Field field = each.getField();
            Parameter annotation = field.getAnnotation(Parameter.class);
            int index = annotation.value();
            try
            {
                field.set(testInstance, parameters[index]);
            }
            catch (IllegalArgumentException iare)
            {
                throw new Exception(getTestClass().getName() + ": Trying to set " + field.getName() + " with the value " +
                                    parameters[index] + " that is not the right type (" + parameters[index].getClass().getSimpleName() +
                                    " instead of " + field.getType().getSimpleName() + ").", iare);
            }
        }
        return testInstance;
    }
}
