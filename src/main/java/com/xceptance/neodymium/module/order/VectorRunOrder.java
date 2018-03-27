package com.xceptance.neodymium.module.order;

import java.util.LinkedList;
import java.util.List;

import com.xceptance.neodymium.module.vector.RunnerVector;

public abstract class VectorRunOrder
{
    protected List<RunnerVector> vectorRunOrder = new LinkedList<>();

    public List<RunnerVector> getVectorRunOrder()
    {
        return vectorRunOrder;
    }
}
