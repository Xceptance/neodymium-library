package com.xceptance.neodymium.dataprovider;

import java.util.LinkedList;

import com.xceptance.neodymium.dataprovider.core.DataListProvider;

public class AddressDataprovider extends DataListProvider<Address>
{
    public AddressDataprovider()
    {
        dataList = new LinkedList<>();
    }
}
