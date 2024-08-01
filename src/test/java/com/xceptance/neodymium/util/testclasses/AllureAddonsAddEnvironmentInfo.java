package com.xceptance.neodymium.util.testclasses;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;
import com.xceptance.neodymium.util.AllureAddons;
import com.xceptance.neodymium.util.Neodymium;

public class AllureAddonsAddEnvironmentInfo
{

    @Test
    public void testAddEnviromentInformation()
    {
        for (int i = 0; i < 10; i++)
        {
            AddEnvironmentInfoThread thread = new AddEnvironmentInfoThread(i);
            thread.start();
        }
    }
}

class AddEnvironmentInfoThread extends Thread
{

    private int number;

    public AddEnvironmentInfoThread(int number)
    {
        this.number = number;
    }

    public void run()
    {
        AllureAddons.addEnvironmentInformation(ImmutableMap.<String, String> builder()
                                                           .put("Testing Framework Thread" + number, "Neodymium " + Neodymium.getNeodymiumVersion())
                                                           .build());
    }
}