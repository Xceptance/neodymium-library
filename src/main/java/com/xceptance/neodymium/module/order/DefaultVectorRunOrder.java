package com.xceptance.neodymium.module.order;

import com.xceptance.neodymium.module.vector.BrowserVector;
import com.xceptance.neodymium.module.vector.MethodVector;
import com.xceptance.neodymium.module.vector.TestdataVector;

public class DefaultVectorRunOrder extends VectorRunOrder
{
    public DefaultVectorRunOrder()
    {
        vectorRunOrder.add(new BrowserVector());
        vectorRunOrder.add(new TestdataVector());
        vectorRunOrder.add(new MethodVector());
    }
}
