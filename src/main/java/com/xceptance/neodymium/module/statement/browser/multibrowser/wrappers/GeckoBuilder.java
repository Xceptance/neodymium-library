package com.xceptance.neodymium.module.statement.browser.multibrowser.wrappers;

import static java.util.Collections.unmodifiableList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.GeckoDriverService.Builder;

public class GeckoBuilder extends Builder
{
    private List<String> arguments;

    public GeckoBuilder withArgs(List<String> args)
    {
        this.arguments = args;
        return this;
    }

    @Override
    protected List<String> createArgs()
    {
        List<String> args = new ArrayList<>();
        if (arguments != null && !arguments.isEmpty())
        {
            int indexOfLogs = arguments.indexOf("--log");
            if (indexOfLogs > -1)
            {
                withLogLevel(FirefoxDriverLogLevel.fromString(arguments.get(indexOfLogs + 1)));
                arguments.remove(indexOfLogs);
                arguments.remove(indexOfLogs);
            }

            List<String> logPaths = arguments.stream().filter(arg -> arg.contains("--log-path=")).collect(Collectors.toList());
            if (!logPaths.isEmpty())
            {
                String logPath = logPaths.get(logPaths.size() - 1);
                withLogFile(new File(logPath.replace("--log-path=", "")));
                arguments.remove(arguments.indexOf(logPath));
            }
            args.addAll(arguments);
        }
        args.addAll(super.createArgs());
        return unmodifiableList(args);
    }
}
