package com.xceptance.neodymium.util;

import java.util.Map;

import com.codeborne.selenide.Selenide;

public class NeoWaitTime
{
    private final int standardWait, shortWait, doubleWait, longWait;

    private final Map<String, String> customWaitTimeMap;

    private static NeoWaitTime INSTANCE;

    private NeoWaitTime() {
        this.standardWait = Neodymium.configuration().getStandardWaitTime();
        this.shortWait = Neodymium.configuration().getShortWaitTime();
        this.doubleWait = Neodymium.configuration().getDoubleWaitTime();
        this.longWait = Neodymium.configuration().getLongWaitTime();
        this.customWaitTimeMap = convertToMap(Neodymium.configuration().getCustomWaitTimes());
    }

    public static void waitStandardWaitTime()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new NeoWaitTime();
        }
        Selenide.sleep(INSTANCE.standardWait);
    }

    public static void waitShortWaitTime()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new NeoWaitTime();
        }
        Selenide.sleep(INSTANCE.shortWait);
    }

    public static void waitDoubleWaitTime()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new NeoWaitTime();
        }
        Selenide.sleep(INSTANCE.doubleWait);
    }

    public static void waitLongWaitTime()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new NeoWaitTime();
        }
        Selenide.sleep(INSTANCE.longWait);
    }

    public static void waitCustomWaitTime(String key)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new NeoWaitTime();
        }
        Selenide.sleep(Integer.valueOf(INSTANCE.customWaitTimeMap.get(key)));
    }
}
