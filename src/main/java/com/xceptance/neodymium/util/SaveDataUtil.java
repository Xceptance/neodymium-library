package com.xceptance.neodymium.util;

import java.util.List;

public class SaveDataUtil
{
    public static final String USED_BROWSER = "UsedBrowser";

    public static void saveUsedBrowsersToData(List<String> browserTags)
    {
        String result = "";
        if (Neodymium.getData().get("UsedBrowsers") != null)
        {
            result = Neodymium.getData().get(USED_BROWSER);
            for (String s : browserTags)
            {
                if (!result.contains(s))
                {
                    result += "\n," + s;
                }
            }
        }
        else
        {
            for (String s : browserTags)
            {
                result += s + "\n,";
            }
            result = result.substring(0, result.length() - 3);
        }
        Neodymium.getData().put(USED_BROWSER, result);
    }
}
