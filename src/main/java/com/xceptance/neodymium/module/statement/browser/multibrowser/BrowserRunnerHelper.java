package com.xceptance.neodymium.module.statement.browser.multibrowser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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

import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.DriverServerPath;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.ProxyConfiguration;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.TestEnvironment;
import com.xceptance.neodymium.module.statement.browser.multibrowser.configuration.WebDriverProperties;

public final class BrowserRunnerHelper
{
    private static List<String> chromeBrowsers = new LinkedList<String>();

    private static List<String> firefoxBrowsers = new LinkedList<String>();

    private static List<String> internetExplorerBrowsers = new LinkedList<String>();

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
    }

    /**
     * Returns an {@link URL} to a Selenium grid (e.g. SauceLabs) that contains basic authentication for access
     * 
     * @param proxyConfig
     *            {@link ProxyConfiguration} that is used to connect to gridUrl
     * @param gridUrl
     *            The {@link URL} to the grid
     * @param gridUsername
     *            The username that should be used to accesd the grid
     * @param gridPassword
     *            The password that should be used to get access to the grid
     * @return {@link URL} to Selenium grid augmented with credentials
     * @throws MalformedURLException
     *             if the given gridUrl is invalid
     */
    protected static HttpCommandExecutor createGridExecutor(final ProxyConfiguration proxyConfig, final URL gridUrl,
                                                            final String gridUsername, final String gridPassword)
        throws MalformedURLException
    {
        // create a configuration for accessing target site via proxy (if a proxy is defined)
        // the proxy and the destination site will have different or no credentials for accessing them
        // so we need to create different authentication scopes and link them with the credentials
        final BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();

        // create credentials for proxy access
        if (proxyConfig.useProxy() //
            && !StringUtils.isEmpty(proxyConfig.getSocketUsername()) //
            && !StringUtils.isEmpty(proxyConfig.getSocketPassword()))
        {
            final AuthScope proxyAuth = new AuthScope(proxyConfig.getHost(), Integer.valueOf(proxyConfig.getPort()));
            final Credentials proxyCredentials = new UsernamePasswordCredentials(proxyConfig.getSocketUsername(), proxyConfig.getSocketPassword());
            basicCredentialsProvider.setCredentials(proxyAuth, proxyCredentials);
        }

        // create credentials for target website
        final AuthScope gridAuth = new AuthScope(gridUrl.getHost(), gridUrl.getPort());

        if (!StringUtils.isEmpty(gridUsername))
        {
            final Credentials gridCredentials = new UsernamePasswordCredentials(gridUsername, gridPassword);
            basicCredentialsProvider.setCredentials(gridAuth, gridCredentials);
        }

        // now create a http client, set the custom proxy and inject the credentials
        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultCredentialsProvider(basicCredentialsProvider);
        if (proxyConfig.useProxy())
            clientBuilder.setProxy(new HttpHost(proxyConfig.getHost(), Integer.valueOf(proxyConfig.getPort())));
        final CloseableHttpClient httpClient = clientBuilder.build();

        final Map<String, CommandInfo> additionalCommands = new HashMap<String, CommandInfo>(); // just a dummy

        // this command executor will do the credential magic for us. both proxy and target site credentials
        return new HttpCommandExecutor(additionalCommands, gridUrl, new ProxyHttpClient(httpClient));
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
        WebDriverProperties webDriverProperties = MultibrowserConfiguration.getInstance().getWebDriverProperties();

        // get the configured window size and set it if defined
        final int windowWidth = webDriverProperties.getWindowWidth();
        final int windowHeight = webDriverProperties.getWindowHeight();

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
            // on saucelabs in some cases like iphone emulation you cant resize the browser.
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
     *            {@link BrowserConfiguration} that describes the descired browser instance
     * @return {@link WebDriver} the instance of the browser described in {@link BrowserConfiguration}
     * @throws MalformedURLException
     *             if <a href="https://github.com/Xceptance/neodymium-library/wiki/Selenium-grid">Selenium grid</a> is
     *             used
     */
    @SuppressWarnings("unchecked")
    public static WebDriver createWebdriver(final BrowserConfiguration config) throws MalformedURLException
    {
        final MutableCapabilities capabilities = config.getCapabilities();

        final String testEnvironment = config.getTestEnvironment();
        ProxyConfiguration proxyConfig = MultibrowserConfiguration.getInstance().getProxyConfiguration();
        if (StringUtils.isEmpty(testEnvironment) || "local".equalsIgnoreCase(testEnvironment))
        {
            if (proxyConfig.useProxy())
            {
                capabilities.setCapability(CapabilityType.PROXY, createProxyCapabilities(proxyConfig));
            }

            DriverServerPath driverServerPath = MultibrowserConfiguration.getInstance().getDriverServerPath();

            final String browserName = config.getCapabilities().getBrowserName();
            if (chromeBrowsers.contains(browserName))
            {
                // do we have a custom path?
                final String pathToBrowser = driverServerPath.getChromeBrowserPath();
                final ChromeOptions options = new ChromeOptions();

                // This is a workaround for a changed Selenium behavior
                // Since device emulation is not part of the "standard" it now has to be considered as experimental
                // option.
                // The capability class already sorts the different configurations in different maps (one for
                // capabilities and one for
                // experimental capabilities). The experimental options are held internal within a map of the capability
                // map and
                // are accessible with key "goog:chromeOptions" (constant ChromeOptions.CAPABILITY). So all we have to
                // do is to copy the
                // keys and values of that special map and set it as experimental option inside ChromeOptions.
                Map<String, String> experimentalOptions = null;
                try
                {
                    experimentalOptions = (Map<String, String>) capabilities.getCapability(ChromeOptions.CAPABILITY);
                    if (experimentalOptions != null)
                    {
                        for (Entry<String, String> entry : experimentalOptions.entrySet())
                        {
                            options.setExperimentalOption(entry.getKey(), entry.getValue());
                        }
                    }
                }
                catch (Exception e)
                {
                    // unsure which case this can cover since only the type conversion can fail
                    // lets throw it as unchecked exception
                    // in case that makes no sense at all then just suppress it
                    throw new RuntimeException(e);
                }

                options.merge(capabilities);
                if (StringUtils.isNotBlank(pathToBrowser))
                {
                    options.setBinary(pathToBrowser);
                }
                options.setHeadless(config.isHeadless());

                if (config.getArguments() != null && config.getArguments().size() > 0)
                    options.addArguments(config.getArguments());

                return new ChromeDriver(options);
            }
            else if (firefoxBrowsers.contains(browserName))
            {
                FirefoxOptions options = new FirefoxOptions();
                options.setBinary(createFirefoxBinary(driverServerPath.getFirefoxBrowserPath()));
                options.merge(capabilities);
                options.setHeadless(config.isHeadless());
                if (config.getArguments() != null && config.getArguments().size() > 0)
                    options.addArguments(config.getArguments());

                return new FirefoxDriver(options);
            }
            else if (internetExplorerBrowsers.contains(browserName))
            {
                InternetExplorerOptions options = new InternetExplorerOptions();
                options.merge(capabilities);
                if (config.getArguments() != null && config.getArguments().size() > 0)
                {
                    for (String argument : config.getArguments())
                    {
                        options.addCommandSwitches(argument);
                    }
                }

                return new InternetExplorerDriver(options);
            }
        }
        else
        {
            TestEnvironment testEnvironmentProperties = MultibrowserConfiguration.getInstance().getTestEnvironment(testEnvironment);

            if (testEnvironmentProperties == null)
            {
                throw new IllegalArgumentException("No properties found for test environment: \"" + testEnvironment + "\"");
            }

            final String gridUsername = testEnvironmentProperties.getUsername();
            final String gridPassword = testEnvironmentProperties.getPassword();
            final String gridUrlString = testEnvironmentProperties.getUrl();
            final URL gridUrl = new URL(gridUrlString);

            // establish connection to target website
            return new RemoteWebDriver(createGridExecutor(proxyConfig, gridUrl, gridUsername, gridPassword), capabilities);
        }

        return null;
    }

    public static Proxy createProxyCapabilities(ProxyConfiguration proxyConfig)
    {
        final String proxyHost = proxyConfig.getHost() + ":" + proxyConfig.getPort();

        final Proxy webdriverProxy = new Proxy();
        webdriverProxy.setHttpProxy(proxyHost);
        webdriverProxy.setSslProxy(proxyHost);
        webdriverProxy.setFtpProxy(proxyHost);
        webdriverProxy.setSocksProxy(proxyHost);
        if (StringUtils.isNoneEmpty(proxyConfig.getSocketUsername(), proxyConfig.getSocketPassword()))
        {
            webdriverProxy.setSocksUsername(proxyConfig.getSocketUsername());
            webdriverProxy.setSocksPassword(proxyConfig.getSocketPassword());
        }
        if (proxyConfig.getSocketVersion() != null)
        {
            webdriverProxy.setSocksVersion(proxyConfig.getSocketVersion());
        }

        webdriverProxy.setNoProxy(proxyConfig.getBypass());
        return webdriverProxy;
    }
}
