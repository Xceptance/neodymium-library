package com.xceptance.neodymium.module.vector;

import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;

public class ExecutionRunner implements Cloneable
{
    private List<RunVector> testExecutionRunners = new LinkedList<>();

    private Description testDescription;

    public void addTestExecutionRunner(RunVector testExecutionRunner)
    {
        testExecutionRunners.add(testExecutionRunner);
    }

    public List<RunVector> getTestExecutionRunner()
    {
        return testExecutionRunners;
    }

    public void setTestDescription(Description description)
    {
        testDescription = description;
    }

    public Description getTestDescription()
    {
        return testDescription;
    }

    @Override
    public ExecutionRunner clone()
    {
        ExecutionRunner newExecutionRunner = new ExecutionRunner();

        newExecutionRunner.testExecutionRunners.addAll(this.getTestExecutionRunner());
        newExecutionRunner.testDescription = this.getTestDescription();

        return newExecutionRunner;
    }
}
