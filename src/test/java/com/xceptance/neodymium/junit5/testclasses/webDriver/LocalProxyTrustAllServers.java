package com.xceptance.neodymium.junit5.testclasses.webDriver;

import static com.codeborne.selenide.Condition.visible;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.junit5.tests.AbstractNeodymiumTest;
import com.xceptance.neodymium.junit5.NeodymiumTest;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.util.Neodymium;

public class LocalProxyTrustAllServers
{
    private static File tempConfigFile;

    @BeforeAll
    public static void beforeClass()
    {
        // set up a temporary neodymium.properties
        final String fileLocation = "config/temp-LocalProxyTrustAllServers-neodymium.properties";
        tempConfigFile = new File("./" + fileLocation);
        Map<String, String> properties = new HashMap<>();
        properties.put("neodymium.localproxy", "true");
        properties.put("neodymium.localproxy.certificate", "false");
        properties.put("neodymium.url.host", "authenticationtest.com");
        properties.put("neodymium.basicauth.username", "User");
        properties.put("neodymium.basicauth.password", "Pass");

        AbstractNeodymiumTest.writeMapToPropertiesFile(properties, tempConfigFile);
        ConfigFactory.setProperty(Neodymium.TEMPORARY_CONFIG_FILE_PROPERTY_NAME, "file:" + fileLocation);
    }

    @NeodymiumTest
    @Browser("Chrome_headless")
    public void test1()
    {
        Assertions.assertNotNull(Neodymium.getDriver());
        Assertions.assertNotNull(Neodymium.getLocalProxy());

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
