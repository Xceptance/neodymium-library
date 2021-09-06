package com.xceptance.neodymium.module.statement.browser.multibrowser.wrappers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.firefox.GeckoDriverService.Builder;
import org.openqa.selenium.net.PortProber;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;

public class GeckoBuilder extends Builder
{
    private File firefoxBinary;

    private int port;

    private List<String> arguments;

    public GeckoDriverService createDriverService(List<String> arguments)
    {
        firefoxBinary = findDefaultExecutable();
        port = PortProber.findFreePort();
        this.arguments = arguments;
        var logPathArgs = arguments.stream().filter(arg -> arg.contains("--log-path")).collect(Collectors.toList());
        this.arguments.removeAll(logPathArgs);
        var service = createDriverService(firefoxBinary, port, createArgs(), ImmutableMap.copyOf(System.getenv()));

        if (!logPathArgs.isEmpty())
        {
            String firefoxLogFile = logPathArgs.get(0).replaceAll("--log-path=", "").trim();
            if (firefoxLogFile != null)
            {
                if ("/dev/stdout".equals(firefoxLogFile))
                {
                    service.sendOutputTo(System.out);
                }
                else if ("/dev/stderr".equals(firefoxLogFile))
                {
                    service.sendOutputTo(System.err);
                }
                else if ("/dev/null".equals(firefoxLogFile))
                {
                    service.sendOutputTo(ByteStreams.nullOutputStream());
                }
                else
                {
                    try
                    {
                        service.sendOutputTo(new FileOutputStream(firefoxLogFile));
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return service;
    }

    @Override
    protected ImmutableList<String> createArgs()
    {
        ImmutableList.Builder<String> argsBuilder = ImmutableList.builder();
        // argsBuilder.addAll(super.createArgs());
        argsBuilder.add(String.format("--port=%d", port));
        if (firefoxBinary != null)
        {
            argsBuilder.add("-b");
            argsBuilder.add(firefoxBinary.getPath());
        } // else GeckoDriver will be responsible for finding Firefox on the PATH or via a capability.
        if (arguments != null && !arguments.isEmpty())
        {
            argsBuilder.addAll(arguments);
        }
        return argsBuilder.build();
    }
}
