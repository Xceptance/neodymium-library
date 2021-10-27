package com.xceptance.neodymium.junit5.testclasses.webDriver;

import static com.codeborne.selenide.Condition.visible;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import com.browserup.bup.proxy.auth.AuthType;
import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.junit5.tests.AbstractNeodymiumTest;
import com.xceptance.neodymium.junit5.NeodymiumTest;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.util.Neodymium;

public class LocalProxyUsingProvidedCertificates
{
    private static File tempConfigFile;

    @BeforeAll
    public static void beforeClass()
    {
        // set up a temporary neodymium.properties
        final String fileLocation = "config/temp-LocalProxyUsingProvidedCertificates-neodymium.properties";
        tempConfigFile = new File("./" + fileLocation);
        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.localproxy", "true");
        properties.put("neodymium.localproxy.certificate", "true");
        properties.put("neodymium.localproxy.certificate.generate ", "false");
        properties.put("neodymium.localproxy.certificate.archiveFile ", "./config/LocalProxyTestCertificate.p12");
        properties.put("neodymium.localproxy.certificate.archivetype ", "PKCS12");
        properties.put("neodymium.localproxy.certificate.name ", "MitmProxy");
        properties.put("neodymium.localproxy.certificate.password ", "xceptance");
        AbstractNeodymiumTest.writeMapToPropertiesFile(properties, tempConfigFile);
        ConfigFactory.setProperty(Neodymium.TEMPORARY_CONFIG_FILE_PROPERTY_NAME, "file:" + fileLocation);
    }

    @NeodymiumTest()
    @Browser("Chrome_headless")
    public void test1()
    {
        Assertions.assertNotNull(Neodymium.getDriver());
        Assertions.assertNotNull(Neodymium.getLocalProxy());
        Neodymium.getLocalProxy().autoAuthorization("authenticationtest.com", "User", "Pass", AuthType.BASIC);

        Selenide.open("https://authenticationtest.com/HTTPAuth/");
        Assertions.assertEquals("Authentication Test", Selenide.title());
        Selenide.$(".alert-success").shouldBe(visible);
    }

    @AfterAll
    public static void afterClass()
    {
        AbstractNeodymiumTest.deleteTempFile(tempConfigFile);
    }
}
