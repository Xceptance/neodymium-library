package com.xceptance.neodymium.testclasses.proxy;

import org.aeonbits.owner.ConfigFactory;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

import com.xceptance.neodymium.module.statement.browser.multibrowser.BrowserRunnerHelper;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.ProxyConfiguration;

public class SetProxForWebDriver
{
    @Test
    public void testChrome()
    {
        DesiredCapabilities capabilities = createCapabilitiesWithProxy();
        ChromeOptions options = new ChromeOptions();
        options.merge(capabilities);

        Assert.assertTrue(options.getCapability(CapabilityType.PROXY) != null);
    }

    @Test
    public void testFirefox()
    {
        DesiredCapabilities capabilities = createCapabilitiesWithProxy();
        FirefoxOptions options = new FirefoxOptions();
        options.merge(capabilities);

        Assert.assertTrue(options.getCapability(CapabilityType.PROXY) != null);
    }

    @Test
    public void testInternetExplorer()
    {
        DesiredCapabilities capabilities = createCapabilitiesWithProxy();
        InternetExplorerOptions options = new InternetExplorerOptions();
        options.merge(capabilities);

        Assert.assertTrue(options.getCapability(CapabilityType.PROXY) != null);
    }

    @Test
    public void testSafari()
    {
        DesiredCapabilities capabilities = createCapabilitiesWithProxy();
        SafariOptions options = new SafariOptions();
        options.merge(capabilities);

        Assert.assertTrue(options.getCapability(CapabilityType.PROXY) != null);
    }

    private DesiredCapabilities createCapabilitiesWithProxy()
    {
        final DesiredCapabilities capabilities = new DesiredCapabilities();

        ProxyConfiguration proxyConfig = ConfigFactory.create(ProxyConfiguration.class);
        if (proxyConfig.useProxy())
        {
            capabilities.setCapability(CapabilityType.PROXY, BrowserRunnerHelper.createProxyCapabilities(proxyConfig));
        }

        return capabilities;
    }
}
