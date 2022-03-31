package org.openqa.selenium.remote.http.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Realm;
import org.asynchttpclient.Realm.AuthScheme;
import org.asynchttpclient.config.AsyncHttpClientConfigDefaults;
import org.asynchttpclient.proxy.ProxyServer;
import org.asynchttpclient.proxy.ProxyServerSelector;
import org.asynchttpclient.proxy.ProxyType;
import org.asynchttpclient.uri.Uri;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.internal.Require;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.http.Filter;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpClientName;
import org.openqa.selenium.remote.http.HttpHandler;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.WebSocket;

import com.google.auto.service.AutoService;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.concurrent.DefaultThreadFactory;

public class NeoClient implements HttpClient
{
    private static final Timer TIMER;

    private final AsyncHttpClient client;

    static
    {
        ThreadFactory threadFactory = new DefaultThreadFactory("netty-client-timer", true);
        TIMER = new HashedWheelTimer(threadFactory, AsyncHttpClientConfigDefaults.defaultHashedWheelTimerTickDuration(), TimeUnit.MILLISECONDS, AsyncHttpClientConfigDefaults.defaultHashedWheelTimerSize());
    }

    private final ClientConfig config;

    private final HttpHandler handler;

    private final BiFunction<HttpRequest, WebSocket.Listener, WebSocket> toWebSocket;

    private NeoClient(ClientConfig config)
    {
        this.config = Require.nonNull("HTTP client config", config);
        this.client = createHttpClient(config);
        this.handler = new NettyHttpHandler(config, client).with(config.filter());
        this.toWebSocket = NettyWebSocket.create(config, client);
    }

    /**
     * Converts a long to an int, clamping the maximum and minimum values to fit in an integer without overflowing.
     */
    static int toClampedInt(long value)
    {
        return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, value));
    }

    private static AsyncHttpClient createHttpClient(ClientConfig config)
    {

        DefaultAsyncHttpClientConfig.Builder builder = new DefaultAsyncHttpClientConfig.Builder()
                                                                                                 .setThreadFactory(new DefaultThreadFactory("AsyncHttpClient", true))
                                                                                                 .setUseInsecureTrustManager(true)
                                                                                                 .setAggregateWebSocketFrameFragments(true)
                                                                                                 .setWebSocketMaxBufferSize(Integer.MAX_VALUE)
                                                                                                 .setWebSocketMaxFrameSize(Integer.MAX_VALUE)
                                                                                                 .setNettyTimer(TIMER)
                                                                                                 .setRequestTimeout(toClampedInt(config.readTimeout()
                                                                                                                                       .toMillis()))
                                                                                                 .setConnectTimeout(toClampedInt(config.connectionTimeout()
                                                                                                                                       .toMillis()))
                                                                                                 .setReadTimeout(toClampedInt(config.readTimeout().toMillis()));
        if (config.proxy() != null)
        {
            InetSocketAddress proxy = (InetSocketAddress) config.proxy().address();
            ProxyServer.Builder ps = new ProxyServer.Builder(proxy.getHostName(), proxy.getPort()).setProxyType(ProxyType.HTTP);

            if (config.credentials() != null)
            {
                UsernameAndPassword credential = (UsernameAndPassword) config.credentials();

                Realm.Builder rb = new Realm.Builder(credential.username(), credential.password()).setScheme(AuthScheme.BASIC)
                                                                                                  .setUsePreemptiveAuth(true);
                ps.setRealm(rb);
            }

            builder.setProxyServerSelector(new ProxyServerSelector()
            {

                @Override
                public ProxyServer select(Uri uri)
                {
                    return ps.build();
                }
            });
        }

        return Dsl.asyncHttpClient(builder);
    }

    @Override
    public HttpResponse execute(HttpRequest request)
    {
        return handler.execute(request);
    }

    @Override
    public WebSocket openSocket(HttpRequest request, WebSocket.Listener listener)
    {
        Require.nonNull("Request to send", request);
        Require.nonNull("WebSocket listener", listener);

        return toWebSocket.apply(request, listener);
    }

    @Override
    public HttpClient with(Filter filter)
    {
        Require.nonNull("Filter", filter);
        return new NeoClient(config.withFilter(filter));
    }

    @Override
    public void close()
    {
        // no-op -- closing the client when the JVM shuts down
    }

    @AutoService(HttpClient.Factory.class)
    @HttpClientName("neo")
    public static class Factory implements HttpClient.Factory
    {

        @Override
        public HttpClient createClient(ClientConfig config)
        {
            Require.nonNull("Client config", config);
            return new NeoClient(config);
        }
    }
}
