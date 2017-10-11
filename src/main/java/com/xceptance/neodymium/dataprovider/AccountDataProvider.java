package com.xceptance.neodymium.dataprovider;

import java.util.LinkedList;

import com.xceptance.neodymium.dataprovider.core.DataListProvider;

public class AccountDataProvider extends DataListProvider<Account>
{
    public AccountDataProvider()
    {
        dataList = new LinkedList<>();
    }
}
