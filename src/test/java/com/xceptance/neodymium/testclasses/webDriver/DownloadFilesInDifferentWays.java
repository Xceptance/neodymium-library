package com.xceptance.neodymium.testclasses.webDriver;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exactText;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.tests.NeodymiumTest;
import com.xceptance.neodymium.util.Neodymium;

@RunWith(NeodymiumRunner.class)
@Browser("chrome_download")
@Browser("firefox_download")
public class DownloadFilesInDifferentWays extends NeodymiumTest
{
    private File fileName;

    @Test
    public void downloadViaLink()
    {
        fileName = new File("target/02_2020-Java_aktuell-Autor-Rene_Schwietzke-High-Performance-Java-Hinter-den-Kulissen-von-Java.pdf");
        Selenide.open("https://blog.xceptance.com/2020/02/28/ijug-magazin-java-aktuell-high-performance-java/");
        $(".alignright.is-resized").scrollIntoView(true).click();
        waitForFileDownloading();
        validateFilePresentInDownloadHistory();
    }

    @Test
    public void downloadOnFormSubmission()
    {
        fileName = new File("target/2020-in-one-picture.pdf");
        Selenide.open("https://png2pdf.com/");
        $(".cc-dismiss").click();
        $("#upload-buttons-wrapper input").uploadFile(new File("src/test/resources/2020-in-one-picture.png"));
        $("#download-all:not(.ui-button-disabled)").shouldBe(visible);
        $(".plupload_file_button").scrollIntoView(true).click();
        waitForFileDownloading();
        validateFilePresentInDownloadHistory();
    }

    @Test
    public void downloadPerLinkWithSelenide() throws FileNotFoundException
    {
        Selenide.open("https://blog.xceptance.com/2020/02/28/ijug-magazin-java-aktuell-high-performance-java/");
        fileName = $(".alignright.is-resized>a").scrollIntoView(true).download();
        waitForFileDownloading();
    }

    @After
    public void deleteFile()
    {
        fileName.delete();
    }

    private void waitForFileDownloading()
    {
        Selenide.Wait().withMessage("File was not downloaded").withTimeout(Duration.ofMillis(6000)).until((driver) -> {
            return fileName.exists() && fileName.canRead();
        });
    }

    private void validateFilePresentInDownloadHistory()
    {
        if (Neodymium.getBrowserName().contains("chrome"))
        {
            Selenide.open("chrome://downloads/");
            $$(Selectors.shadowCss("#title-area", "downloads-manager" ,"#downloadsList downloads-item")).findBy(exactText(fileName.getName())).parent().find("#description").shouldHave(attribute("hidden"));
        }
        else
        {
            Selenide.open("about:downloads");
            $("description[tooltiptext='" + fileName.getName() + "']").closest(".download-state").shouldHave(attribute("state", "1"));
        }
    }
}
