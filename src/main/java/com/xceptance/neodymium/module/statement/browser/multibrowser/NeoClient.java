package com.xceptance.neodymium.module.statement.browser.multibrowser;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLEncoder;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.openqa.selenium.remote.http.AddSeleniumUserAgent;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.http.Contents;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.WebSocket;
import org.openqa.selenium.remote.http.WebSocket.Listener;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NeoClient implements HttpClient
{
    private OkHttpClient client;

    private ClientConfig config;

    public NeoClient(OkHttpClient client, ClientConfig clientConfig)
    {
        this.client = client;
        this.config = clientConfig;
    }

    @Override
    public HttpResponse execute(HttpRequest req) throws UncheckedIOException
    {
        String rawUrl = req.getUri().toString();
        // Add query string if necessary
        String queryString = StreamSupport.stream(req.getQueryParameterNames().spliterator(), false)
                                          .map(
                                               name -> {
                                                   return StreamSupport.stream(req.getQueryParameters(name).spliterator(), false)
                                                                       .map(
                                                                            value -> String.format(
                                                                                                   "%s=%s",
                                                                                                   URLEncoder.encode(name, UTF_8),
                                                                                                   URLEncoder.encode(value, UTF_8)))
                                                                       .collect(Collectors.joining("&"));
                                               })
                                          .collect(Collectors.joining("&"));

        if (!queryString.isEmpty())
        {
            rawUrl = rawUrl + "?" + queryString;
        }

        Builder builder = new Request.Builder().url(config.baseUrl() + rawUrl);

        req.forEachHeader(
                          (name, value) -> {
                              // This prevents the IllegalArgumentException that states 'restricted header name: ...'
                              builder.header(name, value);
                          });

        if (req.getHeader("User-Agent") == null)
        {
            builder.header("User-Agent", AddSeleniumUserAgent.USER_AGENT);
        }
        switch (req.getMethod())
        {
            case DELETE:
                builder.delete();
                break;

            case GET:
                builder.get();
                break;

            case POST:
                builder.post(RequestBody.create(Contents.bytes(req.getContent())));
                break;

            case PUT:
                builder.put(RequestBody.create(Contents.bytes(req.getContent())));
                break;

            default:
                throw new IllegalArgumentException(String.format("Unsupported request method %s: %s", req.getMethod(), req));
        }

        try
        {
            Response response = client.newCall(builder.build()).execute();

            HttpResponse res = new HttpResponse();
            res.setStatus(response.code());
            response
                    .headers()
                    .forEach(
                             (pair) -> res.addHeader(pair.component1(), pair.component2()));
            byte[] responseBody = response.body().bytes();
            if (responseBody != null)
            {
                res.setContent(() -> new ByteArrayInputStream(responseBody));
            }

            return res;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

    @Override
    public WebSocket openSocket(HttpRequest request, Listener listener)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
