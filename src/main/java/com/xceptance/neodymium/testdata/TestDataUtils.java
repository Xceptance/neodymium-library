package com.xceptance.neodymium.testdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for test data handling.
 * 
 * @author Hartmut Arlt (Xceptance Software Technologies GmbH)
 */
public final class TestDataUtils
{
    /**
     * Class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataUtils.class);

    /**
     * Loaded test data.
     */
    private static final Map<Object, Map<String, String>> LOADED_DATA = new HashMap<Object, Map<String, String>>();

    /**
     * Loaded package data.
     */
    private static final Map<String, Map<String, String>> LOADED_PKG_DATA = new HashMap<String, Map<String, String>>();

    /**
     * Name of the default script package.
     */
    public static final String DEFAULT_PACKAGE = "";

    /**
     * Returns the test data sets associated with the given test case class.
     *
     * @param testClass
     *            the test case class
     * @return the data sets, or <code>null</code> if there are no associated test data sets
     * @throws FileNotFoundException
     *             if an explicitly configured data set file cannot be found
     * @throws IOException
     */
    public static List<Map<String, String>> getDataSets(final Class<?> testClass) throws FileNotFoundException, IOException
    {
        // no specific file -> try the usual suspects
        final Set<String> fileNames = new LinkedHashSet<String>();

        final String dottedName = testClass.getName();
        final String slashedName = dottedName.replace('.', '/');

        String[] filetype = new String[]
            {
                ".csv", ".json", ".xml"
            };
        for (final String fileExtension : filetype)
        {
            fileNames.add(slashedName + fileExtension);
            fileNames.add(dottedName + fileExtension);
        }

        List<File> dataSetFileDirs = new LinkedList<>();
        dataSetFileDirs.add(new File("."));

        // look for such a file in the usual directories
        return getDataSets(dataSetFileDirs, fileNames, testClass);
    }

    /**
     * Looks for a data set file and, if found, returns its the data sets. Tries all the specified file names in all the
     * passed directories and finally in the class path.
     *
     * @param dataSetFileDirs
     *            the directories to search
     * @param fileNames
     *            the file names to try
     * @param testClass
     *            the test case class as the class path context
     * @return the data sets, or <code>null</code> if no data sets file was found
     * @throws IOException
     *             if an I/O error occurred
     */
    private static List<Map<String, String>> getDataSets(final List<File> dataSetFileDirs, final Set<String> fileNames,
                                                         final Class<?> testClass)
        throws IOException
    {
        // look for a data set file in the passed directories
        for (final File directory : dataSetFileDirs)
        {
            for (final String fileName : fileNames)
            {
                final File batchDataFile = new File(directory, fileName);
                if (batchDataFile.isFile())
                {
                    return readDataSets(batchDataFile);
                }
            }
        }

        // look for a data set file in the class path
        for (final String fileName : fileNames)
        {
            final InputStream input = testClass.getResourceAsStream("/" + fileName);
            if (input != null)
            {
                OutputStream output = null;
                File batchDataFile = null;

                try
                {
                    // copy the stream to a temporary file
                    final String extension = "." + FilenameUtils.getExtension(fileName);
                    batchDataFile = File.createTempFile("dataSets_", extension);
                    output = new FileOutputStream(batchDataFile);

                    IOUtils.copy(input, output);
                    output.flush();

                    // read the data sets from the temporary file
                    return readDataSets(batchDataFile);
                }
                finally
                {
                    // clean up
                    IOUtils.closeQuietly(input);
                    IOUtils.closeQuietly(output);
                    FileUtils.deleteQuietly(batchDataFile);
                }
            }
        }

        return null;
    }

    /**
     * Returns the test data sets contained in the given test data file. The data set provider used to read the file is
     * determined from the data file's extension.
     *
     * @param dataSetsFile
     *            the test data set file
     * @return the data sets
     */
    private static List<Map<String, String>> readDataSets(final File dataSetsFile)
    {
        LOGGER.debug("Test data set file used: " + dataSetsFile.getAbsolutePath());

        final String fileExtension = FilenameUtils.getExtension(dataSetsFile.getName());

        switch (fileExtension.toLowerCase())
        {
            case "csv":
                return CsvFileReader.readFile(dataSetsFile);

            case "xml":
            case "json":
            default:
                // TODO: xml, json, ...
                throw new NotImplementedException("Not implemented for file type: " + fileExtension);
        }
    }
    
    
    /**
     * Returns the package test data for the given test class.
     * 
     * @param clazz
     *            the test class
     * @return package test data
     */
    public static Map<String, String> getPackageTestData(final Class<?> clazz)
    {
        final Package pkg = clazz.getPackage();
        final String packageName = pkg == null ? DEFAULT_PACKAGE : pkg.getName();

        try
        {
            final String baseDir;
            if (packageName.length() > 0)
            {
                final int nbPkgDelims = org.apache.commons.lang3.StringUtils.countMatches(packageName, ".");
                final StringBuilder sb = new StringBuilder();

                sb.append("..");
                for (int i = 0; i < nbPkgDelims; i++)
                {
                    sb.append('/').append("..");

                }

                baseDir = sb.toString();
            }
            else
            {
                baseDir = ".";
            }

            return getPackageTestData(clazz, baseDir, packageName);
        }
        catch (final Exception e)
        {
            LOGGER.error("Failed to load test data for package '" + packageName + "'.", e);
        }
        return Collections.emptyMap();
    }

    /**
     * Returns the package test data for the given script package.
     * 
     * @param clazz
     *            the context class to be used for resource lookup (pass {@code null} to force file lookup)
     * @param baseDir
     *            the base directory to be used for data file lookup
     * @param packageName
     *            the package name
     * @return the package test data
     */
    private static Map<String, String> getPackageTestData(final Class<?> clazz, final String baseDir, final String packageName)
    {
        final ArrayList<String> pkgs = new ArrayList<String>();
        String pkgName = packageName;
        int idx = pkgName.lastIndexOf('.');
        while (idx > -1)
        {
            pkgs.add(pkgName);
            pkgName = pkgName.substring(0, idx);
            idx = pkgName.lastIndexOf('.');
        }

        if (!DEFAULT_PACKAGE.equals(pkgName))
        {
            pkgs.add(pkgName);
        }

        final Map<String, String> m = new HashMap<String, String>(getOrLoadPackageData(clazz, baseDir, DEFAULT_PACKAGE));
        for (int i = pkgs.size() - 1; i >= 0; i--)
        {
            m.putAll(getOrLoadPackageData(clazz, baseDir, pkgs.get(i)));
        }

        return m;
    }

    /**
     * Returns the package test data from the internal cache or loads it from disk.
     * 
     * @param clazz
     *            the context class to be used for resource lookup (pass {@code null} to force file lookup)
     * @param baseDir
     *            the base directory
     * @param packageName
     *            the package name
     * @return the package test data
     */
    private static Map<String, String> getOrLoadPackageData(final Class<?> clazz, final String baseDir, final String packageName)
    {
        Map<String, String> data = LOADED_PKG_DATA.get(packageName);
        if (data == null)
        {
            synchronized (LOADED_PKG_DATA)
            {
                data = LOADED_PKG_DATA.get(packageName);
                if (data == null)
                {
                    data = TestDataUtils.getPackageTestData(clazz, baseDir, packageName);
                    LOADED_PKG_DATA.put(packageName, data);
                }
            }
        }
        return data;
    }
}