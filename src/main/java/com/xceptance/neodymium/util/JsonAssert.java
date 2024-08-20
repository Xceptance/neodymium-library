package com.xceptance.neodymium.util;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.qameta.allure.Allure;

public class JsonAssert
{   
    private static String htmlScript(String expectedJson, String actualJson) 
    {
        String html = ""
            + "<html>"
              + "<head>"
                + "<link"
                  + " rel=\"stylesheet\""
                  + " href=\"https://esm.sh/jsondiffpatch@0.6.0/lib/formatters/styles/html.css\""
                  + " type=\"text/css\""
                + "/>"
              + "</head>"
              + "<body>"
                + "<div id=\"visual\"></div>"
                + "<script type=\"module\">"
                  + "import * as jsondiffpatch from 'https://esm.sh/jsondiffpatch@0.6.0';"
                  + "import * as htmlFormatter from 'https://esm.sh/jsondiffpatch@0.6.0/formatters/html';"
                  
                  + "const jsondiffpatchInstance = jsondiffpatch.create({"
                    + "objectHash: function (obj) {return obj._id || obj.id;},"
                    + "arrays: {detectMove: false}"
                  + "});"
                  + "const left = " + expectedJson + ";"
                  + "const right = " + actualJson + ";"
                  + "const delta = jsondiffpatchInstance.diff(left, right);"
                  + "document.getElementById('visual').innerHTML = htmlFormatter.format(delta, left);"
                + "</script>"
                + "<script>"
                    + "window.onerror = function (e) {"
                        + "document.getElementById('visual').innerHTML = \"An error occured: please check all json data for correct syntax.\";"
                    + "};"
                + "</script>"
              + "</body>"
            + "</html>";
        
        return html;
    }
    
    public static void assertEquals(String expectedJson, String actualJson)
    {
        try
        {
            SelenideAddons.wrapAssertionError(() -> {
                JSONAssert.assertEquals("both json's are not equal (see attachment 'Json Compare')", expectedJson, actualJson, JSONCompareMode.LENIENT);
            });
        }
        catch (AssertionError | JSONException e)
        {            
            Allure.addAttachment("Json Compare", "text/html", htmlScript(expectedJson, actualJson), "html");

            throw e;
        }
    }
    
    public static void assertNotEquals(String expectedJson, String actualJson)
    {
        try
        {
            SelenideAddons.wrapAssertionError(() -> {
                JSONAssert.assertNotEquals("both json's are the exact same (see attachment 'Json View')", expectedJson, actualJson, JSONCompareMode.LENIENT);
            });
        }
        catch (AssertionError | JSONException e)
        {            
            
            Allure.addAttachment("Json View", "text/html", DataUtils.convertJsonToHtml(expectedJson), "html");
            
            throw e;
        }
    }
}
