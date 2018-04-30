package com.xceptance.neodymium.module.statement.browser.multibrowser;

import java.net.URL;

import org.apache.http.client.HttpClient;
import org.openqa.selenium.remote.internal.ApacheHttpClient;

public class ProxyHttpClient implements org.openqa.selenium.remote.http.HttpClient.Factory
{
    private final HttpClient httpClient;

    public ProxyHttpClient(HttpClient httpClient)
    {
        this.httpClient = httpClient;
    }

    @Override
    public org.openqa.selenium.remote.http.HttpClient createClient(URL url)
    {
        return new ApacheHttpClient(httpClient, url);
    }
}
