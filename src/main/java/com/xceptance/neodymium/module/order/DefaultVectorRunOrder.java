package com.xceptance.neodymium.module.order;

import com.xceptance.neodymium.module.vector.BrowserVectorBuilder;
import com.xceptance.neodymium.module.vector.MethodVectorBuilder;

public class DefaultVectorRunOrder extends VectorRunOrder
{
    public DefaultVectorRunOrder()
    {
        vectorRunOrder.add(BrowserVectorBuilder.class);
        vectorRunOrder.add(MethodVectorBuilder.class);
        // vectorRunOrder.add(new TestdataVector());
        // vectorRunOrder.add(new ParameterVector());
    }
}
