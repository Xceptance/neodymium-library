package com.xceptance.neodymium.datapool;

import java.util.LinkedList;

import com.xceptance.neodymium.datapool.core.DataListPool;
import com.xceptance.neodymium.datapool.core.DataListPoolCache;

public class AccountDataPool extends DataListPool<Account>
{
    public AccountDataPool()
    {
        dataList = new LinkedList<>();
        DataListPoolCache.getInstance().addDataListProvider(this);
    }
}
