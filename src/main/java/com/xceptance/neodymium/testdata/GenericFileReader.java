package com.xceptance.neodymium.testdata;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GenericFileReader
{
    public static List<Map<String, String>> readFile()
    {
        String callerClassName = getCallerClassName();
        File dataFile = findDataFile(callerClassName);

        if (dataFile != null)
        {
            Path pathToFile = dataFile.toPath();
            String lowerCaseFilename = pathToFile.getFileName().toString().toLowerCase();
            if (lowerCaseFilename.endsWith(".csv"))
            {
                return CsvFileReader.readFile(pathToFile.toFile());
            }
            else if (lowerCaseFilename.endsWith(".json"))
            {
                return JsonFileReader.readFile(pathToFile.toFile());
            }
            else if (lowerCaseFilename.endsWith(".xml"))
            {
                return XmlFileReader.readFile(pathToFile.toFile());
            }
            else if (lowerCaseFilename.endsWith(".properties"))
            {
                return PropertyFileReader.readFile(pathToFile.toFile());
            }
        }

        throw new RuntimeException("No data file found for class: " + callerClassName);
    }

    private static File findDataFile(String fullQualifiedClassname)
    {
        // <package>/<class>.csv
        // <package>/<class>.json
        // <package>/<class>.xml
        // <package>/<class>_data.csv
        // <package>/<class>_data.json
        // <package>/<class>_data.xml
        // <package>/package.csv
        // <package>/package.json
        // <package>/package.xml
        // <package>/package_data.csv
        // <package>/package_data.json
        // <package>/package_data.xml

        String[] splits = fullQualifiedClassname.split("\\.");
        List<String> qualifier = new LinkedList<>();

        for (String split : splits)
        {
            qualifier.add(split);
        }

        String[] postfix = new String[]
            {
                "", "_data"
            };

        String[] filetype = new String[]
            {
                ".csv", ".json", ".xml", ".properties"
            };

        String testClassPath = String.join("/", qualifier);

        qualifier.remove(qualifier.size() - 1);
        qualifier.add("package");
        String packageClassPath = String.join("/", qualifier);

        String[] paths = new String[]
            {
                testClassPath, packageClassPath
            };

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        // try to find [<classname>|package](_data).[csv|json|xml] in package folder
        for (String path : paths)
        {
            for (String pf : postfix)
            {
                for (String ft : filetype)
                {
                    StringBuilder sb = new StringBuilder(64);
                    URL resource = systemClassLoader.getResource(sb.append(path).append(pf).append(ft).toString());
                    if (resource != null)
                        return new File(resource.getFile());

                }
            }
        }

        return null;
    }

    private static String getCallerClassName()
    {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++)
        {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(GenericFileReader.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") < 0)
            {
                return ste.getClassName();
            }
        }
        return null;
    }
}
