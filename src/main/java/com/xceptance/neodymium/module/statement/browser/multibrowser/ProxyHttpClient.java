package com.xceptance.neodymium.module.statement.browser.multibrowser;

import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.openqa.selenium.remote.http.HttpClient.Builder;
import org.openqa.selenium.remote.internal.OkHttpClient;

public class ProxyHttpClient implements org.openqa.selenium.remote.http.HttpClient.Factory
{

    private CloseableHttpClient proxyHttpClient;

    public ProxyHttpClient(CloseableHttpClient proxyHttpClient)
    {
        this.proxyHttpClient = proxyHttpClient;
    }

    @Override
    public Builder builder()
    {
        return new OkHttpClient.Factory().builder();
    }

    @Override
    public void cleanupIdleClients()
    {
        try
        {
            proxyHttpClient.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
