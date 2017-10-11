package com.xceptance.neodymium.dataprovider.core;

import java.util.HashMap;
import java.util.Map;

public class DataListProviderCache
{
    private Map<Class<? extends DataListProvider<?>>, Map<String, Object>> dataListProviderCache = new HashMap<>();

    private DataListProviderCache()
    {

    }

    public <T extends DataListProvider<?>> void addDataListProvider(T dataListProvider)
    {
        addDataListProvider(dataListProvider, "");
    }

    @SuppressWarnings("unchecked")
    public <T extends DataListProvider<?>> void addDataListProvider(T dataListProvider, String instanceName)
    {
        synchronized (dataListProviderCache)
        {
            Map<String, Object> entry = dataListProviderCache.get(dataListProvider.getClass());
            if (entry == null)
            {
                Map<String, Object> newDataListProviderMap = new HashMap<>();
                newDataListProviderMap.put(instanceName, dataListProvider);
                dataListProviderCache.put((Class<? extends DataListProvider<?>>) dataListProvider.getClass(), newDataListProviderMap);
            }
            else
            {
                entry.put(instanceName, dataListProvider);
            }
        }
    }

    public <T> Object getDataListProvider(T dataListProviderType)
    {
        return getDataListProvider(dataListProviderType, "");
    }

    @SuppressWarnings("unchecked")
    public <T> Object getDataListProvider(T dataListProviderType, String providerInstanceName)
    {
        synchronized (dataListProviderCache)
        {
            Map<String, Object> dataListProviderMap = dataListProviderCache.get(dataListProviderType);
            if (dataListProviderMap != null)
            {
                return (T) dataListProviderMap.get(providerInstanceName);
            }
        }

        return null;
    }

    public <T> Map<String, Object> getAllDataListProvider(T dataListProviderType)
    {
        synchronized (dataListProviderCache)
        {
            return dataListProviderCache.get(dataListProviderType);
        }
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
