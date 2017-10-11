package com.xceptance.neodymium.dataprovider.core;

import java.util.HashMap;
import java.util.Map;

public class DataListProviderCache
{
    private Map<Class<? extends DataListProvider<?>>, Map<String, Object>> dataProviderCache = new HashMap<>();

    private DataListProviderCache()
    {

    }

    public <T extends DataListProvider<?>> void addDataProvider(T dataProvider)
    {
        addDataProvider(dataProvider, "");
    }

    @SuppressWarnings("unchecked")
    public <T extends DataListProvider<?>> void addDataProvider(T dataProvider, String instanceName)
    {
        Map<String, Object> entry = dataProviderCache.get(dataProvider.getClass());
        if (entry == null)
        {
            Map<String, Object> newDataProviderMap = new HashMap<>();
            newDataProviderMap.put(instanceName, dataProvider);
            dataProviderCache.put((Class<? extends DataListProvider<?>>) dataProvider.getClass(), newDataProviderMap);
        }
        else
        {
            entry.put(instanceName, dataProvider);
        }
    }

    public <T> Object getDataProvider(T dataType)
    {
        return getDataProvider(dataType, "");
    }

    @SuppressWarnings("unchecked")
    public <T> Object getDataProvider(T dataType, String providerInstance)
    {
        Map<String, Object> dataProviderMap = dataProviderCache.get(dataType);
        if (dataProviderMap != null)
        {
            return (T) dataProviderMap.get(providerInstance);
        }

        return null;
    }

    public static DataListProviderCache getInstance()
    {
        return DataListProviderCacheHolder.INSTANCE;
    }

    private static class DataListProviderCacheHolder
    {
        private static final DataListProviderCache INSTANCE = new DataListProviderCache();
    }
}
