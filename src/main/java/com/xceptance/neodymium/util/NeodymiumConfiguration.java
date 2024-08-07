package com.xceptance.neodymium.util;

import static org.aeonbits.owner.Config.DisableableFeature.VARIABLE_EXPANSION;

import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Mutable;

import com.xceptance.neodymium.junit4.NeodymiumRunner.DescriptionMode;

@LoadPolicy(LoadType.MERGE)
@Sources(
{
  "${neodymium.temporaryConfigFile}", "file:config/dev-neodymium.properties", "file:config/credentials.properties", "file:config/neodymium.properties"
})
public interface NeodymiumConfiguration extends Mutable
{
    @Key("neodymium.debugUtils.highlight")
    @DefaultValue("false")
    public boolean debuggingHighlightSelectedElements();

    @Key("neodymium.debugUtils.highlight.duration")
    @DefaultValue("100")
    public long debuggingHighlightDuration();

    // standard timeout for selenide interaction
    @Key("neodymium.selenide.timeout")
    @DefaultValue("3000")
    public long selenideTimeout();

    @Key("neodymium.selenide.fastSetValue")
    @DefaultValue("false")
    public boolean selenideFastSetValue();

    @Key("neodymium.selenide.clickViaJs")
    @DefaultValue("false")
    public boolean selenideClickViaJs();

    @Key("neodymium.selenideAddons.staleElement.retry.count")
    @DefaultValue("3")
    public int staleElementRetryCount();

    @Key("neodymium.selenideAddons.staleElement.retry.timeout")
    @DefaultValue("500")
    public long staleElementRetryTimeout();

    @Key("neodymium.selenideAddons.optional.retry.pollingIntervall")
    @DefaultValue("3000")
    public long optionalElementRetryPollingIntervall();

    @Key("neodymium.selenideAddons.optional.retry.timeout")
    @DefaultValue("30000")
    public long optionalElementRetryTimeout();

    @Key("neodymium.javaScriptUtils.timeout")
    @DefaultValue("2000")
    public long javaScriptTimeout();

    @Key("neodymium.javaScriptUtils.pollingInterval")
    @DefaultValue("200")
    public long javaScriptPollingInterval();

    @Key("neodymium.javaScriptUtils.loading.jQueryIsRequired")
    @DefaultValue("true")
    public boolean javascriptLoadingJQueryIsRequired();

    @Key("neodymium.javaScriptUtils.loading.animationSelector")
    public String javascriptLoadingAnimationSelector();

    @Key("neodymium.url")
    public String url();

    @Key("neodymium.url.protocol")
    public String protocol();

    @Key("neodymium.url.host")
    public String host();

    @Key("neodymium.url.path")
    public String path();

    @Key("neodymium.url.path")
    @DisableFeature(
    {
      VARIABLE_EXPANSION
    })
    public String rawPath();

    @Key("neodymium.url.site")
    public String site();

    @Key("neodymium.basicauth.username")
    public String basicAuthUsername();

    @Key("neodymium.basicauth.password")
    public String basicAuthPassword();

    @Key("neodymium.locale")
    @DefaultValue("en_US")
    public String locale();

    @Key("neodymium.localization.file")
    @DefaultValue("config/localization.yaml")
    public String localizationFile();

    @Key("neodymium.allureAddons.screenshots.perstep.always")
    @DefaultValue("false")
    public boolean screenshotPerStep();

    @Key("neodymium.context.device.breakpoint.small")
    @DefaultValue("576")
    public int smallDeviceBreakpoint();

    @Key("neodymium.context.device.breakpoint.medium")
    @DefaultValue("768")
    public int mediumDeviceBreakpoint();

    @Key("neodymium.context.device.breakpoint.large")
    @DefaultValue("992")
    public int largeDeviceBreakpoint();

    @Key("neodymium.context.device.breakpoint.xlarge")
    @DefaultValue("1200")
    public int xlargeDeviceBreakpoint();

    @Key("neodymium.context.random.initialValue")
    public Long initialRandomValue();

    @Key("neodymium.dataUtils.email.domain")
    @DefaultValue("varmail.de")
    public String dataUtilsEmailDomain();

    @Key("neodymium.dataUtils.email.local.prefix")
    @DefaultValue("test")
    public String dataUtilsEmailLocalPrefix();

    @Key("neodymium.dataUtils.email.randomCharsAmount")
    @DefaultValue("12")
    public int dataUtilsEmailRandomCharsAmount();

    @Key("neodymium.dataUtils.password.uppercaseCharAmount")
    @DefaultValue("2")
    public int dataUtilsPasswordUppercaseCharAmount();

    @Key("neodymium.dataUtils.password.lowercaseCharAmount")
    @DefaultValue("5")
    public int dataUtilsPasswordLowercaseCharAmount();

    @Key("neodymium.dataUtils.password.digitAmount")
    @DefaultValue("2")
    public int dataUtilsPasswordDigitAmount();

    @Key("neodymium.dataUtils.password.specialCharAmount")
    @DefaultValue("2")
    public int dataUtilsPasswordSpecialCharAmount();

    @Key("neodymium.dataUtils.password.specialChars")
    @DefaultValue("+-#$%&.;,_")
    public String dataUtilsPasswordSpecialChars();

    @Key("neodymium.junit.viewmode")
    @DefaultValue("tree")
    public DescriptionMode junitViewMode();

    @Key("neodymium.proxy")
    @DefaultValue("false")
    public boolean useProxy();

    @Key("neodymium.proxy.host")
    public String getProxyHost();

    @Key("neodymium.proxy.port")
    public String getProxyPort();

    @Key("neodymium.proxy.bypassForHosts")
    public String getProxyBypass();

    @Key("neodymium.proxy.socket.version")
    public Integer getProxySocketVersion();

    @Key("neodymium.proxy.socket.userName")
    public String getProxySocketUsername();

    @Key("neodymium.proxy.socket.password")
    public String getProxySocketPassword();

    @Key("neodymium.selenideProxy")
    @DefaultValue("false")
    public boolean enableSelenideProxy();

    @Key("neodymium.localproxy")
    @DefaultValue("false")
    public boolean useLocalProxy();

    @Key("neodymium.localproxy.certificate")
    @DefaultValue("false")
    public boolean useLocalProxyWithSelfSignedCertificate();

    @Key("neodymium.localproxy.certificate.generate")
    @DefaultValue("true")
    public boolean localProxyGenerateSelfSignedCertificate();

    @Key("neodymium.localproxy.certificate.archiveFile")
    @DefaultValue("./config/Certificates.p12")
    public String localProxyCertificateArchiveFile();

    @Key("neodymium.localproxy.certificate.archivetype")
    @DefaultValue("PKCS12")
    public String localProxyCertificateArchiveType();

    @Key("neodymium.localproxy.certificate.name")
    public String localProxyCertificateName();

    @Key("neodymium.localproxy.certificate.password")
    public String localProxyCertificatePassword();

    @Key("neodymium.webDriver.window.width")
    @DefaultValue("-1")
    public Integer getWindowWidth();

    @Key("neodymium.webDriver.window.height")
    @DefaultValue("-1")
    public Integer getWindowHeight();

    @Key("neodymium.webDriver.reuseDriver")
    @DefaultValue("false")
    public boolean reuseWebDriver();

    @Key("neodymium.webDriver.maxReuse")
    @DefaultValue("-1")
    public int maxWebDriverReuse();

    @Key("neodymium.webDriver.keepBrowserOpen")
    @DefaultValue("false")
    public boolean keepBrowserOpen();

    @Key("neodymium.webDriver.keepBrowserOpenOnFailure")
    @DefaultValue("false")
    public boolean keepBrowserOpenOnFailure();

    @Key("neodymium.webDriver.chrome.pathToDriverServer")
    public String getChromeDriverPath();

    @Key("neodymium.webDriver.edge.pathToDriverServer")
    public String getEdgeDriverPath();

    @Key("neodymium.webDriver.firefox.pathToDriverServer")
    public String getFirefoxDriverPath();

    @Key("neodymium.webDriver.ie.pathToDriverServer")
    public String getIeDriverPath();

    @Key("neodymium.webDriver.chrome.pathToBrowser")
    public String getChromeBrowserPath();

    @Key("neodymium.webDriver.firefox.pathToBrowser")
    public String getFirefoxBrowserPath();

    @Key("neodymium.testNameFilter")
    public String getTestNameFilter();

    @Key("neodymium.workInProgress")
    @DefaultValue("false")
    public boolean workInProgress();

    @Key("neodymium.logNeoVersion")
    @DefaultValue("true")
    public boolean logNeoVersion();
}
