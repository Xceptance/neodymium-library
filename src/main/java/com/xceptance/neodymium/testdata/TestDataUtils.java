package com.xceptance.neodymium.testdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
}