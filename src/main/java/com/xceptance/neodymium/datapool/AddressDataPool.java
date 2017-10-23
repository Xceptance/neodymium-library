package com.xceptance.neodymium.datapool;

import java.util.LinkedList;

import com.xceptance.neodymium.datapool.core.DataListPool;
import com.xceptance.neodymium.datapool.core.DataListPoolCache;

public class AddressDataPool extends DataListPool<Address>
{
    public AddressDataPool()
    {
        dataList = new LinkedList<>();
        DataListPoolCache.getInstance().addDataListProvider(this);
    }
}
