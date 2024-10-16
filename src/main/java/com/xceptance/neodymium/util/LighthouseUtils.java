package com.xceptance.neodymium.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

public class LighthouseUtils
{
    public static void createLightHouseReport(WebDriver driver, String URL, String reportName) throws InterruptedException, IOException 
    {
        // close window to avoid conflict with lighthouse
        String newWindow = windowOperations(driver);

        // start lighthouse report
        lighthouseAudit(URL, reportName);
        
        // TODO - add lighthouse report as attachment to allure report

        // switch back to saved URL
        driver.switchTo().window(newWindow);
        driver.get(URL);
    }
    
    private static String windowOperations(WebDriver driver) throws InterruptedException
    {
        String originalWindow = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);
        String newWindow = driver.getWindowHandle();
        driver.switchTo().window(originalWindow);
        driver.close();
        return newWindow;
    }

    private static void lighthouseAudit(String URL, String ReportName) throws IOException
    {
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "lighthouse", "--chrome-flags=\"--ignore-certificate-errors\"", URL, "--port=9999", "--preset=desktop", "--output=html", "--output-path=target/"
                                                                                                                                                                                              + ReportName
                                                                                                                                                                                              + ".html");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true)
        {
            line = r.readLine();
            if (line == null)
            {
                break;
            }
        }
    }
}
