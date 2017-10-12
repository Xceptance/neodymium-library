package com.xceptance.neodymium.dataprovider;

import java.util.LinkedList;

import com.xceptance.neodymium.dataprovider.core.DataListProvider;

public class AddressDataProvider extends DataListProvider<Address>
{
    public AddressDataProvider()
    {
        dataList = new LinkedList<>();
    }
}
