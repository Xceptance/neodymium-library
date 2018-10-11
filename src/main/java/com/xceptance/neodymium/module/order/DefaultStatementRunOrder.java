package com.xceptance.neodymium.module.order;

import com.xceptance.neodymium.module.statement.browser.BrowserStatement;
import com.xceptance.neodymium.module.statement.parameter.ParameterStatement;
import com.xceptance.neodymium.module.statement.testdata.TestdataStatement;

public class DefaultStatementRunOrder extends StatementRunOrder
{
    public DefaultStatementRunOrder()
    {
        runOrder.add(BrowserStatement.class);
        runOrder.add(ParameterStatement.class);
        runOrder.add(TestdataStatement.class);
    }
}
