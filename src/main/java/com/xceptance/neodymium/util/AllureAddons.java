package com.xceptance.neodymium.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableMap;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;

/**
 * Convenience methods for step definitions
 *
 * @author rschwietzke
 */
public class AllureAddons
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureAddons.class);

    private static boolean neoVersionLogged = false;

    private static boolean customDataAdded = false;

    /**
     * Define a step without return value. This can be used to transport data (information) from test into the report.
     *
     * @param info
     *            the info of the information (maybe the information itself if short enough), used in the description of
     *            this step
     * @param content
     *            further information that need to be passed to the report
     */
    @Step("INFO: {info}")
    public static void addToReport(String info, Object content)
    {
    }

    /**
     * Define a step without return value. This is good for complete and encapsulated test steps.
     *
     * @param description
     *            the proper description of this step
     * @param actions
     *            what to do as Lambda
     */
    @Step("{description}")
    public static void step(final String description, final Runnable actions)
    {
        try
        {
            actions.run();
        }
        finally
        {
            if (Neodymium.configuration().screenshotPerStep())
            {
                attachPNG(UUID.randomUUID().toString() + ".png");
            }
        }
    }

    /**
     * Define a step with a return value. This is good for complete and encapsulated test steps.
     *
     * @param <T>
     *            generic return type
     * @param description
     *            the proper description of this step
     * @param actions
     *            what to do as Lambda
     * @return T
     */
    @Step("{description}")
    public static <T> T step(final String description, final Supplier<T> actions)
    {
        try
        {
            return actions.get();
        }
        finally
        {
            if (Neodymium.configuration().screenshotPerStep())
            {
                attachPNG(UUID.randomUUID().toString() + ".png");
            }
        }
    }

    /**
     * Takes screenshot and converts it to byte stream
     * 
     * @param filename
     * @return
     */
    @Attachment(type = "image/png", value = "{filename}", fileExtension = ".png")
    public static byte[] attachPNG(final String filename)
    {
        return ((TakesScreenshot) Neodymium.getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Adds information about environment to the report
     * 
     * @param environmentValuesSet
     *            map with environment values
     */
    public static void addEnvironmentInformation(ImmutableMap<String, String> environmentValuesSet)
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // check if allure-results folder exists
            if (!getAllureResultsFolder().exists())
            {
                // create it if not
                getAllureResultsFolder().mkdirs();
            }
            Document doc;

            // if environment.xml file exists, there probably already was an entry in it
            // in this case we need to append our values to it
            if (envFileExists())
            {
                doc = docBuilder.parse(getEnvFile());
                environmentValuesSet.forEach((k, v) -> {
                    Node environment = doc.getDocumentElement();
                    Element parameter = doc.createElement("parameter");
                    Element key = doc.createElement("key");
                    Element value = doc.createElement("value");
                    key.appendChild(doc.createTextNode(k));
                    value.appendChild(doc.createTextNode(v));
                    parameter.appendChild(key);
                    parameter.appendChild(value);
                    environment.appendChild(parameter);
                });
            }
            else
            {
                doc = docBuilder.newDocument();
                Element environment = doc.createElement("environment");
                doc.appendChild(environment);
                environmentValuesSet.forEach((k, v) -> {
                    Element parameter = doc.createElement("parameter");
                    Element key = doc.createElement("key");
                    Element value = doc.createElement("value");
                    key.appendChild(doc.createTextNode(k));
                    value.appendChild(doc.createTextNode(v));
                    parameter.appendChild(key);
                    parameter.appendChild(value);
                    environment.appendChild(parameter);
                });
            }
            DOMSource source = new DOMSource(doc);
            FileOutputStream output = new FileOutputStream(getEnvFile());
            FileChannel channel = output.getChannel();
            FileLock lock = channel.tryLock();
            if (lock != null)
            {
                StreamResult result = new StreamResult(output);
                transformer.transform(source, result);
            }
            lock.release();
        }
        catch (ParserConfigurationException | TransformerException | SAXException | IOException e)
        {
            LOGGER.warn("Failed to add information about environment to Allure report");
        }
    }

    /**
     * Check if allure-reprot environment.xml file exists
     * 
     * @return false - if doesn't exist <br>
     *         true - if exists
     */
    public static boolean envFileExists()
    {
        return getEnvFile().exists();
    }

    private static File getEnvFile()
    {
        File allureResultsDir = getAllureResultsFolder();
        File envFile = new File(allureResultsDir.getAbsoluteFile() + "/environment.xml");
        return envFile;
    }

    /**
     * Get path to allure-results folder (default or configured in pom)
     * 
     * @return File with path to the allure-results folder
     */
    public static File getAllureResultsFolder()
    {
        return new File(System.getProperty("allure.results.directory", System.getProperty("user.dir")
                                                                       + "/target/allure-results"));
    }

    public static void initializeEnvironmentInformation()
    {
        Map<String, String> environmentDataMap = new HashMap<String, String>();

        if (!neoVersionLogged && Neodymium.configuration().logNeoVersion())
        {
            if (!AllureAddons.envFileExists())
            {
                LOGGER.info("This test uses Neodymium Library (version: " + Neodymium.getNeodymiumVersion()
                            + "), MIT License, more details on https://github.com/Xceptance/neodymium-library");
                neoVersionLogged = true;
                environmentDataMap.putIfAbsent("Testing Framework", "Neodymium " + Neodymium.getNeodymiumVersion());
            }
        }
        if (!customDataAdded && Neodymium.configuration().enableCustomEnvironmentData())
        {
            LOGGER.info("Custom Environment Data was added.");
            customDataAdded = true;
            String customDataIdentifier = "neodymium.report.environment.custom";
            environmentDataMap = PropertiesUtil.addMissingPropertiesFromFile("./config/dev-neodymium.properties", customDataIdentifier, environmentDataMap);

            Map<String, String> systemEnvMap = new HashMap<String, String>();
            for (Map.Entry<String, String> entry : System.getenv().entrySet())
            {
                String key = (String) entry.getKey();
                if (key.contains(customDataIdentifier))
                {
                    String cleanedKey = key.replace(customDataIdentifier, "");
                    cleanedKey = cleanedKey.replaceAll("\\.", "");
                    systemEnvMap.put(cleanedKey, (String) entry.getValue());
                }
            }
            environmentDataMap = PropertiesUtil.mapPutAllIfAbsent(environmentDataMap, systemEnvMap);
            environmentDataMap = PropertiesUtil.mapPutAllIfAbsent(environmentDataMap,
                                                                  PropertiesUtil.getDataMapForIdentifier(customDataIdentifier,
                                                                                                         System.getProperties()));
            environmentDataMap = PropertiesUtil.addMissingPropertiesFromFile("./config/credentials.properties", customDataIdentifier, environmentDataMap);
            environmentDataMap = PropertiesUtil.addMissingPropertiesFromFile("./config/neodymium.properties", customDataIdentifier, environmentDataMap);
        }

        if (!environmentDataMap.isEmpty())
        {
            AllureAddons.addEnvironmentInformation(ImmutableMap.<String, String> builder().putAll(environmentDataMap).build());
        }
    }
}
