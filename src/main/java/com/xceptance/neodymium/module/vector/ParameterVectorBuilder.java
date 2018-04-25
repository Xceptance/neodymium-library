package com.xceptance.neodymium.module.vector;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParameterVectorBuilder implements RunVectorBuilder
{
    public static Logger LOGGER = LoggerFactory.getLogger(ParameterVectorBuilder.class);

    private List<FrameworkField> parameterFrameworkFields;

    private Iterable<Object> parameter = null;

    @Override
    public void create(TestClass testClass, FrameworkMethod frameworkMethod) throws Exception
    {
        List<FrameworkMethod> parametersMethods = testClass.getAnnotatedMethods(Parameters.class);

        for (FrameworkMethod parametersMethod : parametersMethods)
        {
            if (parametersMethod.isPublic() && parametersMethod.isStatic())
            {
                // take the first public static method. invoke it and use the result as parameter
                try
                {
                    Object parametersResult = parametersMethod.invokeExplosively(null);
                    if (parametersResult instanceof Iterable)
                    {
                        parameter = (Iterable<Object>) parametersResult;
                    }
                    else if (parametersResult instanceof Object[])
                    {
                        parameter = Arrays.asList((Object[]) parametersResult);
                    }
                    else
                    {
                        String msg = MessageFormat.format("{0}.{1}() must return an Iterable of arrays.",
                                                          testClass.getJavaClass().getName(), parametersMethod.getName());
                        throw new Exception(msg);
                    }
                    break;
                }
                catch (Throwable e)
                {
                    throw new RuntimeException(e);
                }
            }
        }

        if (!parametersMethods.isEmpty() && parameter == null)
        {
            throw new Exception("No public static parameters method on class");
        }

        parameterFrameworkFields = testClass.getAnnotatedFields(Parameter.class);
        LOGGER.debug("Found " + parameterFrameworkFields.size() + " parameter fields");

    }

    @Override
    public List<RunVector> buildRunVectors()
    {
        List<RunVector> r = new LinkedList<>();

        if (parameter != null)
        {
            for (Object para : parameter)
            {
                Object[] p;
                if (para instanceof Object[])
                {
                    p = (Object[]) para;
                }
                else
                {
                    p = new Object[]
                        {
                            para
                        };
                }

                r.add(new ParameterVector(parameterFrameworkFields, p));
            }
        }

        return r;
    }

}
