package com.xceptance.neodymium.module.vector;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import com.xceptance.neodymium.multibrowser.Browser;

public class BrowserVectorBuilder implements RunVectorBuilder
{
    private String[] browser;

    @Override
    public void create(TestClass testClass, FrameworkMethod frameworkMethod)
    {
        // get the @Browser annotation from the method to run as well as from the enclosing class
        // if it doesn't exist check the class for a @Browser annotation
        Browser methodBrowser = frameworkMethod.getAnnotation(Browser.class);
        Browser classBrowser = testClass.getAnnotation(Browser.class);

        if (methodBrowser != null)
        {
            browser = methodBrowser.value();
        }
        else if (classBrowser != null)
        {
            browser = classBrowser.value();
        }
    }

    @Override
    public List<RunVector> buildRunVectors()
    {
        List<RunVector> r = new LinkedList<>();
        int vectorHashCode = (browser == null) ? 0 : Arrays.hashCode(browser);

        // create a vector for every browser tag
        for (String browserTag : browser)
        {
            r.add(new BrowserVector(browserTag, vectorHashCode));
        }

        return r;
    }
}
