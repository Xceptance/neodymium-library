package com.xceptance.neodymium.testdata;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CsvReader
{
    public static List<String[]> readFile(String filename, Charset charset, CSVFormat format)
    {
        List<String[]> csvData = new LinkedList<>();

        try
        {
            CSVParser csvParser = CSVParser.parse(new File(filename), charset, format);
            List<CSVRecord> records = csvParser.getRecords();
            for (CSVRecord record : records)
            {
                String[] line = new String[record.size()];

                for (int i = 0; i < record.size(); i++)
                {
                    line[i] = record.get(i);
                }

                csvData.add(line);
            }

            return csvData;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static List<String[]> readFile(String filename)
    {
        return readFile(filename, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
    }

    public static List<Object[]> convert(List<String[]> data)
    {
        List<Object[]> r = new LinkedList<>();

        for (String[] entry : data)
        {
            r.add(entry);
        }

        return r;
    }
}
