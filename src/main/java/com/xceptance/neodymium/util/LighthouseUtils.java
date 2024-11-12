package com.xceptance.neodymium.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.qameta.allure.Allure;

public class LighthouseUtils
{
    public static void createLightHouseReport(WebDriver driver, String URL, String reportName) throws Exception 
    {
        // validate that lighthouse is installed
        try 
        {
            new ProcessBuilder("cmd.exe", "/c", "lighthouse", "--version").start();
        }
        catch (Exception e)
        {
            throw new Exception("lighthouse binary not found, please install lighthouse and add it to the PATH");
        }
        
        // validate chrome browser (lighthouse only works for chrome)
        Assert.assertTrue("the current browser is " + Neodymium.getBrowserName() + ", but lighthouse only works in combination with chrome", Neodymium.getBrowserName().contains("chrome"));
        
        // close window to avoid conflict with lighthouse
        String newWindow = windowOperations(driver);

        // start lighthouse report
        lighthouseAudit(URL, reportName);
        
        // get report json
        FileReader reader = new FileReader("target/" + reportName + ".report.json");
        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
        
        // get report json scores
        JsonObject categories = json.getAsJsonObject("categories");
        double performanceScore = categories.getAsJsonObject("performance").get("score").getAsDouble();
        double accessibilityScore = categories.getAsJsonObject("accessibility").get("score").getAsDouble();
        double bestPracticesScore = categories.getAsJsonObject("best-practices").get("score").getAsDouble();
        double seoScore = categories.getAsJsonObject("seo").get("score").getAsDouble();
        
        // validate if values in report json are greater than defined threshold in config
        SelenideAddons.wrapAssertionError(() -> {
            Assert.assertTrue("the performance score " + performanceScore + " doesn't exceed nor match the required threshold of " + Neodymium.configuration().lighthousePerformance() + ", please improve the score to match expectations", Neodymium.configuration().lighthousePerformance() <= performanceScore);
            Assert.assertTrue("the accessibility score " + accessibilityScore + " doesn't exceed nor match the required threshold of " + Neodymium.configuration().lighthousePerformance() + ", please improve the score to match expectations", Neodymium.configuration().lighthouseAccessibility() <= accessibilityScore);
            Assert.assertTrue("the best practices score " + bestPracticesScore + " doesn't exceed nor match the required threshold of " + Neodymium.configuration().lighthousePerformance() + ", please improve the score to match expectations", Neodymium.configuration().lighthouseBestPractices() <= bestPracticesScore);
            Assert.assertTrue("the seo score " + seoScore + " doesn't exceed nor match the required threshold of " + Neodymium.configuration().lighthousePerformance() + ", please improve the score to match expectations", Neodymium.configuration().lighthouseSeo() <= seoScore);
        });
        
        // add report html to allure
        Allure.addAttachment(reportName, "text/html", FileUtils.openInputStream(new File("target/" + reportName + ".report.html")), "html");
        
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

    private static void lighthouseAudit(String URL, String reportName) throws IOException
    {
        ProcessBuilder builder = new ProcessBuilder();
        
        if (System.getProperty("os.name").toLowerCase().contains("win")) 
        {
            builder = new ProcessBuilder("cmd.exe", "/c", "lighthouse", "--chrome-flags=\"--ignore-certificate-errors\"", URL, "--port=9999", "--preset=desktop", "--output=json", "--output=html", "--output-path=target/" + reportName + ".json");
        }
        else if (System.getProperty("os.name").toLowerCase().contains("linux"))
        {
            // TODO (with luck linux and macOS are the same, ask Olha for help)
        }

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while (r.readLine() != null)
        {
            continue;
        }
    }
}
