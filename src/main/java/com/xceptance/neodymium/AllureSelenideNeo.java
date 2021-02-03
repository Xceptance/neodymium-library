package com.xceptance.neodymium;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import com.codeborne.selenide.logevents.LogEvent;

import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;

public class AllureSelenideNeo extends AllureSelenide
{
    @Override
    public void afterEvent(final LogEvent event)
    {
        if (event.getStatus().equals(LogEvent.EventStatus.FAIL))
        {
            Allure.addAttachment("Information about step failure: ", getFailureDetails(event.getError()));
        }
        super.afterEvent(event);

    }

    private String getFailureDetails(Throwable error)
    {
        return new StringBuffer().append(Optional.ofNullable(error.getMessage()).orElse(error.getClass().getName())).append("\n")
                                 .append(getStackTraceAsString(error)).toString();
    }

    private static String getStackTraceAsString(final Throwable throwable)
    {
        final StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}
