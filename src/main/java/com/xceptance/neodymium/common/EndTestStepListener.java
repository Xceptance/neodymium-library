package com.xceptance.neodymium.common;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import com.xceptance.neodymium.util.AllureAddons;
import com.xceptance.neodymium.util.Neodymium;

public class EndTestStepListener implements LogEventListener
{
    public static final String LISTENER_NAME = "end-teststep-listener";

    private static final Map<Thread, String> LAST_URL = Collections.synchronizedMap(new WeakHashMap<>());

    private static String getLastUrl()
    {
        return LAST_URL.get(Thread.currentThread());
    }

    private static void setLastUrl(String lastUrl)
    {
        LAST_URL.put(Thread.currentThread(), lastUrl);
    }

    @Override
    public void afterEvent(LogEvent currentLog)
    {
        String currentUrl = Neodymium.getDriver().getCurrentUrl();
        String lastUrl = getLastUrl();
        if (!lastUrl.equals(currentUrl) && !currentUrl.equals("data:,"))
        {
            AllureAddons.addLinkToReport("URL changed", Neodymium.getDriver().getCurrentUrl());
        }
        setLastUrl(currentUrl);
    }

    @Override
    public void beforeEvent(LogEvent currentLog)
    {
        // Do nothing as we only need the afterEvent method but both need to be implemented
    }
}