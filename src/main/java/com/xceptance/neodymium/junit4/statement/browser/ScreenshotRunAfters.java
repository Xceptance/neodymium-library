package com.xceptance.neodymium.junit4.statement.browser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.internal.runners.statements.RunAfters;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import com.xceptance.neodymium.common.ScreenshotWriter;

public class ScreenshotRunAfters extends RunAfters
{
    private final String displayName;

    private final String className;

    private final Statement next;

    private final List<FrameworkMethod> afters;

    public ScreenshotRunAfters(String displayName, String className, Statement next, List<FrameworkMethod> afters, Object target)
    {
        super(next, afters, target);
        this.displayName = displayName.replaceAll("\\d+\\s\\/\\s\\d+", "");
        this.className = className;
        this.afters = afters;
        this.next = next;
    }

    @Override
    public void evaluate() throws Throwable
    {
        List<Throwable> errors = new ArrayList<Throwable>();
        try
        {
            next.evaluate();
            new ScreenshotWriter().doScreenshot(displayName, className, Optional.empty());
        }
        catch (Throwable e)
        {
            errors.add(e);
            new ScreenshotWriter().doScreenshot(displayName, className, Optional.of(e));
        }
        finally
        {
            for (FrameworkMethod each : afters)
            {
                try
                {
                    invokeMethod(each);
                }
                catch (Throwable e)
                {
                    errors.add(e);
                }
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}
