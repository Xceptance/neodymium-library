package com.xceptance.neodymium.datapool;

import java.util.LinkedList;

import com.xceptance.neodymium.datapool.core.DataListPool;

public class AddressDataPool extends DataListPool<Address>
{
    public AddressDataPool()
    {
        dataList = new LinkedList<>();
    }
}
