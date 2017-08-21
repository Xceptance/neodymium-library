package com.xceptance.multibrowser.runner;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.runners.model.FrameworkMethod;

import com.xceptance.multibrowser.dto.BrowserConfigurationDto;

/**
 * A specialization of {@link FrameworkMethod}, which replaces the default method name with the provided name and the
 * test data set used.
 */
public class AnnotatedFrameworkMethod extends FrameworkMethod
{
    /**
     * The test data set to use.
     */
    private final Map<String, String> dataSet;

    /**
     * The browser configuration to use.
     */
    private final BrowserConfigurationDto browserConfiguration;

    /**
     * The new method name.
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param method
     *            the test method
     * @param testMethodName
     *            the name to show for the test method
     * @param index
     *            the index of the test run
     * @param dataSet
     *            the test data set
     */
    public AnnotatedFrameworkMethod(final Method method, final String testMethodName, final BrowserConfigurationDto browserConfiguration,
                                    final int index, final Map<String, String> dataSet)
    {
        super(method);

        this.dataSet = dataSet;
        this.browserConfiguration = browserConfiguration;

        if (index == -1)
        {
            name = String.format("%s - %s", browserConfiguration.getName(), testMethodName);
        }
        else
        {
            name = String.format("%s - %s[%d] - %s", browserConfiguration.getName(), testMethodName, index, dataSet);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        return this == obj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return System.identityHashCode(this);
    }

    public BrowserConfigurationDto getBrowserConfiguration()
    {
        return browserConfiguration;
    }

    public Map<String, String> getDataSet()
    {
        return dataSet;
    }
}
