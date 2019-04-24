package com.xceptance.neodymium.module.statement.browser.multibrowser;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpClient.Builder;
import org.openqa.selenium.remote.internal.OkHttpClient;

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.TestEnvironment;

import okhttp3.Authenticator;
import okhttp3.ConnectionPool;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class NeodymiumProxyHttpClientFactory implements HttpClient.Factory
{
    private final ConnectionPool pool = new ConnectionPool();

    private TestEnvironment testEnvironment;

    public NeodymiumProxyHttpClientFactory(TestEnvironment testEnvironment) throws MalformedURLException
    {
        this.testEnvironment = testEnvironment;
    }

    @Override
    public Builder builder()
    {
        return new Builder()
        {
            @Override
            public HttpClient createClient(URL url)
            {
                // copied from org.openqa.selenium.remote.internal.OkHttpClient.Factory.Builder to prevent big
                // differences in implementation
                okhttp3.OkHttpClient.Builder client = new okhttp3.OkHttpClient.Builder();
                client.connectionPool(pool)
                      .followRedirects(true)
                      .followSslRedirects(true)
                      .readTimeout(readTimeout.toMillis(), MILLISECONDS)
                      .connectTimeout(connectionTimeout.toMillis(), MILLISECONDS);

                // Neodymium integration to use our test environment settings
                configureClientWithTestEnvironment(client, url);

                // copied from org.openqa.selenium.remote.internal.OkHttpClient.Factory.Builder to prevent big
                // differences in implementation
                client.addNetworkInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    return response.code() == 408
                                                  ? response.newBuilder().code(500).message("Server-Side Timeout").build()
                                                  : response;
                });

                return new OkHttpClient(client.build(), url);
            }

            private void configureClientWithTestEnvironment(okhttp3.OkHttpClient.Builder client, URL url)
            {
                if (testEnvironment.useProxy())
                {
                    String host = testEnvironment.getProxyHost();
                    Integer port = testEnvironment.getProxyPort();
                    client.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));

                    String proxyUsername = testEnvironment.getProxyUsername();
                    String proxyPassword = testEnvironment.getProxyPassword();
                    if (StringUtils.isNoneEmpty(proxyUsername, proxyPassword))
                    {
                        String credentials = Credentials.basic(proxyUsername, proxyPassword);
                        client.proxyAuthenticator(new NeodymiumAuthenticator("Proxy-Authorization", credentials));
                    }
                }

                String info = url.getUserInfo();
                String userName = "";
                String password = "";
                if (StringUtils.isNotBlank(info))
                {
                    String[] parts = info.split(":", 2);
                    userName = parts[0];
                    password = parts.length > 1 ? parts[1] : null;
                }
                else if (StringUtils.isNoneEmpty(testEnvironment.getUsername(), testEnvironment.getPassword()))
                {
                    userName = testEnvironment.getUsername();
                    password = testEnvironment.getPassword();
                }

                if (StringUtils.isNoneBlank(userName, password))
                {
                    String credentials = Credentials.basic(userName, password);
                    client.authenticator(new NeodymiumAuthenticator("Authorization", credentials));
                }
            }
        };
    }

    @Override
    public void cleanupIdleClients()
    {
        pool.evictAll();
    }

    private static class NeodymiumAuthenticator implements Authenticator
    {
        String headerName;

        String credentials;

        public NeodymiumAuthenticator(String headerName, String credentials)
        {
            this.headerName = headerName;
            this.credentials = credentials;
        }

        @Override
        public Request authenticate(Route route, Response response) throws IOException
        {
            if (response.request().header(headerName) != null)
            {
                return null; // Give up, we've already attempted to authenticate.
            }
            return response.request().newBuilder()
                           .header(headerName, credentials)
                           .build();
        }
    }

}
