package com.xceptance.neodymium.module.order;

import com.xceptance.neodymium.module.vector.BrowserVectorBuilder;

public class DefaultVectorRunOrder extends VectorRunOrder
{
    public DefaultVectorRunOrder()
    {
        vectorRunOrder.add(BrowserVectorBuilder.class);
        // vectorRunOrder.add(new TestdataVector());
        // vectorRunOrder.add(new ParameterVector());
    }
}
