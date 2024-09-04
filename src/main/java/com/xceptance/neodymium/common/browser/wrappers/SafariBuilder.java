package com.xceptance.neodymium.common.browser.wrappers;

import java.io.File;
import java.util.List;

import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.safari.SafariDriverService;
import org.openqa.selenium.safari.SafariDriverService.Builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class SafariBuilder extends Builder
{
    private File firefoxBinary;

    private int port;

    private List<String> arguments;

    public SafariDriverService createDriverService(List<String> arguments)
    {
        // firefoxBinary = findDefaultExecutable();
        port = PortProber.findFreePort();
        this.arguments = arguments;
        return createDriverService(null, port, getDefaultTimeout(), createArgs(), ImmutableMap.copyOf(System.getenv()));
    }

    @Override
    protected ImmutableList<String> createArgs()
    {
        ImmutableList.Builder<String> argsBuilder = ImmutableList.builder();
        argsBuilder.addAll(super.createArgs());
        argsBuilder.add(String.format("--port=%d", port));
        if (firefoxBinary != null)
        {
            argsBuilder.add("-b");
            argsBuilder.add(firefoxBinary.getPath());
        } // else GeckoDriver will be responsible for finding Firefox on the PATH or via a capability.
        if (arguments != null)
        {
            argsBuilder.addAll(arguments);
        }
        return argsBuilder.build();
    }
}