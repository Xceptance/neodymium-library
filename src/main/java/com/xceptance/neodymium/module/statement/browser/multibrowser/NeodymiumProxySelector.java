package com.xceptance.neodymium.module.statement.browser.multibrowser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class NeodymiumProxySelector extends ProxySelector
{
    // A reference on the previous default
    private final ProxySelector defsel;

    // The proxy address
    private final SocketAddress sa;

    // The proxy
    private final Proxy proxy;

    // The hosts that pass by the proxy
    private final Set<BypassHost> bypasses;

    /**
     * 
     */
    class BypassHost
    {
        /** has left hand wildcard */
        private final boolean wcLeft;

        /** has right hand wildcard */
        private final boolean wcRight;

        /** host part without wildcard */
        private final String bypassHost;

        /**
         * @param bypass
         *            bypass pattern. Might contain a wildcard (*) at start/end
         */
        public BypassHost(final String bypass)
        {
            if (StringUtils.isBlank(bypass) || bypass.matches("\\*+"))
            {
                throw new IllegalArgumentException("Bypass host must not be NULL or empty or consist of wildcard only.");
            }

            // Get the bypass host pattern, detect wildcards and extract plain host part.
            String tmp = bypass.trim();

            // Has pattern a left hand wildcard?
            wcLeft = tmp.startsWith("*");
            if (wcLeft)
            {
                tmp = tmp.substring(1);
            }

            // Has pattern a right hand wildcard?
            wcRight = tmp.endsWith("*");
            if (wcRight)
            {
                tmp = tmp.substring(0, tmp.length() - 1);
            }

            bypassHost = tmp;

            // Check that there has anything left for identifying the host.
            if (StringUtils.isBlank(bypassHost))
            {
                throw new IllegalArgumentException("Bypass host must not be NULL or empty or consist of wildcard only.");
            }
        }

        /**
         * Does the given host string match the bypass pattern?
         * 
         * @param host
         *            host name to match against the bypass pattern
         * @return <code>true</code> if the given host string matches the bypass pattern; <code>false</code> otherwise
         */
        public boolean matches(final String host)
        {
            // bypass has wildcard on both ends
            if (wcLeft && wcRight)
            {
                return host.contains(bypassHost);
            }
            // bypass starts with wildcard
            else if (wcLeft)
            {
                return host.endsWith(bypassHost);
            }
            // bypass ends with wildcard
            else if (wcRight)
            {
                return host.startsWith(bypassHost);
            }
            // bypass matches completely
            else
            {
                return host.equals(bypassHost);
            }
        }
    }

    public NeodymiumProxySelector(final ProxySelector def, String host, int port)
    {
        // Save the previous default
        defsel = def;

        // Get the proxy host.
        if (StringUtils.isBlank(host))
        {
            throw new IllegalArgumentException("Proxy host must not be NULL or empty.");
        }

        // Populate the set HTTPS proxy
        sa = new InetSocketAddress(host, port);
        proxy = new Proxy(Proxy.Type.HTTP, sa);
        bypasses = new HashSet<BypassHost>();

        // Get the bypass hosts. If not set ... do nothing
        final String bypassProp = System.getProperty("https.nonProxyHosts");
        if (!StringUtils.isBlank(bypassProp))
        {
            for (final String bypass : bypassProp.split("\\|"))
            {
                bypasses.add(new BypassHost(bypass));
            }
        }
    }

    // This is the method that the handlers will call.
    /**
     * {@inheritDoc}
     */
    @Override
    public java.util.List<Proxy> select(final URI uri)
    {
        // Let's stick to the specs.
        if (uri == null)
        {
            throw new IllegalArgumentException("URI can't be null.");
        }

        // If it's a HTTPS URL, then we use our own list.
        final String protocol = uri.getScheme();
        if ("https".equalsIgnoreCase(protocol))
        {
            // Check if it is a bypass host.
            final String host = uri.getHost();
            for (final BypassHost bypass : bypasses)
            {
                if (bypass.matches(host))
                {
                    final ArrayList<Proxy> l = new ArrayList<Proxy>();
                    l.add(Proxy.NO_PROXY);
                    return l;
                }
            }

            // No bypass, so we return our proxy.
            final ArrayList<Proxy> l = new ArrayList<Proxy>();
            l.add(proxy);
            return l;
        }
        // Not HTTPS (could be HTTP or SOCKS or FTP) defer to the default selector.
        else if (defsel != null)
        {
            return defsel.select(uri);
        }
        else
        {
            final ArrayList<Proxy> l = new ArrayList<Proxy>();
            l.add(Proxy.NO_PROXY);
            return l;
        }
    }

    // Method called by the handlers when it failed to connect to one of the proxies returned by select().
    /**
     * {@inheritDoc}
     */
    @Override
    public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe)
    {
        // Let's stick to the specs again.
        if (uri == null || sa == null || ioe == null)
        {
            throw new IllegalArgumentException("Arguments can't be null.");
        }

        // Let's lookup for the proxy
        if (this.sa.equals(sa))
        {
            // It's one of ours.
        }
        else
        {
            // Not one of ours, let's delegate to the default.
            if (defsel != null)
            {
                defsel.connectFailed(uri, sa, ioe);
            }
        }
    }
}
