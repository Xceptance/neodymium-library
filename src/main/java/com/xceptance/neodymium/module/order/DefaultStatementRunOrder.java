package com.xceptance.neodymium.module.order;

import com.xceptance.neodymium.module.statement.BrowserStatement;
import com.xceptance.neodymium.module.statement.ParameterStatement;
import com.xceptance.neodymium.module.statement.TestdataStatement;

public class DefaultStatementRunOrder extends StatementRunOrder
{
    public DefaultStatementRunOrder()
    {
        runOrder.add(BrowserStatement.class);
        runOrder.add(TestdataStatement.class);
        runOrder.add(ParameterStatement.class);
    }
}
