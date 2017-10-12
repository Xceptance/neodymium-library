package com.xceptance.neodymium.testdata;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class JsonFileReader
{
    public static List<Object[]> readFile(String filename)
    {
        return readFile(filename, Charset.forName("UTF-8"));
    }

    public static List<Object[]> readFile(String filename, Charset charset)
    {
        List<Object[]> data = new LinkedList<>();
        try
        {
            FileInputStream fileStram = new FileInputStream(filename);
            BufferedInputStream bufferedStream = new BufferedInputStream(fileStram);
            InputStreamReader streamReader = new InputStreamReader(new BufferedInputStream(bufferedStream), charset);
            JsonReader jsonReader = new JsonReader(streamReader);

            JsonArray asJsonArray = new JsonParser().parse(jsonReader).getAsJsonArray();

            for (int i = 0; i < asJsonArray.size(); i++)
            {
                JsonArray dataSet = asJsonArray.get(i).getAsJsonArray();
                Object[] newDataSet = new Object[dataSet.size()];
                for (int j = 0; j < dataSet.size(); j++)
                {
                    newDataSet[j] = dataSet.get(j).getAsString();
                }
                data.add(newDataSet);
            }

            jsonReader.close();
            streamReader.close();
            bufferedStream.close();
            fileStram.close();

            return data;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
