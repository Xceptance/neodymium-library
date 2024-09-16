package com.xceptance.neodymium.junit5.browser;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import com.xceptance.neodymium.common.browser.BrowserAfterRunner;
import com.xceptance.neodymium.common.browser.BrowserBeforeRunner;

public class BrowserAfterEachExecutionCallback implements InvocationInterceptor
{
    @Override
    public void interceptBeforeEachMethod(Invocation<Void> invocation,
                                          ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext)
        throws Throwable
    {

        new BrowserBeforeRunner().run(() -> {
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

    @Override
    public void interceptAfterEachMethod(Invocation<Void> invocation,
                                         ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext)
        throws Throwable
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

}
