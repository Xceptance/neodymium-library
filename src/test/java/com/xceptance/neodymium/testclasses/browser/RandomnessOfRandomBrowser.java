package com.xceptance.neodymium.testclasses.browser;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.RandomBrowsers;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;
import com.xceptance.neodymium.util.Neodymium;

@Browser("browser1")
@Browser("browser2")
@Browser("browser3")
@Browser("browser4")
@Browser("browser5")
@Browser("browser6")
@Browser("browser7")
@Browser("browser8")
@Browser("browser9")
@Browser("browser10")
@Browser("browser11")
@Browser("browser12")
@Browser("browser13")
@Browser("browser14")
@Browser("browser15")
@Browser("browser16")
@Browser("browser17")
@Browser("browser18")
@Browser("browser19")
@Browser("browser20")
@Browser("browser21")
@Browser("browser22")
@Browser("browser23")
@Browser("browser24")
@Browser("browser25")
@Browser("browser26")
@Browser("browser27")
@Browser("browser28")
@Browser("browser29")
@Browser("browser30")
@Browser("browser31")
@Browser("browser32")
@Browser("browser33")
@Browser("browser34")
@Browser("browser35")
@Browser("browser36")
@Browser("browser37")
@Browser("browser38")
@Browser("browser39")
@Browser("browser40")
@RandomBrowsers(2)
@RunWith(NeodymiumRunner.class)
public class RandomnessOfRandomBrowser
{
    private static ArrayList<String> browsers = new ArrayList<>();

    @Test
    public void test1()
    {
        browsers.add(Neodymium.getBrowserProfileName());
    }

    @Test
    @SuppressBrowsers
    public void test2()
    {
        System.out.print(browsers);
        Assert.assertEquals(2, browsers.size());
        boolean changedOrder = false;
        for (int i = 1; i < browsers.size(); i++)
        {
            if (!browsers.get(i).equals("browser" + i))
            {
                changedOrder = true;
                break;
            }
        }
        Assert.assertTrue(changedOrder);
    }
}
