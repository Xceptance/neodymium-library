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
    public <T extends DataListProvider<?>> void addDataListProvider(T dataListProvider, String providerInstanceName)
    {
        synchronized (dataListProviderCache)
        {
            Map<String, Object> entry = dataListProviderCache.get(dataListProvider.getClass());
            if (entry == null)
            {
                Map<String, Object> newDataListProviderMap = new HashMap<>();
                newDataListProviderMap.put(providerInstanceName, dataListProvider);
                dataListProviderCache.put((Class<? extends DataListProvider<?>>) dataListProvider.getClass(), newDataListProviderMap);
            }
            else
            {
                entry.put(providerInstanceName, dataListProvider);
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

    public <T> boolean removeDataListProvider(T dataListProviderType, String providerInstanceName)
    {
        synchronized (dataListProviderCache)
        {
            Map<String, Object> dataListProviders = dataListProviderCache.get(dataListProviderType.getClass());

            return (dataListProviders.remove(providerInstanceName) != null);
        }
    }

    public <T> boolean removeDataListProvider(T dataListProviderType)
    {
        return removeDataListProvider(dataListProviderType, "");
    }

    public <T> boolean removeDataListProviderType(T dataListProviderType)
    {
        synchronized (dataListProviderCache)
        {
            Map<String, Object> dataListProvider = dataListProviderCache.get(dataListProviderType.getClass());
            if (dataListProvider != null && dataListProvider.size() > 0)
            {
                dataListProvider.clear();

                return true;
            }
        }
        return false;
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
