package com.xceptance.neodymium.module.statement.browser.multibrowser.wrappers;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerDriverService.Builder;
import org.openqa.selenium.net.PortProber;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class IEBuilder extends Builder
{
    private File firefoxBinary;

    private int port;

    private List<String> arguments;

    public InternetExplorerDriverService createDriverService(List<String> arguments)
    {
        // firefoxBinary = findDefaultExecutable();
        port = PortProber.findFreePort();
        this.arguments = arguments;
        try
        {
            return createDriverService(null, port, getDefaultTimeout(), createArgs(), ImmutableMap.copyOf(System.getenv()));
        }
        catch (WebDriverException e)
        {
            e.printStackTrace();
            return null;
        }
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
