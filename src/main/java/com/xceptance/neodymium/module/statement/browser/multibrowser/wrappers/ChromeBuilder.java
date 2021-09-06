package com.xceptance.neodymium.module.statement.browser.multibrowser.wrappers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeDriverService.Builder;
import org.openqa.selenium.net.PortProber;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ChromeBuilder extends Builder
{
    private File firefoxBinary;

    private int port;

    private List<String> arguments;

    public ChromeDriverService createDriverService(List<String> arguments)
    {
        firefoxBinary = findDefaultExecutable();
        port = PortProber.findFreePort();
        this.arguments = arguments;
        try
        {
            return new ChromeDriverService(firefoxBinary, port, createArgs(), ImmutableMap.copyOf(System.getenv()));
        }
        catch (IOException e)
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
