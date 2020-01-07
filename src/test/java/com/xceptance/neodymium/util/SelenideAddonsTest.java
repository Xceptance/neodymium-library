package com.xceptance.neodymium.util;

import static com.codeborne.selenide.Condition.exist;
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

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
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
    @Test
    public void testMatchesAttributeCondition()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();

        $("#search-container .search-field").should(SelenideAddons.matchesAttribute("placeholder", "Search"));
    }

    @Test
    public void testMatchAttributeCondition()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();

        $("#search-container .search-field").should(SelenideAddons.matchAttribute("placeholder", "^S.a.c.\\s…"));
        $("#search-container .search-field").should(SelenideAddons.matchAttribute("placeholder", "\\D+"));
    }

    @Test(expected = ElementShould.class)
    public void testMatchAttributeConditionError()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();

        $("#search-container .search-field").should(SelenideAddons.matchAttribute("placeholder", "\\d+"));
    }

    @Test(expected = ElementShould.class)
    public void testMatchAttributeConditionErrorMissingAttribute()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();

        $("#search-container .search-field").should(SelenideAddons.matchAttribute("foo", "bar"));
    }

    @Test
    public void testMatchesValueCondition()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();
        $("#search-container .search-field").val("searchphrase").submit();

        $("#content .search-field").should(SelenideAddons.matchesValue("earchphras"));
    }

    @Test
    public void testMatchValueCondition()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();
        $("#search-container .search-field").val("searchphrase").submit();

        $("#content .search-field").should(SelenideAddons.matchValue("^s.a.c.p.r.s.$"));
        $("#content .search-field").should(SelenideAddons.matchValue("\\D+"));
    }

    @Test(expected = ElementShould.class)
    public void testMatchValueConditionError()
    {
        Selenide.open("https://blog.xceptance.com/");
        $("#masthead .search-toggle").click();
        $("#search-container .search-field").val("searchphrase").submit();

        $("#content .search-field").should(SelenideAddons.matchValue("\\d+"));
    }

    @Test()
    public void testWrapAssertion()
    {
        Selenide.open("https://blog.xceptance.com/");
        SelenideAddons.wrapAssertionError(() -> {
            Assert.assertEquals("Passionate Testing | Xceptance Blog", Selenide.title());
        });
    }

    @Test(expected = UIAssertionError.class)
    public void testWrapAssertionError()
    {
        Selenide.open("https://blog.xceptance.com/");
        SelenideAddons.wrapAssertionError(() -> {
            Assert.assertEquals("MyPageTitle", Selenide.title());
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

        Selenide.open("https://blog.xceptance.com/");
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

        Selenide.open("https://blog.xceptance.com/");
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
            Selenide.open("https://blog.xceptance.com/");
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
        Selenide.open("https://blog.xceptance.com/");
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
                         throw getWrappepThrowable("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw getWrappepThrowable("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw getWrappepThrowable("You shall not pass!");
                     });
        runArray.add(
                     () -> {
                         throw getWrappepThrowable("You shall pass!");
                     });
        runArray.add(
                     () -> {
                         throw getWrappepThrowable("You shall never be seen!");
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

    private RuntimeException getWrappepThrowable(String message)
    {
        return new RuntimeException(new StaleElementReferenceException(message));
    }
}
