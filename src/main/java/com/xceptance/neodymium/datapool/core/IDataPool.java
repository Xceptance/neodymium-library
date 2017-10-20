package com.xceptance.neodymium.datapool.core;

public interface IDataPool<T>
{
    public boolean addEntry(T newEntry);

    public T getEntry(int index);

    public T getEntry();

    public int size();

    public boolean removeEntry(T entry);

    public T removeEntry(int index);

    public T getRandomEntry();

    public Iterable<T> getAllEntries();

    public int addAll(Iterable<T> newEntries);

    public Iterable<T> removeAllEntries();

    public void clear();

}
