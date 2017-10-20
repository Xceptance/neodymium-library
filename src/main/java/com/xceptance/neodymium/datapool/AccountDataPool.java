package com.xceptance.neodymium.datapool;

import java.util.LinkedList;

import com.xceptance.neodymium.datapool.core.DataListPool;

public class AccountDataPool extends DataListPool<Account>
{
    public AccountDataPool()
    {
        dataList = new LinkedList<>();
    }
}
