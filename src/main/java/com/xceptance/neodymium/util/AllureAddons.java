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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.codeborne.selenide.Selenide;
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

    private static final int MAX_RETRY_COUNT = 10;

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
    public static synchronized void addEnvironmentInformation(ImmutableMap<String, String> environmentValuesSet, boolean shouldUpdate)
    {
        try
        {
            FileOutputStream output = new FileOutputStream(getEnvFile());
            FileChannel channel = output.getChannel();
            FileLock lock = null;
            int retries = 0;
            do
            {
                if (retries > 0)
                {
                    Selenide.sleep(100);
                }
                lock = channel.tryLock();
                retries++;
            }
            while (retries < MAX_RETRY_COUNT && lock == null);
            if (lock != null)
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
                boolean isFileAccessNeeded = false;

                // if environment.xml file exists, there probably already was an entry in it
                // in this case we need to append our values to it
                if (getEnvFile().length() != 0)
                {
                    doc = docBuilder.parse(getEnvFile());
                    for (Map.Entry<String, String> entry : environmentValuesSet.entrySet())
                    {
                        Node environment = doc.getDocumentElement();
                        NodeList childNodes = environment.getChildNodes();
                        boolean isSameNode = false;
                        int keyToUpdate = 0;
                        for (int i = 0; i < childNodes.getLength(); i++)
                        {
                            Node child = childNodes.item(i);
                            NodeList subNodes = child.getChildNodes();
                            String key = "";
                            String value = "";
                            for (int j = 0; j < subNodes.getLength(); j++)
                            {
                                Node subNode = subNodes.item(j);
                                if ("key".equals(subNode.getNodeName()))
                                {
                                    key = subNode.getTextContent();
                                }
                                else if ("value".equals(subNode.getNodeName()))
                                {
                                    value = subNode.getTextContent();
                                }
                            }
                            if (key.equals(entry.getKey()) && value.equals(entry.getValue()))
                            {
                                isSameNode = true;
                                break;
                            }
                            else if (shouldUpdate && key.equals(entry.getKey()))
                            {
                                keyToUpdate = i;
                                break;
                            }
                        }
                        if (!isSameNode)
                        {
                            Element parameter = doc.createElement("parameter");
                            Element key = doc.createElement("key");
                            Element value = doc.createElement("value");
                            key.appendChild(doc.createTextNode(entry.getKey()));
                            value.appendChild(doc.createTextNode(entry.getValue()));
                            parameter.appendChild(key);
                            parameter.appendChild(value);
                            environment.appendChild(parameter);
                            isFileAccessNeeded = true;
                        }
                        else if (shouldUpdate && keyToUpdate != 0)
                        {
                            Document updatedDoc = docBuilder.newDocument();
                            Element updatedEnvironment = updatedDoc.createElement("environment");
                            for (int i = 0; i < childNodes.getLength(); i++)
                            {
                                if (i != keyToUpdate)
                                {
                                    updatedEnvironment.appendChild(childNodes.item(i));
                                }
                                else
                                {
                                    Element parameter = doc.createElement("parameter");
                                    Element key = doc.createElement("key");
                                    Element value = doc.createElement("value");
                                    key.appendChild(doc.createTextNode(entry.getKey()));
                                    value.appendChild(doc.createTextNode(entry.getValue()));
                                    parameter.appendChild(key);
                                    parameter.appendChild(value);
                                    updatedEnvironment.appendChild(parameter);
                                }
                            }
                            doc = updatedDoc;
                            isFileAccessNeeded = true;
                        }
                    }
                }
                else
                {
                    isFileAccessNeeded = true;
                    doc = docBuilder.newDocument();
                    Element environment = doc.createElement("environment");
                    doc.appendChild(environment);
                    for (Map.Entry<String, String> entry : environmentValuesSet.entrySet())
                    {
                        Element parameter = doc.createElement("parameter");
                        Element key = doc.createElement("key");
                        Element value = doc.createElement("value");
                        key.appendChild(doc.createTextNode(entry.getKey()));
                        value.appendChild(doc.createTextNode(entry.getValue()));
                        parameter.appendChild(key);
                        parameter.appendChild(value);
                        environment.appendChild(parameter);
                    }
                }
                if (isFileAccessNeeded)
                {
                    DOMSource source = new DOMSource(doc);

                    StreamResult result = new StreamResult(output);
                    transformer.transform(source, result);
                }
                lock.release();
            }
            else
            {
                LOGGER.warn("Could not acquire Filelock in time. Failed to add information about enviroment to Allure report");
            }
            channel.close();
            output.close();
        }
        catch (ParserConfigurationException | TransformerException | SAXException | IOException e)
        {
            LOGGER.warn("Failed to add information about environment to Allure report", e);
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
        File envFile = new File(allureResultsDir.getAbsoluteFile() + File.separator + "environment.xml");
        if (!envFile.exists())
        {
            try
            {
                envFile.getParentFile().mkdirs();
                envFile.createNewFile();
            }
            catch (IOException e)
            {
                LOGGER.error(e.getMessage(), e);
            }
        }
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
                                                                       + File.separator + "target" + File.separator + "allure-results"));
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
            environmentDataMap = PropertiesUtil.addMissingPropertiesFromFile("." + File.separator + "config" + File.separator + "dev-neodymium.properties",
                                                                             customDataIdentifier, environmentDataMap);

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
            environmentDataMap = PropertiesUtil.addMissingPropertiesFromFile("." + File.separator + "config" + File.separator + "credentials.properties",
                                                                             customDataIdentifier, environmentDataMap);
            environmentDataMap = PropertiesUtil.addMissingPropertiesFromFile("." + File.separator + "config" + File.separator + "neodymium.properties",
                                                                             customDataIdentifier, environmentDataMap);
        }

        if (!environmentDataMap.isEmpty())
        {
            AllureAddons.addEnvironmentInformation(ImmutableMap.<String, String> builder().putAll(environmentDataMap).build(), false);
        }
    }
}
