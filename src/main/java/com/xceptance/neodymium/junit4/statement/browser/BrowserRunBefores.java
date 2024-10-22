package com.xceptance.neodymium.junit4.statement.browser;

import java.util.List;

import org.junit.internal.runners.statements.RunBefores;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.xceptance.neodymium.common.browser.BrowserBeforeRunner;

public class BrowserRunBefores extends RunBefores
{
    private final Statement next;

    private final List<FrameworkMethod> befores;

    public BrowserRunBefores(Statement next, List<FrameworkMethod> befores, Object target)
    {
        super(next, befores, target);
        this.befores = befores;
        this.next = next;
    }

    @Override
    public void evaluate() throws Throwable
    {
        for (FrameworkMethod before : befores)
        {

            new BrowserBeforeRunner().run(() -> {
                try
                {
                    invokeMethod(before);
                }
                catch (Throwable e)
                {
                    return e;
                }
                return null;
            }, before.getMethod(), false);
        }
        next.evaluate();
    }
}
