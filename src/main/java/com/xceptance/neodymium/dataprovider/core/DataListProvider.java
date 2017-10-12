package com.xceptance.neodymium.dataprovider.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class DataListProvider<T> implements IDataProvider<T>
{
    protected List<T> dataList;

    @Override
    public boolean addEntry(T newEntry)
    {
        synchronized (dataList)
        {
            if (newEntry != null)
            {
                return dataList.add(newEntry);
            }
        }

        return false;
    }

    @Override
    public T getEntry(int index)
    {
        if (index >= 0)
        {
            synchronized (dataList)
            {
                if (index < dataList.size())
                    return dataList.get(index);
            }
        }

        return null;
    }

    @Override
    public T getEntry()
    {
        synchronized (dataList)
        {
            if (dataList.size() > 0)
                return dataList.get(0);
        }

        return null;
    }

    @Override
    public int size()
    {
        synchronized (dataList)
        {
            return dataList.size();
        }
    }

    @Override
    public boolean removeEntry(T entry)
    {
        synchronized (dataList)
        {
            return dataList.remove(entry);
        }
    }

    @Override
    public T removeEntry(int index)
    {
        if (index >= 0)
        {
            synchronized (dataList)
            {
                if (dataList.size() >= index)
                    return dataList.remove(index);
            }
        }

        return null;
    }

    @Override
    public T getRandomEntry()
    {
        synchronized (dataList)
        {
            if (dataList.size() > 0)
            {
                Random rnd = new Random();
                return dataList.get(rnd.nextInt(dataList.size()));
            }
        }

        return null;
    }

    @Override
    public Iterable<T> getAllEntries()
    {
        return dataList;
    }

    @Override
    public int addAll(Iterable<T> newEntries)
    {
        synchronized (dataList)
        {
            int count = 0;
            for (T entry : newEntries)
            {
                if (dataList.add(entry))
                    count++;
            }

            return count;
        }
    }

    @Override
    public Iterable<T> removeAllEntries()
    {
        synchronized (dataList)
        {
            List<T> copy = new LinkedList<>(dataList);
            dataList.clear();
            return copy;
        }
    }

    @Override
    public void clear()
    {
        synchronized (dataList)
        {
            dataList.clear();
        }
    }
}
