package com.xceptance.neodymium;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeodymiumParameterRunner extends BlockJUnit4ClassRunnerWithParameters
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NeodymiumParameterRunner.class);

    private TestWithParameters test;

    private Object[] parameters;

    private Object testInstance;

    private MethodExecutionContext methodExecutionContext;

    public NeodymiumParameterRunner(TestWithParameters test, MethodExecutionContext methodExecutionContext) throws InitializationError
    {
        super(test);
        this.test = test;
        this.methodExecutionContext = methodExecutionContext;
        parameters = test.getParameters().toArray(new Object[0]);
    }

    @Override
    public Object createTest() throws Exception
    {
        return super.createTest();
    }

    @Override
    public Description getDescription()
    {
        return Description.createTestDescription(test.getTestClass().getClass(), test.getName());
    }

    @Override
    public void run(RunNotifier notifier)
    {
        testInstance = methodExecutionContext.getTestClassInstance();

        super.run(notifier);
        try
        {
            injectTestParameter();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getName()
    {
        return test.getName();
    }

    @Override
    protected List<FrameworkMethod> getChildren()
    {
        List<FrameworkMethod> dummy = new LinkedList<>();
        return dummy;
    }

    private void injectTestParameter() throws Exception
    {
        List<FrameworkField> parameterFrameworkFields = getTestClass().getAnnotatedFields(Parameter.class);
        LOGGER.debug("Found " + parameterFrameworkFields.size() + " parameter fields");
        if (parameterFrameworkFields.size() != parameters.length)
        {
            throw new Exception("Number of parameters (" + parameters.length + ") and " + //
                                "fields (" + parameterFrameworkFields.size() + ") " + //
                                "annotated with @Parameter must match!");
        }

        for (FrameworkField parameterFrameworkField : parameterFrameworkFields)
        {
            Field field = parameterFrameworkField.getField();
            int parameterIndex = field.getAnnotation(Parameter.class).value();

            LOGGER.debug("Set parameter \"" + parameterFrameworkField.getName() + "\" to \"" + parameters[parameterIndex] + "\"");
            setField(field, parameters[parameterIndex]);
        }
    }

    private void setField(Field field, Object value)
    {
        Class<?> fieldType = field.getType();
        Class<?> valueType;
        if (value == null)
        {
            valueType = Null.class;
        }
        else
        {
            valueType = value.getClass();
        }

        // try to convert String values to appropriate target types
        if (valueType == String.class)
        {
            try
            {
                if (fieldType == int.class || fieldType == Integer.class)
                {
                    value = Integer.valueOf((String) value);
                }
                else if (fieldType == long.class || fieldType == Long.class)
                {
                    value = Long.valueOf((String) value);
                }
                else if (fieldType == double.class || fieldType == Double.class)
                {
                    value = Double.valueOf((String) value);
                }
                else if (fieldType == float.class || fieldType == Float.class)
                {
                    value = Float.valueOf((String) value);
                }
                else if (fieldType == boolean.class || fieldType == Boolean.class)
                {
                    value = Boolean.valueOf((String) value);
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException("An error occured during conversion of input string \"" + (String) value + "\" to type " +
                                           fieldType.getName() + " for field \"" + field.getName() + "\"", e);
            }
        }

        try
        {
            field.set(testInstance, value);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException("Could not set parameter of type " + valueType + " to field \"" + field.getName() + "\" of type " +
                                       fieldType + ". Value: " + value);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Could not set parameter due to it is not public");
        }
    }
}
