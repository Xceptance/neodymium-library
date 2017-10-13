package com.xceptance.xrunner;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.testdata.GenericFileReader;

@RunWith(NeodymiumRunner.class)
public class TestParameter
{
    @Parameters(name = "{index} - {0}")
    public static List<Object[]> getData()
    {
        // return JsonFileReader.readFile("src/test/resources/test_data.json");
        // return CsvFileReader.readFile("src/test/resources/test_data.csv");
        return GenericFileReader.readFile();
        // return XmlFileReader.readFile("test_data.xml");
    }

    @Parameter(0)
    public String string;

    @Parameter(1)
    public Integer integer;

    @Parameter(2)
    public long looong;

    @Test
    public void testMethod0()
    {
        System.out.println("string: " + string);
        System.out.println("integer: " + integer);
        System.out.println("looong: " + looong);
    }
}
