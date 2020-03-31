package com.xceptance.neodymium.module.statement.browser.multibrowser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.client.ClientUtil;
import com.browserup.bup.mitm.KeyStoreFileCertificateSource;
import com.browserup.bup.mitm.manager.ImpersonatingMitmManager;
import com.browserup.bup.proxy.auth.AuthType;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.TestEnvironment;
import com.xceptance.neodymium.util.Neodymium;

public final class BrowserRunnerHelper
{
    private static List<String> chromeBrowsers = new LinkedList<String>();

    private static List<String> firefoxBrowsers = new LinkedList<String>();

    private static List<String> internetExplorerBrowsers = new LinkedList<String>();

    private static List<String> safariBrowsers = new LinkedList<String>();

    static
    {
        chromeBrowsers.add(BrowserType.ANDROID);
        chromeBrowsers.add(BrowserType.CHROME);

        firefoxBrowsers.add(BrowserType.FIREFOX);
        firefoxBrowsers.add(BrowserType.FIREFOX_CHROME);
        firefoxBrowsers.add(BrowserType.FIREFOX_PROXY);

        internetExplorerBrowsers.add(BrowserType.IE);
        internetExplorerBrowsers.add(BrowserType.IE_HTA);
        internetExplorerBrowsers.add(BrowserType.IEXPLORE);
        internetExplorerBrowsers.add(BrowserType.IEXPLORE_PROXY);

        safariBrowsers.add(BrowserType.SAFARI);
        safariBrowsers.add(BrowserType.SAFARI_PROXY);
    }

    /**
     * Returns an {@link HttpCommandExecutor} to a Selenium grid (e.g. SauceLabs) that contains basic authentication for
     * access
     * 
     * @param testEnvironment
     *            The {@link TestEnvironment} to the grid
     * @return {@link HttpCommandExecutor} to Selenium grid augmented with credentials
     * @throws MalformedURLException
     *             if the given gridUrl is invalid
     */
    protected static HttpCommandExecutor createGridExecutor(String testEnvironment) throws MalformedURLException

    {
        TestEnvironment testEnvironmentProperties = MultibrowserConfiguration.getInstance().getTestEnvironment(testEnvironment);

        if (testEnvironmentProperties == null)
        {
            throw new IllegalArgumentException("No properties found for test environment: \"" + testEnvironment + "\"");
        }

        final Map<String, CommandInfo> additionalCommands = new HashMap<String, CommandInfo>(); // just a dummy

        URL gridUrl = new URL(testEnvironmentProperties.getUrl());
        return new HttpCommandExecutor(additionalCommands, gridUrl, new NeodymiumProxyHttpClientFactory(testEnvironmentProperties));
    }

    /**
     * Sets the browser window size
     * <p>
     * Reads the default size from browser properties and applies them to the browser window as long as its no
     * device-emulation test. In case of device-emulation the emulated device specifies the size of the browser window.
     *
     * @param config
     *            {@link BrowserConfiguration} that describes the requested browser properties
     * @param driver
     *            {@link WebDriver} instance of the configured {@link BrowserConfiguration}
     */
    public static void setBrowserWindowSize(final BrowserConfiguration config, final WebDriver driver)
    {
        // get the configured window size and set it if defined
        final int windowWidth = Neodymium.configuration().getWindowWidth();
        final int windowHeight = Neodymium.configuration().getWindowHeight();

        final int configuredBrowserWidth = config.getBrowserWidth();
        final int configuredBrowserHeight = config.getBrowserHeight();

        Dimension browserSize = null;
        // first check if the configured browser profile has a defined size, else use the default browser size
        if (configuredBrowserWidth > 0 && configuredBrowserHeight > 0)
        {
            browserSize = new Dimension(configuredBrowserWidth, configuredBrowserHeight);
        }
        else if (windowWidth > 0 && windowHeight > 0)
        {
            browserSize = new Dimension(windowWidth, windowHeight);
        }

        try
        {
            if (browserSize != null)
                driver.manage().window().setSize(browserSize);
        }
        catch (final UnsupportedCommandException e)
        {
            // same as the exception handling below
            if (!e.getMessage().contains("not yet supported"))
                throw e;
        }
        catch (final WebDriverException e)
        {
            // on saucelabs in some cases like iphone emulation you can't resize the browser.
            // they throw an unchecked WebDriverException with the message "Not yet implemented"
            // if we catch an exception we check the message. if another message is set we throw the exception else
            // we suppress it
            if (!e.getMessage().contains("Not yet implemented"))
                throw e;
        }
    }

    /**
     * Creates a {@link FirefoxBinary} object and sets the path, but only if the path is not blank.
     * 
     * @param pathToBrowser
     *            the path to the browser binary
     * @return the Firefox binary
     */
    private static FirefoxBinary createFirefoxBinary(final String pathToBrowser)
    {
        if (StringUtils.isNotBlank(pathToBrowser))
        {
            return new FirefoxBinary(new File(pathToBrowser));
        }
        else
        {
            return new FirefoxBinary();
        }
    }

    /**
     * Instantiate the {@link WebDriver} according to the configuration read from {@link Browser} annotations.
     * 
     * @param config
     *            {@link BrowserConfiguration} that describes the desired browser instance
     * @return {@link WebDriver} the instance of the browser described in {@link BrowserConfiguration}
     * @throws MalformedURLException
     *             if <a href="https://github.com/Xceptance/neodymium-library/wiki/Selenium-grid">Selenium grid</a> is
     *             used
     */
    public static CachingContainer createWebdriver(final BrowserConfiguration config) throws MalformedURLException
    {
        final MutableCapabilities capabilities = config.getCapabilities();
        final CachingContainer container = new CachingContainer();

        if (Neodymium.configuration().useLocalProxy())
        {
            // instantiate the proxy
            BrowserUpProxy proxy = new BrowserUpProxyServer();

            if (Neodymium.configuration().useLocalWithSelfSignedCertificate())
            {
                // configure the MITM using the provided certificate
                String type = Neodymium.configuration().localProxyCertificateArchiveType();
                String file = Neodymium.configuration().localProxyCertificateArchiveFile();
                String cName = Neodymium.configuration().localProxyCertificateName();
                String cPassword = Neodymium.configuration().localProxyCertificatePassword();
                if (StringUtils.isAnyBlank(type, file, cName, cPassword))
                {
                    throw new RuntimeException("The local proxy certificate isn't fully configured. Please check: certificate archive type, certificate archive file, certificate name and certificate password.");
                }
                KeyStoreFileCertificateSource fileCertificateSource = new KeyStoreFileCertificateSource(type, new File(file), cName, cPassword);
                ImpersonatingMitmManager mitmManager = ImpersonatingMitmManager.builder().rootCertificateSource(fileCertificateSource).build();
                proxy.setMitmManager(mitmManager);
            }
            else
            {
                // disable proxy certificate verification
                proxy.setTrustAllServers(true);
            }

            // start the proxy
            proxy.start();

            // default basic authentication via the proxy
            String host = Neodymium.configuration().host();
            String bUsername = Neodymium.configuration().basicAuthUsername();
            String bPassword = Neodymium.configuration().basicAuthPassword();
            if (StringUtils.isNoneBlank(host, bUsername, bPassword))
            {
                proxy.autoAuthorization(host, bUsername, bPassword, AuthType.BASIC);
            }

            // set the Proxy for later usage
            container.setProxy(proxy);

            // configure the proxy via capabilities
            capabilities.setCapability(CapabilityType.PROXY, ClientUtil.createSeleniumProxy(proxy));
        }
        else if (Neodymium.configuration().useProxy())
        {
            capabilities.setCapability(CapabilityType.PROXY, createProxyCapabilities());
        }

        final String testEnvironment = config.getTestEnvironment();
        if (StringUtils.isEmpty(testEnvironment) || "local".equalsIgnoreCase(testEnvironment))
        {
            final String browserName = capabilities.getBrowserName();
            if (chromeBrowsers.contains(browserName))
            {
                final ChromeOptions options = (ChromeOptions) capabilities;

                // do we have a custom path?
                final String pathToBrowser = Neodymium.configuration().getChromeBrowserPath();
                if (StringUtils.isNotBlank(pathToBrowser))
                {
                    options.setBinary(pathToBrowser);
                }
                options.setHeadless(config.isHeadless());
                if (config.getArguments() != null && config.getArguments().size() > 0)
                {
                    options.addArguments(config.getArguments());
                }

                container.setWebDriver(new ChromeDriver(options));
            }
            else if (firefoxBrowsers.contains(browserName))
            {
                final FirefoxOptions options = new FirefoxOptions();
                options.setBinary(createFirefoxBinary(Neodymium.configuration().getFirefoxBrowserPath()));
                options.merge(capabilities);
                options.setHeadless(config.isHeadless());
                if (config.getArguments() != null && config.getArguments().size() > 0)
                {
                    options.addArguments(config.getArguments());
                }

                container.setWebDriver(new FirefoxDriver(options));
            }
            else if (internetExplorerBrowsers.contains(browserName))
            {
                final InternetExplorerOptions options = new InternetExplorerOptions();
                options.merge(capabilities);
                if (config.getArguments() != null && config.getArguments().size() > 0)
                {
                    for (String argument : config.getArguments())
                    {
                        options.addCommandSwitches(argument);
                    }
                }

                container.setWebDriver(new InternetExplorerDriver(options));
            }
            else if (safariBrowsers.contains(browserName))
            {
                final SafariOptions options = (SafariOptions) capabilities;
                container.setWebDriver(new SafariDriver(options));
            }
            else
            {
                container.setWebDriver(new RemoteWebDriver(capabilities));
            }
        }
        else
        {
            // establish connection to target website
            container.setWebDriver(new RemoteWebDriver(createGridExecutor(testEnvironment), capabilities));
        }

        return container;
    }

    public static Proxy createProxyCapabilities()
    {
        final String proxyHost = Neodymium.configuration().getProxyHost() + ":" + Neodymium.configuration().getProxyPort();

        final Proxy webdriverProxy = new Proxy();
        webdriverProxy.setHttpProxy(proxyHost);
        webdriverProxy.setSslProxy(proxyHost);
        webdriverProxy.setFtpProxy(proxyHost);
        if (!StringUtils.isAllEmpty(Neodymium.configuration().getProxySocketUsername(), Neodymium.configuration().getProxySocketPassword())
            || Neodymium.configuration().getProxySocketVersion() != null)
        {
            webdriverProxy.setSocksProxy(proxyHost);
            if (StringUtils.isNoneEmpty(Neodymium.configuration().getProxySocketUsername(),
                                        Neodymium.configuration().getProxySocketPassword()))
            {
                webdriverProxy.setSocksUsername(Neodymium.configuration().getProxySocketUsername());
                webdriverProxy.setSocksPassword(Neodymium.configuration().getProxySocketPassword());
            }
            if (Neodymium.configuration().getProxySocketVersion() != null)
            {
                webdriverProxy.setSocksVersion(4);
            }
        }

        webdriverProxy.setNoProxy(Neodymium.configuration().getProxyBypass());
        return webdriverProxy;
    }
}
