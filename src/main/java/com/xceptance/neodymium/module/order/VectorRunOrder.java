package com.xceptance.neodymium.module.order;

import java.util.LinkedList;
import java.util.List;

import com.xceptance.neodymium.module.vector.RunVectorBuilder;

public abstract class VectorRunOrder
{
    protected List<Class<? extends RunVectorBuilder>> vectorRunOrder = new LinkedList<>();

    public List<Class<? extends RunVectorBuilder>> getVectorRunOrder()
    {
        return vectorRunOrder;
    }
}
