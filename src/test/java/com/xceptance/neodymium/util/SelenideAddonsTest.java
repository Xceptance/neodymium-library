package com.xceptance.neodymium.util;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.StaleElementReferenceException;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEvent.EventStatus;
import com.codeborne.selenide.logevents.LogEventListener;
import com.codeborne.selenide.logevents.SelenideLog;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.module.statement.browser.multibrowser.Browser;
import com.xceptance.neodymium.module.statement.browser.multibrowser.SuppressBrowsers;

@RunWith(NeodymiumRunner.class)
@Browser("Chrome_headless")
public class SelenideAddonsTest
{
    private void openPostersStartPage()
    {
        Selenide.open("https://posters.xceptance.io:8443/posters/");
    }

    @Test
    public void testMatchesAttributeCondition()
    {
        openPostersStartPage();
        $("#header-search-trigger").click();

        $("#searchForm input").should(SelenideAddons.matchesAttribute("placeholder", "Search"));
    }

    @Test
    public void testMatchAttributeCondition()
    {
        openPostersStartPage();
        $("#header-search-trigger").click();

        $("#searchForm input").should(SelenideAddons.matchAttribute("placeholder", "^S.a.c."));
        $("#searchForm input").should(SelenideAddons.matchAttribute("placeholder", "\\D+"));
    }

    @Test
    public void testMatchAttributeConditionError()
    {
        openPostersStartPage();
        $("#header-search-trigger").click();

        Assert.assertThrows(ElementShould.class, () -> {
            $("#searchForm input").should(SelenideAddons.matchAttribute("placeholder", "\\d+"));
        });
    }

    @Test
    public void testMatchAttributeConditionErrorMissingAttribute()
    {
        openPostersStartPage();
        $("#header-search-trigger").click();

        Assert.assertThrows(ElementShould.class, () -> {
            $("#searchForm input").should(SelenideAddons.matchAttribute("foo", "bar"));
        });
    }

    @Test
    public void testMatchesValueCondition()
    {
        openPostersStartPage();
        $("#header-search-trigger").click();
        $("#searchForm input").val("mozzarella").submit();

        $("#searchForm input").should(SelenideAddons.matchesValue("ozzarell"));
    }

    @Test
    public void testMatchValueCondition()
    {
        openPostersStartPage();
        $("#header-search-trigger").click();
        $("#searchForm input").val("mozzarella").submit();

        $("#searchForm input").should(SelenideAddons.matchValue("^m.z.a.e.l.$"));
        $("#searchForm input").should(SelenideAddons.matchValue("\\D+"));
    }

    @Test
    public void testMatchValueConditionError()
    {
        openPostersStartPage();
        $("#header-search-trigger").click();
        $("#searchForm input").val("mozzarella").submit();

        Assert.assertThrows(ElementShould.class, () -> {
            $("#searchForm input").should(SelenideAddons.matchValue("\\d+"));
        });
    }

    @Test()
    public void testWrapAssertion()
    {
        openPostersStartPage();

        SelenideAddons.wrapAssertionError(() -> {
            Assert.assertEquals("Posters - The Ultimate Online Shop", Selenide.title());
        });
    }

    @Test
    public void testWrapAssertionError()
    {
        openPostersStartPage();

        Assert.assertThrows(UIAssertionError.class, () -> {
            SelenideAddons.wrapAssertionError(() -> {
                Assert.assertEquals("MyPageTitle", Selenide.title());
            });
        });
    }

    @Test
    public void testWrapSoftAssertionError()
    {
        final String errMessage = "Title doesn't match.";
        SelenideLogger.addListener("testListener", new LogEventListener()
        {
            @Override
            public void afterEvent(LogEvent currentLog)
            {
                SelenideLog log = (SelenideLog) currentLog;
                if (log.getStatus().equals(EventStatus.FAIL))
                {
                    Assert.assertEquals("Selenide log event not matching", "Assertion error", log.getElement());
                    Assert.assertTrue("Selenide log event not matching", log.getError().getMessage().startsWith(errMessage));
                }
            }

            @Override
            public void beforeEvent(LogEvent currentLog)
            {
                // ignore
            }
        });

        openPostersStartPage();
        Neodymium.softAssertions(true);
        try
        {
            SelenideAddons.wrapAssertionError(() -> {
                Assert.assertEquals(errMessage, "MyPageTitle", Selenide.title());
            });
        }
        finally
        {
            Neodymium.softAssertions(false);
            SelenideLogger.removeListener("testListener");
        }
    }

    @Test
    public void testWrapSoftAssertionErrorWithoutMessage()
    {
        final String errMessage = "No error message provided by the Assertion.";

        SelenideLogger.addListener("testListener", new LogEventListener()
        {
            @Override
            public void afterEvent(LogEvent currentLog)
            {
                SelenideLog log = (SelenideLog) currentLog;
                if (log.getStatus().equals(EventStatus.FAIL))
                {
                    Assert.assertEquals("Selenide log event not matching", "Assertion error", log.getElement());
                    Assert.assertEquals("Selenide log event not matching", log.getSubject(), errMessage);
                }
            }

            @Override
            public void beforeEvent(LogEvent currentLog)
            {
                // ignore
            }
        });

        openPostersStartPage();
        Neodymium.softAssertions(true);
        try
        {
            SelenideAddons.wrapAssertionError(() -> {
                Assert.assertTrue(Selenide.title().startsWith("MyPageTitle"));
            });
        }
        finally
        {
            Neodymium.softAssertions(false);
            SelenideLogger.removeListener("testListener");
        }
    }

    @Test
    public void testWrapAssertionErrorWithoutMessage()
    {
        final String errMessage = "AssertionError: No error message provided by the Assertion.";
        try
        {
            openPostersStartPage();
            SelenideAddons.wrapAssertionError(() -> {
                Assert.assertTrue(Selenide.title().startsWith("MyPageTitle"));
            });
        }
        catch (UIAssertionError e)
        {
            Assert.assertTrue(e.getMessage().startsWith(errMessage));
        }
    }

    @Test()
    @SuppressBrowsers
    public void testSafeRunnable()
    {
        // preparing the test setup as kind of generator
        AtomicInteger counter = new AtomicInteger(0);
        List<Runnable> runArray = new ArrayList<Runnable>();
        runArray.add(
                     () -> {
                         throw new StaleElementReferenceException("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw new StaleElementReferenceException("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw new StaleElementReferenceException("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw new StaleElementReferenceException("You shall pass!");
                     });
        runArray.add(
                     () -> {
                         throw new StaleElementReferenceException("You shall never be seen!");
                     });
        final Iterator<Runnable> iterator = runArray.iterator();

        // testing the error path after three exceptions
        long startTime = new Date().getTime();
        try
        {
            SelenideAddons.$safe(() -> {
                counter.incrementAndGet();
                if (iterator.hasNext())
                {
                    iterator.next().run();
                }
            });
        }
        catch (StaleElementReferenceException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("You shall pass!"));
        }
        long endTime = new Date().getTime();
        Assert.assertTrue(endTime - startTime > Neodymium.configuration().staleElementRetryTimeout());
        Assert.assertEquals(counter.get(), Neodymium.configuration().staleElementRetryCount() + 1);

        // testing the happy path after one exception
        SelenideAddons.$safe(() -> {
            counter.incrementAndGet();
            if (iterator.hasNext())
            {
                iterator.next().run();
            }
        });
        Assert.assertEquals(counter.get(), Neodymium.configuration().staleElementRetryCount() + 3);
    }

    @Test()
    public void testSafeSupplier()
    {
        // preparing the test setup as kind of generator
        AtomicInteger counter = new AtomicInteger(0);
        List<Runnable> runArray = new ArrayList<Runnable>();
        runArray.add(
                     () -> {
                         throw new StaleElementReferenceException("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw new StaleElementReferenceException("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw new StaleElementReferenceException("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw new StaleElementReferenceException("You shall pass!");
                     });
        runArray.add(
                     () -> {
                         throw new StaleElementReferenceException("You shall never be seen!");
                     });
        final Iterator<Runnable> iterator = runArray.iterator();

        // testing the error path after three exceptions
        openPostersStartPage();
        long startTime = new Date().getTime();
        try
        {
            SelenideAddons.$safe(() -> {
                counter.incrementAndGet();
                if (iterator.hasNext())
                {
                    iterator.next().run();
                }
                return $("body").should(exist);
            });
        }
        catch (StaleElementReferenceException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("You shall pass!"));
        }
        long endTime = new Date().getTime();
        Assert.assertTrue(endTime - startTime > Neodymium.configuration().staleElementRetryTimeout());
        Assert.assertEquals(counter.get(), Neodymium.configuration().staleElementRetryCount() + 1);

        // testing the happy path after one exception
        SelenideElement element = SelenideAddons.$safe(() -> {
            counter.incrementAndGet();
            if (iterator.hasNext())
            {
                iterator.next().run();
            }
            return $("body");
        });
        Assert.assertEquals(counter.get(), Neodymium.configuration().staleElementRetryCount() + 3);
        element.shouldBe(visible);
    }

    @Test()
    @SuppressBrowsers
    public void testSafeNestedException()
    {
        // preparing the test setup as kind of generator
        AtomicInteger counter = new AtomicInteger(0);
        List<Runnable> runArray = new ArrayList<Runnable>();
        runArray.add(
                     () -> {
                         throw getWrappedThrowable("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw getWrappedThrowable("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw getWrappedThrowable("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw getWrappedThrowable("You shall pass!");
                     });
        runArray.add(
                     () -> {
                         throw getWrappedThrowable("You shall never be seen!");
                     });
        final Iterator<Runnable> iterator = runArray.iterator();

        // testing the error path after three exceptions
        long startTime = new Date().getTime();
        try
        {
            SelenideAddons.$safe(() -> {
                counter.incrementAndGet();
                if (iterator.hasNext())
                {
                    iterator.next().run();
                }
            });
        }
        catch (RuntimeException e)
        {
            Assert.assertNotNull(e.getCause());
            Assert.assertTrue(e.getCause() instanceof StaleElementReferenceException);
            Assert.assertTrue(e.getCause().getMessage().startsWith("You shall pass!"));
        }
        long endTime = new Date().getTime();
        Assert.assertTrue(endTime - startTime > Neodymium.configuration().staleElementRetryTimeout());
        Assert.assertEquals(counter.get(), Neodymium.configuration().staleElementRetryCount() + 1);

        // testing the happy path after one exception
        SelenideAddons.$safe(() -> {
            counter.incrementAndGet();
            if (iterator.hasNext())
            {
                iterator.next().run();
            }
        });
        Assert.assertEquals(counter.get(), Neodymium.configuration().staleElementRetryCount() + 3);
    }

    private RuntimeException getWrappedThrowable(String message)
    {
        return new RuntimeException(new StaleElementReferenceException(message));
    }

    @Test()
    public void testRightwardDragAndDropUntilCondition()
    {
        openSliderPage();

        SelenideElement slider = $(".balSlider span[role=slider]");
        slider.shouldHave(attribute("aria-valuenow", "0"));
        SelenideAddons.dragAndDropUntilCondition(slider, slider, 40, 0, 3000, 23, Condition.attribute("aria-valuenow", "8"));
        slider.shouldHave(attribute("aria-valuenow", "8"));
    }

    @Test()
    public void testLeftwardDragAndDropUntilCondition()
    {
        openSliderPage();

        SelenideElement slider = $(".balSlider span[role=slider]");
        slider.shouldHave(attribute("aria-valuenow", "0"));
        SelenideAddons.dragAndDropUntilCondition(slider, slider, -40, 0, 3000, 23, Condition.attribute("aria-valuenow", "-8"));
        slider.shouldHave(attribute("aria-valuenow", "-8"));
    }

    @Test()
    public void testUpwardDragAndDropUntilCondition()
    {
        openSliderPage();

        SelenideElement slider = $("#equalizer .k-slider-vertical:first-child span.k-draghandle");
        slider.scrollIntoView("{'block':'center','inline':'center'}");
        slider.shouldHave(attribute("aria-valuenow", "10"));
        SelenideAddons.dragAndDropUntilCondition(slider, slider, 0, -10, 3000, 23, Condition.attribute("aria-valuenow", "16"));
        slider.shouldHave(attribute("aria-valuenow", "16"));
    }

    @Test()
    public void testDownwardDragAndDropUntilCondition()
    {
        openSliderPage();

        SelenideElement slider = $("#equalizer .k-slider-vertical:first-child span.k-draghandle");
        slider.scrollIntoView("{'block':'center','inline':'center'}");
        slider.shouldHave(attribute("aria-valuenow", "10"));
        SelenideAddons.dragAndDropUntilCondition(slider, slider, 0, 10, 3000, 23, Condition.attribute("aria-valuenow", "-8"));
        slider.shouldHave(attribute("aria-valuenow", "-8"));
    }

    @Test()
    public void testLeftwardDragAndDropUntilAttribute()
    {
        openSliderPage();

        SelenideElement slider = $(".balSlider span[role=slider]");
        slider.shouldHave(attribute("aria-valuenow", "0"));
        leftHorizontalDragAndDropUntilAttribute(slider, slider, -40, "aria-valuenow", "-8");
        slider.shouldHave(attribute("aria-valuenow", "-8"));
    }

    @Test()
    public void testDragAndDropAssertionError()
    {
        openSliderPage();

        SelenideElement slider = $(".balSlider span[role=slider]");
        slider.shouldHave(attribute("aria-valuenow", "0"));

        Assert.assertThrows(UIAssertionError.class, () -> {
            SelenideAddons.dragAndDropUntilCondition(slider, slider, -10, 0, 3000, -1, Condition.attribute("aria-valuenow", "-16"));
        });
    }

    @Test()
    public void testRightwardDragAndDrop()
    {
        openSliderPage();

        SelenideElement slider = $(".balSlider span[role=slider]");
        slider.shouldHave(attribute("aria-valuenow", "0"));
        SelenideAddons.dragAndDrop(slider, 42, 0);
        slider.shouldHave(attribute("aria-valuenow", "2"));
    }

    @Test()
    public void testLeftwardDragAndDrop()
    {
        openSliderPage();

        SelenideElement slider = $(".balSlider span[role=slider]");
        slider.shouldHave(attribute("aria-valuenow", "0"));
        SelenideAddons.dragAndDrop(slider, -42, 0);
        slider.shouldHave(attribute("aria-valuenow", "-2"));
    }

    @Test()
    public void testDownwardDragAndDrop()
    {
        openSliderPage();

        SelenideElement slider = $("#equalizer .k-slider-vertical:first-child span.k-draghandle");
        slider.scrollIntoView("{'block':'center','inline':'center'}");
        slider.shouldHave(attribute("aria-valuenow", "10"));
        SelenideAddons.dragAndDrop(slider, 0, 12);
        slider.shouldHave(attribute("aria-valuenow", "6"));
    }

    @Test()
    public void testUpwardDragAndDrop()
    {
        openSliderPage();

        SelenideElement slider = $("#equalizer .k-slider-vertical:first-child span.k-draghandle");
        slider.scrollIntoView("{'block':'center','inline':'center'}");
        slider.shouldHave(attribute("aria-valuenow", "10"));
        SelenideAddons.dragAndDrop(slider, 0, -12);
        slider.shouldHave(attribute("aria-valuenow", "14"));
    }

    private void openSliderPage()
    {
        Selenide.open("https://demos.telerik.com/kendo-ui/slider/index");
        boolean overlayIsVisible = true;
        try
        {
            $("#onetrust-accept-btn-handler").shouldBe(visible);
        }
        catch (ElementNotFound e)
        {
            overlayIsVisible = false;
        }

        if (overlayIsVisible)
        {
            $("#onetrust-accept-btn-handler").click();
            $("#onetrust-consent-sdk .onetrust-pc-dark-filter").shouldBe(hidden);
            Selenide.refresh();
        }
        
        Selenide.sleep(2000);
        SelenideElement questionaire = $("#qual_ol .qual_x_close");
        if (questionaire.isDisplayed()) {
        	questionaire.click();
        }
    }

    private void leftHorizontalDragAndDropUntilAttribute(SelenideElement elementToMove, SelenideElement elementToCheck, int horizontalMovement,
                                                         String sliderValueAttributeName, String moveUntil)
    {
        // Example: create a special method to move until a given text
        SelenideAddons.dragAndDropUntilCondition(elementToMove, elementToCheck, horizontalMovement, 0, 3000, 10,
                                                 Condition.attribute(sliderValueAttributeName, moveUntil));
    }

    @Test
    public void testOpenHtmlContentWithCurrentWebDriver()
    {
        final String text = "Hi\n\nHow are you?)\n\nBye";
        final String textHtml = "<div dir=\"auto\">Hi<div dir=\"auto\"><br></div><div dir=\"auto\">How are you?)</div><div dir=\"auto\"><br></div><div dir=\"auto\">Bye</div></div>";

        SelenideAddons.openHtmlContentWithCurrentWebDriver(textHtml);
        Assert.assertEquals(text, $("body").getText());
    }
}