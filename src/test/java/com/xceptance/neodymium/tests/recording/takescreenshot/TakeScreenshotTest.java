package com.xceptance.neodymium.tests.recording.takescreenshot;

import java.io.IOException;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Selenide;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.recording.FilmTestExecution;
import com.xceptance.neodymium.recording.TakeScreenshotsThread;
import com.xceptance.neodymium.util.Neodymium;

@Browser("Chrome_headless")
@RunWith(NeodymiumRunner.class)
public class TakeScreenshotTest
{
    @Test
    public void test() throws IOException, InterruptedException
    {
        TakeScreenshotsThread takeScreenshotsThread = new TakeScreenshotsThread(Neodymium.getDriver(), MockWriter.class, FilmTestExecution.getContextGif(), UUID.randomUUID()
                                                                                                                                                                .toString());
        Selenide.open("https://www.xceptance.com/en/");
        takeScreenshotsThread.start();
        Selenide.sleep(FilmTestExecution.getContextGif().oneImagePerMilliseconds());
        takeScreenshotsThread.stopRun(false);
        takeScreenshotsThread.join();
        Assert.assertNotNull("neither start nor stop method of writer were called by " + TakeScreenshotsThread.class.getSimpleName(), MockWriter.screenshots);
        Assert.assertTrue(TakeScreenshotsThread.class.getSimpleName() + " should only take one or two screenshots in this time",
                          MockWriter.screenshots.size() > 0 && MockWriter.screenshots.size() < 3);
    }
}
