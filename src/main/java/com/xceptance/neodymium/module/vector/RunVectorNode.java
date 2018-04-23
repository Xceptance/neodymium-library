package com.xceptance.neodymium.module.vector;

import java.util.LinkedList;
import java.util.List;

public class RunVectorNode
{
    public Class<? extends RunVectorBuilder> runVectorBuilder;

    public int vectorHashCode;

    public List<RunVector> runVectors;

    public List<RunVectorNode> childNodes = new LinkedList<>();

    public RunVectorNode(List<List<RunVector>> newRunVectors)
    {
        add(newRunVectors);
    }

    private RunVectorNode(List<List<RunVector>> newRunVectors, RunVector runVector)
    {
        this.vectorHashCode = runVector.vectorHashCode();
        this.runVectorBuilder = (Class<? extends RunVectorBuilder>) runVector.getClass();
        add(newRunVectors);
    }

    public void add(List<List<RunVector>> newRunVectors)
    {
        if (newRunVectors == null)
            throw new NullPointerException("Parameter mustn't be null");

        RunVector runVector = newRunVectors.get(0).get(0);
        if (runVectorBuilder != null && runVector.vectorHashCode() == vectorHashCode &&
            runVector.getClass().getTypeName().equals(runVectorBuilder.getTypeName()))
        {
            runVectors = newRunVectors.get(0);
            if (newRunVectors.size() > 1)
            {
                List<List<RunVector>> remaining = new LinkedList<>();
                remaining.addAll(newRunVectors.subList(1, newRunVectors.size()));
                add(remaining);
            }
        }
        else
        {
            // check if there is a child that matches the vector hash code and vector builder
            // if there is one we add it to its childs
            // if not we create a new child

            boolean foundChildVector = false;
            for (RunVectorNode childNode : childNodes)
            {
                if (childNode.vectorHashCode == runVector.vectorHashCode() &&
                    runVector.getClass().getTypeName().equals(childNode.runVectorBuilder.getTypeName()))
                {
                    foundChildVector = true;
                    childNode.add(newRunVectors);
                }
            }

            if (!foundChildVector)
                childNodes.add(new RunVectorNode(newRunVectors, runVector));
        }
    }
}
