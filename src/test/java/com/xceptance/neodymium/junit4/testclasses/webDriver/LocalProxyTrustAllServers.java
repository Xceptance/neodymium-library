package com.xceptance.neodymium.junit4.testclasses.webDriver;

import static com.codeborne.selenide.Condition.visible;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.junit4.NeodymiumRunner;
import com.xceptance.neodymium.junit4.tests.NeodymiumTest;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
public class LocalProxyTrustAllServers
{
    private static File tempConfigFile;

    @BeforeClass
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

        NeodymiumTest.writeMapToPropertiesFile(properties, tempConfigFile);
        ConfigFactory.setProperty(Neodymium.TEMPORARY_CONFIG_FILE_PROPERTY_NAME, "file:" + fileLocation);
    }

    @Test
    @Browser("Chrome_headless")
    public void test1()
    {
        Assert.assertNotNull(Neodymium.getDriver());
        Assert.assertNotNull(Neodymium.getLocalProxy());

        Selenide.open("https://authenticationtest.com/HTTPAuth/");
        Assert.assertEquals("Authentication Test", Selenide.title());
        Selenide.$(".alert-success").shouldBe(visible);
    }

    @AfterClass
    public static void afterClass()
    {
        NeodymiumTest.deleteTempFile(tempConfigFile);
    }
}
