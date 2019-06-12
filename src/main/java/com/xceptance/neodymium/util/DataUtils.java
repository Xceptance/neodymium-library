package com.xceptance.neodymium.util;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class DataUtils
{
    /**
     * Returns a random email address using UUID.java
     *
     * @return random email
     */
    public static String randomEmail()
    {
        final String uuid = UUID.randomUUID().toString();
        final String data = uuid.replaceAll("-", "");
        final StringBuilder sb = new StringBuilder(42);

        sb.append(Neodymium.configuration().dataUtilsEmailLocalPrefix());
        sb.append(data.concat(data).substring(0, 12));
        sb.append("@");
        sb.append(Neodymium.configuration().dataUtilsEmailDomain());

        return sb.toString().toLowerCase();
    }

    /**
     * A random password that is strong enough for most services
     *
     * @return a password
     */
    public static String randomPassword()
    {
        final String upper = new RandomStringGenerator.Builder().selectFrom("abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray()).build()
                                                                .generate(Neodymium.configuration().dataUtilsPasswordUppercaseCharAmount());

        final String lower = new RandomStringGenerator.Builder().selectFrom("abcdefghijklmnopqrstuvwxyz".toCharArray()).build()
                                                                .generate(Neodymium.configuration().dataUtilsPasswordLowercaseCharAmount());

        final String number = new RandomStringGenerator.Builder().selectFrom("0123456789".toCharArray()).build()
                                                                 .generate(Neodymium.configuration().dataUtilsPasswordDigitAmount());

        final String special = new RandomStringGenerator.Builder().selectFrom(Neodymium.configuration().dataUtilsPasswordSpecialChars().toCharArray()).build()
                                                                  .generate(Neodymium.configuration().dataUtilsPasswordSpecialCharAmount());

        final char[] all = (upper + lower + number + special).toCharArray();
        ArrayUtils.shuffle(all);

        return new String(all);
    }

    /**
     * Returns data for the data type requested
     * 
     * @param <T>
     *            the inferred type
     * @param clazz
     *            A reference to an class that should be instantiated and filled from test data
     * @return an instance of the class provided
     */
    public static <T> T get(final Class<T> clazz)
    {
        Map<String, String> data = Neodymium.getData();

        JsonObject jsonObject = new JsonObject();
        JsonParser parser = new JsonParser();

        // iterate over every data entry and parse the entries to prepare complex structures for object mapping
        for (Iterator<String> iterator = data.keySet().iterator(); iterator.hasNext();)
        {
            String key = (String) iterator.next();
            String value = data.get(key);
            String trimmedValue = StringUtils.defaultString(value).trim();

            if (value == null)
            {
                jsonObject.add(key, null);
            }
            else if (trimmedValue.startsWith("{") || trimmedValue.startsWith("["))
            {
                jsonObject.add(key, parser.parse(value));
            }
            else
            {
                jsonObject.add(key, new JsonPrimitive(value));
            }
        }

        return new Gson().fromJson(jsonObject, clazz);
    }

    /**
     * Get a test data value as {@link String}
     * 
     * @param key
     *            Name of the test data key
     * @return mapped value as {@link String} if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static String asString(String key)
    {
        String value = Neodymium.dataValue(key);
        if (value == null)
        {
            throw new IllegalArgumentException("Test data could not be found for key: " + key);
        }

        return value;
    }

    /**
     * Get a test data value as string or default value if it couldn't be found
     * 
     * @param key
     *            Name of test data key
     * @param defaultValue
     *            a value that will be returned if the key was not found
     * @return mapped value as {@link String} if the key was found else defaultValue
     */
    public static String asString(String key, String defaultValue)
    {
        try
        {
            return asString(key);
        }
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        }
    }

    /**
     * Get a test data value as int
     * 
     * @param key
     *            Name of the test data key
     * @return mapped value as int if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static int asInt(String key)
    {
        return Integer.parseInt(asString(key));
    }

    /**
     * Get a test data value as int or default value if it couldn't be found
     * 
     * @param key
     *            Name of test data key
     * @param defaultValue
     *            a value that will be returned if the key was not found
     * @return mapped value as int if the key was found else defaultValue
     */
    public static int asInt(String key, int defaultValue)
    {
        try
        {
            return asInt(key);
        }
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        }
    }

    /**
     * Get a test data value as long
     * 
     * @param key
     *            Name of the test data key
     * @return mapped value as long if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static long asLong(String key)
    {
        return Long.parseLong(asString(key));
    }

    /**
     * Get a test data value as long or default value if it couldn't be found
     * 
     * @param key
     *            Name of test data key
     * @param defaultValue
     *            a value that will be returned if the key was not found
     * @return mapped value as long if the key was found else defaultValue
     */
    public static long asLong(String key, long defaultValue)
    {
        try
        {
            return asLong(key);
        }
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        }
    }

    /**
     * Get a test data value as double
     * 
     * @param key
     *            Name of the test data key
     * @return mapped value as double if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static double asDouble(String key)
    {
        return Double.parseDouble(asString(key));
    }

    /**
     * Get a test data value as double or default value if it couldn't be found
     * 
     * @param key
     *            Name of test data key
     * @param defaultValue
     *            a value that will be returned if the key was not found
     * @return mapped value as double if the key was found else defaultValue
     */
    public static double asDouble(String key, double defaultValue)
    {
        try
        {
            return asDouble(key);
        }
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        }
    }

    /**
     * Get a test data value as float
     * 
     * @param key
     *            Name of the test data key
     * @return mapped value as float if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static float asFloat(String key)
    {
        return Float.parseFloat(asString(key));
    }

    /**
     * Get a test data value as float or default value if it couldn't be found
     * 
     * @param key
     *            Name of test data key
     * @param defaultValue
     *            a value that will be returned if the key was not found
     * @return mapped value as float if the key was found else defaultValue
     */
    public static float asFloat(String key, float defaultValue)
    {
        try
        {
            return asFloat(key);
        }
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        }
    }

    /**
     * Get a test data value as boolean
     * 
     * @param key
     *            Name of the test data key
     * @return mapped value as boolean if the key was found
     * @throws IllegalArgumentException
     *             if the key was NOT found
     */
    public static boolean asBool(String key)
    {
        return Boolean.parseBoolean(asString(key));
    }

    /**
     * Get a test data value as boolean or default value if it couldn't be found
     * 
     * @param key
     *            Name of test data key
     * @param defaultValue
     *            a value that will be returned if the key was not found
     * @return mapped value as boolean if the key was found else defaultValue
     */
    public static boolean asBool(String key, boolean defaultValue)
    {
        try
        {
            return asBool(key);
        }
        catch (IllegalArgumentException e)
        {
            return defaultValue;
        }
    }
}
