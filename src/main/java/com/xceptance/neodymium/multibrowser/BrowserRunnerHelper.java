package com.xceptance.neodymium.multibrowser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.Dimension;
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
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.xceptance.neodymium.multibrowser.configuration.BrowserConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.DriverServerPath;
import com.xceptance.neodymium.multibrowser.configuration.MultibrowserConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.ProxyConfiguration;
import com.xceptance.neodymium.multibrowser.configuration.TestEnvironment;
import com.xceptance.neodymium.multibrowser.configuration.WebDriverProperties;

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
     * @return {@link URL} to Selenium grid augmented with credentials
     * @throws MalformedURLException
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
            && !StringUtils.isEmpty(proxyConfig.getUsername()) //
            && !StringUtils.isEmpty(proxyConfig.getPassword()))
        {
            final AuthScope proxyAuth = new AuthScope(proxyConfig.getHost(), Integer.valueOf(proxyConfig.getPort()));
            final Credentials proxyCredentials = new UsernamePasswordCredentials(proxyConfig.getUsername(), proxyConfig.getPassword());
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

        final Map<String, CommandInfo> additionalCommands = new HashMap<String, CommandInfo>();   // just a dummy

        // this command executor will do the credential magic for us. both proxy and target site credentials
        return new HttpCommandExecutor(additionalCommands, gridUrl, new ProxyHttpClient(httpClient));

    }

    /**
     * Sets the browser window size
     * <p>
     * Reads the default size from xlt properties and applies them to the browser window as long as its no device-emulation
     * test. In case of device-emulation the emulated device specifies the size of the browser window.
     *
     * @param config
     * @param driver
     */
    protected static void setBrowserWindowSize(final BrowserConfiguration config, final WebDriver driver)
    {
        WebDriverProperties webDriverProperties = MultibrowserConfiguration.getInstance().getWebDriverProperties();

        // get the configured window size and set it if defined
        final int windowWidth = webDriverProperties.getWindowWidth();
        final int windowHeight = webDriverProperties.getWindowHeight();

        final int configuredBrowserWidth = config.getBrowserWidth();
        final int configuredBrowserHeight = config.getBrowserHeight();

        Dimension browserSize = null;
        // first check if the configured browserprofile has a defined size, else use the xlt default browser size
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
     * @param proxyConfig
     * @return
     * @throws MalformedURLException
     */
    protected static WebDriver createWebdriver(final BrowserConfiguration config) throws MalformedURLException
    {
        final DesiredCapabilities capabilities = config.getCapabilities();

        final String testEnvironment = config.getTestEnvironment();
        ProxyConfiguration proxyConfig = MultibrowserConfiguration.getInstance().getProxyConfiguration();
        if (StringUtils.isEmpty(testEnvironment) || "local".equalsIgnoreCase(testEnvironment))
        {
            if (proxyConfig.useProxy())
            {
                final String proxyHost = proxyConfig.getHost() + ":" + proxyConfig.getPort();

                final Proxy webdriverProxy = new Proxy();
                webdriverProxy.setHttpProxy(proxyHost);
                webdriverProxy.setSslProxy(proxyHost);
                webdriverProxy.setFtpProxy(proxyHost);
                if (!StringUtils.isEmpty(proxyConfig.getUsername()) && !StringUtils.isEmpty(proxyConfig.getPassword()))
                {
                    webdriverProxy.setSocksUsername(proxyConfig.getUsername());
                    webdriverProxy.setSocksPassword(proxyConfig.getPassword());
                }

                capabilities.setCapability(CapabilityType.PROXY, webdriverProxy);
            }

            DriverServerPath driverServerPath = MultibrowserConfiguration.getInstance().getDriverServerPath();

            final String browserName = config.getCapabilities().getBrowserName();
            if (chromeBrowsers.contains(browserName))
            {
                // do we have a custom path?
                final String pathToBrowser = driverServerPath.getChromeBrowserPath();
                if (StringUtils.isNotBlank(pathToBrowser))
                {
                    final ChromeOptions options = new ChromeOptions();
                    options.setBinary(pathToBrowser);
                    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                }
                return new ChromeDriver(capabilities);
            }
            else if (firefoxBrowsers.contains(browserName))
            {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setBinary(createFirefoxBinary(driverServerPath.getFirefoxBrowserPath()));
                firefoxOptions.addCapabilities(capabilities);
                firefoxOptions.setProfile(null);

                return new FirefoxDriver(firefoxOptions);
            }
            else if (internetExplorerBrowsers.contains(browserName))
            {
                return new InternetExplorerDriver(capabilities);
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
}
