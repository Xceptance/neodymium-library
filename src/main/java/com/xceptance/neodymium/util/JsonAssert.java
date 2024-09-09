package com.xceptance.neodymium.util;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.qameta.allure.Allure;

public class JsonAssert
{
    private static String htmlScript(String expectedJson, String actualJson, JSONCompareMode jsonCompareMode) 
    {
        boolean arrayOrderDoesntMatters = false;
        boolean arrayIsExtensible = false;
        
        if (jsonCompareMode == JSONCompareMode.LENIENT) 
        {
            arrayOrderDoesntMatters = true;
            arrayIsExtensible = true;
        }
        else if (jsonCompareMode == JSONCompareMode.NON_EXTENSIBLE) 
        {
            arrayOrderDoesntMatters = true;
        }
        
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
                  
                  + "const detectMoveFalseInstance = jsondiffpatch.create({"
                    + "arrays: {"
                      + "detectMove: false"
                    + "}"
                  + "});"
                  + "const detectMoveTrueInstance = jsondiffpatch.create({"
                    + "arrays: {"
                      + "detectMove: true"
                    + "}"
                  + "});"

                  + "const left = " + expectedJson + ";"
                  + "const right = " + actualJson + ";"
                  
                  + "function sortArraysInJson(json) {"
                    + "for (const key in json) {"
                      + "if (Array.isArray(json[key])) {"
                        + "if (json[key].every(item => typeof item === \"object\" && !Array.isArray(item))) {"
                          + "const firstKey = Object.keys(json[key][0])[0];"
                          + "json[key].sort((a, b) => a[firstKey].localeCompare(b[firstKey]));"
                        + "}"
                        + "else {"
                          + "json[key].sort();"
                        + "}"
                      + "}"
                      + "else if (typeof json[key] === \"object\") {"
                        + "sortArraysInJson(json[key]);"
                      + "}"
                    + "}"
                  + "}"
                    
                  + "function deleteArraysWithLengthOne(delta, deltaMove) {"
                    + "for (const key in delta) {"
                      + "if (Array.isArray(delta[key])) {"
                        + "if (delta[key].length === 1 && key in deltaMove) {"
                          + "delete delta[key];"
                        + "}"
                        + "else {"
                          + "deleteArraysWithLengthOne(delta[key], deltaMove[key]);"
                        + "}"
                      + "}"
                      + "else if (typeof delta[key] === \"object\") {"
                        + "deleteArraysWithLengthOne(delta[key], deltaMove[key]);"
                      + "}"
                    + "}"
                  + "}"
                    
                  + "if (" + arrayOrderDoesntMatters + ") {"
                    + "sortArraysInJson(left);"
                    + "sortArraysInJson(right);"
                  + "}"
                  
                  + "const deltaDetectMoveTrue = detectMoveTrueInstance.diff(left, right);"
                  + "const deltaDetectMoveFalse = detectMoveFalseInstance.diff(left, right);"
                  
                  + "if (" + arrayIsExtensible + ") {"
                    + "deleteArraysWithLengthOne(deltaDetectMoveFalse, deltaDetectMoveTrue);"
                  + "}"
                  
                  + "document.getElementById('visual').innerHTML = htmlFormatter.format(deltaDetectMoveFalse, left);"
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

    public static void assertEquals(String expectedJson, String actualJson, JSONCompareMode jsonCompareMode)
    {
        try
        {
            SelenideAddons.wrapAssertionError(() -> {
                JSONAssert.assertEquals("both json's are not equal (see attachment 'Json Compare')", expectedJson, actualJson, jsonCompareMode);
            });
        }
        catch (AssertionError | JSONException e)
        {
            Allure.addAttachment("Json Compare", "text/html", htmlScript(expectedJson, actualJson, jsonCompareMode), "html");
            
            throw e;
        }
    }

    public static void assertNotEquals(String expectedJson, String actualJson, JSONCompareMode jsonCompareMode)
    {
        try
        {
            SelenideAddons.wrapAssertionError(() -> {
                JSONAssert.assertNotEquals("both json's are the exact same (see attachment 'Json View')", expectedJson, actualJson, jsonCompareMode);
            });
        }
        catch (AssertionError | JSONException e)
        {

            Allure.addAttachment("Json View", "text/html", DataUtils.convertJsonToHtml(expectedJson), "html");

            throw e;
        }
    }
}
