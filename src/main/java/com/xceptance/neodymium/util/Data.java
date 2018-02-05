package com.xceptance.neodymium.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class accesses the test data of the current {@link Context} and provides convenience methods for type conversion
 * 
 * @author m.kaufmann
 */
public class Data
{

    /**
     * Returns data for the data type requested
     * 
     * @return
     */
    public static <T> T get(final Class<T> clazz)
    {
        // just use what we have and ignore if we have more, so we can build up different objects
        // with just the data that fits
        final ObjectMapper m = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return m.convertValue(Context.get().data, clazz);
    }

    /**
     * Get a test data value as {@link String}
     * 
     * @param key
     *            Name of the test data key
     * @return {@link String} if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static String asString(String key)
    {
        String value = Context.dataValue(key);
        if (value == null)
        {
            throw new IllegalArgumentException("Test data could not be found for key: " + key);
        }

        return value;
    }

    /**
     * Get a test data value as int
     * 
     * @param key
     *            Name of the test data key
     * @return int if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static int asInt(String key)
    {
        return Integer.valueOf(asString(key)).intValue();
    }

    /**
     * Get a test data value as long
     * 
     * @param key
     *            Name of the test data key
     * @return long if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static long asLong(String key)
    {
        return Long.valueOf(asString(key)).longValue();
    }

    /**
     * Get a test data value as double
     * 
     * @param key
     *            Name of the test data key
     * @return double if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static double asDouble(String key)
    {
        return Double.valueOf(asString(key)).doubleValue();
    }

    /**
     * Get a test data value as float
     * 
     * @param key
     *            Name of the test data key
     * @return float if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static float asFloat(String key)
    {
        return Float.valueOf(asString(key)).floatValue();
    }

    /**
     * Get a test data value as boolean
     * 
     * @param key
     *            Name of the test data key
     * @return boolean if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static boolean asBool(String key)
    {
        return Boolean.valueOf(asString(key)).booleanValue();
    }
}
