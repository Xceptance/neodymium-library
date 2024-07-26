package com.xceptance.neodymium.common;

import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import com.xceptance.neodymium.util.AllureAddons;
import com.xceptance.neodymium.util.Neodymium;

public class EndTestStepListener implements LogEventListener
{
    public static final String LISTENER_NAME = "end-teststep-listener";

    private String lastUrl = "";

    @Override
    public void afterEvent(LogEvent currentLog)
    {
        String currentUrl = Neodymium.getDriver().getCurrentUrl();
        if (!lastUrl.equals(currentUrl) && !currentUrl.equals("data:,"))
        {
            AllureAddons.addLinkToReport("Site changed", Neodymium.getDriver().getCurrentUrl());
        }
        lastUrl = currentUrl;
    }

    @Override
    public void beforeEvent(LogEvent currentLog)
    {
        // Do nothing as we only need the afterEvent method but both need to be implemented
        assert true;
    }
}