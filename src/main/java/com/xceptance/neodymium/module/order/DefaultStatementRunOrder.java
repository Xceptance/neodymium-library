package com.xceptance.neodymium.module.order;

import com.xceptance.neodymium.module.statement.BrowserStatement;

public class DefaultStatementRunOrder extends StatementRunOrder
{
    public DefaultStatementRunOrder()
    {
        runOrder.add(BrowserStatement.class);
    }
}
