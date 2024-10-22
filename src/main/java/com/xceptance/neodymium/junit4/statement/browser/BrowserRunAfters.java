package com.xceptance.neodymium.junit4.statement.browser;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.runners.statements.RunAfters;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import com.xceptance.neodymium.common.browser.BrowserAfterRunner;

public class BrowserRunAfters extends RunAfters
{
    private final Statement next;

    private final List<FrameworkMethod> afters;

    public BrowserRunAfters(Statement next, List<FrameworkMethod> afters, Object target)
    {
        super(next, afters, target);
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
        }
        catch (Throwable e)
        {
            errors.add(e);
        }
        finally
        {
            for (FrameworkMethod each : afters)
            {
                new BrowserAfterRunner().run(() -> {
                    try
                    {
                        invokeMethod(each);
                    }
                    catch (Throwable e)
                    {
                        return e;
                    }
                    return null;
                }, each.getMethod(), false);
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}
