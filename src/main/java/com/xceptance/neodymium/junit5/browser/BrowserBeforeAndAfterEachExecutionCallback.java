package com.xceptance.neodymium.junit5.browser;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import com.xceptance.neodymium.common.browser.BrowserAfterRunner;
import com.xceptance.neodymium.util.Neodymium;

public class BrowserBeforeAndAfterEachExecutionCallback implements InvocationInterceptor
{
    @Override
    public void interceptAfterEachMethod(Invocation<Void> invocation,
                                         ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext)
        throws Throwable
    {
        if (Neodymium.configuration().startNewBrowserForCleanUp())
        {
            new BrowserAfterRunner().run(() -> {
                try
                {
                    invocation.proceed();
                }
                catch (Throwable e)
                {
                    return e;
                }
                return null;

            }, invocationContext.getExecutable(), true);
        }
        else
        {
            invocation.proceed();
        }
    }
}
