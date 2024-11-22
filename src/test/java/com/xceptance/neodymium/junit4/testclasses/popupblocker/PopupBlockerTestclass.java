package com.xceptance.neodymium.junit4.testclasses.popupblocker;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.junit4.NeodymiumRunner;
import com.xceptance.neodymium.junit4.tests.NeodymiumTest;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_1024x768")
public class PopupBlockerTestclass extends NeodymiumTest
{
    @Test
    public void testPopUpIsBlocked()
    {
        Selenide.open("https://www.xceptance.com/");
        String popup = "var e = document.createElement(\"div\");\r\n"
                       + "e.innerHTML = \"testThing\";\r\n"
                       + "e.setAttribute('id','myWindow');\r\n"
                       + "e.setAttribute('onclick','this.remove()');\r\n"
                       + "document.body.appendChild(e);";
        Selenide.executeJavaScript(popup, "");
        Selenide.sleep(1500);
        $("#myWindow").shouldNotBe(visible);
    }

    @Test
    public void testPopUpIsNotBlocked()
    {
        Selenide.open("https://www.xceptance.com/");
        String popup = "var e = document.createElement(\"div\");\r\n"
                       + "e.innerHTML = \"testThing\";\r\n"
                       + "e.setAttribute('id','anotherWindow');\r\n"
                       + "e.setAttribute('onclick','this.remove()');\r\n"
                       + "document.body.appendChild(e);";
        Selenide.executeJavaScript(popup, "");
        Selenide.sleep(1500);
        $("#anotherWindow").shouldBe(visible);
    }

    @Test
    public void testWithNoPopUp()
    {
        Selenide.open("https://www.xceptance.com/");
        Selenide.sleep(1500);
        $("#myWindow").shouldNotBe(visible);
    }
}
