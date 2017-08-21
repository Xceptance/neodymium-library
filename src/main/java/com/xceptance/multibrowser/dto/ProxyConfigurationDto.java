package com.xceptance.multibrowser.dto;

public class ProxyConfigurationDto
{
    private String host;

    private String port;

    private String username;

    private String password;

    private String proxyByPass;

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getProxyByPass()
    {
        return proxyByPass;
    }

    public void setProxyByPass(String proxyByPass)
    {
        this.proxyByPass = proxyByPass;
    }
}
