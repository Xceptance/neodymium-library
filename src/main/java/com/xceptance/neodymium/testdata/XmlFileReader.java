package com.xceptance.neodymium.testdata;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlFileReader
{
    public static List<Object[]> readFile(String filename)
    {
        List<Object[]> testData = new LinkedList<>();
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.parse(new File(filename));

            doc.getDocumentElement().normalize();

            Element rootElement = doc.getDocumentElement();
            if (rootElement.getNodeName().equals("datafile"))
            {
                NodeList dataSets = rootElement.getElementsByTagName("dataset");
                for (int i = 0; i < dataSets.getLength(); i++)
                {
                    Node dataSet = dataSets.item(i);
                    NodeList datas = dataSet.getChildNodes();
                    List<Object> newTestData = new LinkedList<>();

                    for (int j = 0; j < datas.getLength(); j++)
                    {
                        Node dataItem = datas.item(j);
                        if (dataItem.getNodeName().equals("data"))
                        {
                            newTestData.add(dataItem.getTextContent());
                        }
                    }
                    testData.add(newTestData.toArray(new Object[0]));
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        
        return testData;
    }
}
