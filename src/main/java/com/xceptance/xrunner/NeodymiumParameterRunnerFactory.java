package com.xceptance.xrunner;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

public class NeodymiumParameterRunnerFactory implements ParametersRunnerFactory
{

    private MethodExecutionContext methodExecutionContext;

    public NeodymiumParameterRunnerFactory(MethodExecutionContext methodExecutionContext)
    {
        this.methodExecutionContext = methodExecutionContext;
    }

    @Override
    public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError
    {
        return new NeodymiumParameterRunner(test, methodExecutionContext);
    }

}
