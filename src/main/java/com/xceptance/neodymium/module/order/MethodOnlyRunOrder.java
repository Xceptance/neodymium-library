package com.xceptance.neodymium.module.order;

import com.xceptance.neodymium.module.vector.MethodVector;

public class MethodOnlyRunOrder extends VectorRunOrder
{
    public MethodOnlyRunOrder()
    {
        vectorRunOrder.add(new MethodVector());
    }
}
