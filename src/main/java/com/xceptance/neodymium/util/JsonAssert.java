package com.xceptance.neodymium.util;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.qameta.allure.Allure;

public class JsonAssert
{    
    public static void assertEquals(String expectedJson, String actualJson)
    {
        try
        {
            SelenideAddons.wrapAssertionError(() -> {
                JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.LENIENT);
            });
        }
        catch (AssertionError | JSONException e)
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
                            + "</body>"
                          + "</html>";
            
            Allure.addAttachment("Json Compare", "text/html", html, "html");

            throw e;
        }
    }
}
