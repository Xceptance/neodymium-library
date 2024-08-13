package com.xceptance.neodymium.junit5.testend;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.common.collect.ImmutableMap;
import com.xceptance.neodymium.util.AllureAddons;
import com.xceptance.neodymium.util.Neodymium;
import com.xceptance.neodymium.util.SaveDataUtil;

public class NeodymiumAfterAllCallback implements AfterAllCallback
{

    @Override
    public void afterAll(ExtensionContext context) throws Exception
    {
        AllureAddons.addEnvironmentInformation(ImmutableMap.<String, String> builder().put("Used Browsers", Neodymium.getData().get(SaveDataUtil.USED_BROWSER))
                                                           .build());
    }
}
