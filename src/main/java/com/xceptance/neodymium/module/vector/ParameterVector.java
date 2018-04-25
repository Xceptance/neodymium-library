package com.xceptance.neodymium.module.vector;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.model.FrameworkField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParameterVector implements RunVector
{
    public static Logger LOGGER = LoggerFactory.getLogger(ParameterVector.class);

    private Object testClassInstance;

    private Object[] parameter;

    private List<FrameworkField> parameterFrameworkFields;

    private int parameterSetIndex;

    public ParameterVector(List<FrameworkField> parameterFrameworkFields, Object[] parameter, int parameterSetIndex)
    {
        this.parameterFrameworkFields = parameterFrameworkFields;
        this.parameter = parameter;
        this.parameterSetIndex = parameterSetIndex;
    }

    @Override
    public void beforeMethod()
    {
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
    public void afterMethod()
    {
        // nothing
    }

    @Override
    public String getTestName()
    {
        return "[" + parameterSetIndex + "]";
    }

    @Override
    public int vectorHashCode()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTestClassInstance(Object testClassInstance)
    {
        this.testClassInstance = testClassInstance;

    }

    private void injectTestParameter() throws Exception
    {
        if (parameterFrameworkFields.size() != parameter.length)
        {
            throw new Exception("Number of parameters (" + parameter.length + ") and " + //
                                "fields (" + parameterFrameworkFields.size() + ") " + //
                                "annotated with @Parameter must match!");
        }

        for (FrameworkField parameterFrameworkField : parameterFrameworkFields)
        {
            Field field = parameterFrameworkField.getField();
            int parameterIndex = field.getAnnotation(Parameter.class).value();

            LOGGER.debug("Set parameter \"" + parameterFrameworkField.getName() + "\" to \"" + parameter[parameterIndex] + "\"");
            setField(field, parameter[parameterIndex]);
        }
    }

    private void setField(Field field, Object value)
    {
        Class<?> fieldType = field.getType();
        Class<?> valueType;
        if (value == null)
        {
            valueType = Void.class;
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
            field.set(testClassInstance, value);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException("Could not set parameter of type " + valueType + " to field \"" + field.getName() + "\" of type " +
                                       fieldType + ". Value: " + value);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Could not set parameter due to it is not public or it is final");
        }
    }
}
