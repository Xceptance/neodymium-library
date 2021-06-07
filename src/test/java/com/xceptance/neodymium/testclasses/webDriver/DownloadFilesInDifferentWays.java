package com.xceptance.neodymium.testclasses.webDriver;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.tests.NeodymiumTest;

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
}
