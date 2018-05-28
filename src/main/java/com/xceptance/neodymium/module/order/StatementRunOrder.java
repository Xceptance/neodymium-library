package com.xceptance.neodymium.module.order;

import java.util.LinkedList;
import java.util.List;

import com.xceptance.neodymium.module.StatementBuilder;

public class StatementRunOrder
{
    protected List<Class<? extends StatementBuilder>> runOrder = new LinkedList<>();

    public List<Class<? extends StatementBuilder>> getRunOrder()
    {
        return runOrder;
    }
}
