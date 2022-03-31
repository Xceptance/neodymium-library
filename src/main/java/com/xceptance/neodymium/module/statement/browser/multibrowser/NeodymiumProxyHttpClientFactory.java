package com.xceptance.neodymium.module.statement.browser.multibrowser;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.netty.NeoClient;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.TestEnvironment;

public class NeodymiumProxyHttpClientFactory implements HttpClient.Factory
{
    private TestEnvironment testEnvironment;

    public NeodymiumProxyHttpClientFactory(TestEnvironment testEnvironmentProperties)
    {
        this.testEnvironment = testEnvironmentProperties;
    }

    @Override
    public HttpClient createClient(ClientConfig config)
    {
        try
        {
            String[] urlParts = testEnvironment.getUrl().split("//");
            URL gridUrl = new URL(urlParts[0] + "//" + testEnvironment.getUsername() + ":" + testEnvironment.getPassword() + "@" + urlParts[1]);
            config = config.baseUrl(gridUrl);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        if (testEnvironment.useProxy())
        {
            config = config.proxy(new Proxy(Type.HTTP, new InetSocketAddress(testEnvironment.getProxyHost(), testEnvironment.getProxyPort())));
            String proxyUsername = testEnvironment.getProxyUsername();
            String proxyPassword = testEnvironment.getProxyPassword();
            if (StringUtils.isNoneEmpty(proxyUsername, proxyPassword))
            {
                config = config.authenticateAs(new UsernameAndPassword(proxyUsername, proxyPassword));
            }
        }
        return new NeoClient.Factory().createClient(config);
    }
}
