package com.xceptance.neodymium.module.order;

import java.util.LinkedList;
import java.util.List;

import com.xceptance.neodymium.module.vector.VectorBuilder;

public abstract class VectorRunOrder
{
    protected List<Class<? extends VectorBuilder>> vectorRunOrder = new LinkedList<>();

    public List<Class<? extends VectorBuilder>> getVectorRunOrder()
    {
        return vectorRunOrder;
    }
}
