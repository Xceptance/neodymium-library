package com.xceptance.neodymium.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;

import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import com.xceptance.neodymium.util.AllureAddons;
import com.xceptance.neodymium.util.Neodymium;

public class TestStepListener implements LogEventListener
{
    public static final String LISTENER_NAME = "end-teststep-listener";

    private static final Map<Thread, String> LAST_URL = Collections.synchronizedMap(new WeakHashMap<>());

    private List<String> excludeList = null;

    private List<String> includeList = null;

    public TestStepListener()
    {
        if (!Neodymium.configuration().getExcludeList().isEmpty())
        {
            excludeList = Arrays.asList(Neodymium.configuration().getExcludeList().split("\\s+"));
        }
        if (!Neodymium.configuration().getIncludeList().isEmpty())
        {
            includeList = Arrays.asList(Neodymium.configuration().getIncludeList().split("\\s+"));
        }
    }

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
        if (Neodymium.configuration().enableStepLinks())
        {
            String lastUrl = getLastUrl();
            if (lastUrl != null && !lastUrl.equals(currentUrl) && !currentUrl.equals("data:,"))
            {
                AllureAddons.addLinkToReport("URL changed", Neodymium.getDriver().getCurrentUrl());
            }
            setLastUrl(currentUrl);
        }

        if (this.includeList != null)
        {
            for (String s : includeList)
            {
                Assertions.assertTrue(Pattern.compile(s).matcher(currentUrl).find(), "Opened Link was outside permitted URLs: " + currentUrl);
            }
        }
        else if (this.excludeList != null)
        {
            for (String s : excludeList)
            {
                Assertions.assertTrue(!Pattern.compile(s).matcher(currentUrl).find(), "Opened Link was to forbidden site: " + currentUrl);
            }
        }
    }

    @Override
    public void beforeEvent(LogEvent currentLog)
    {
        // Do nothing as we only need the afterEvent method but both need to be implemented
    }
}