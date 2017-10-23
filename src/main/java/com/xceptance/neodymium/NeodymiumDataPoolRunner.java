package com.xceptance.neodymium;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.TestClass;

import com.xceptance.neodymium.datapool.core.DataListPool;
import com.xceptance.neodymium.datapool.core.DataListPoolCache;
import com.xceptance.neodymium.datapool.core.DataPoolProvider;
import com.xceptance.neodymium.datapool.core.PoolEntry;

public class NeodymiumDataPoolRunner extends Runner
{
    private MethodExecutionContext methodExecutionContext;

    private TestClass testClass;

    public NeodymiumDataPoolRunner(TestClass testClass, MethodExecutionContext methodExecutionContext)
    {
        this.testClass = testClass;
        this.methodExecutionContext = methodExecutionContext;
    }

    @Override
    public Description getDescription()
    {
        Description description = Description.createSuiteDescription("DataPool", testClass.getJavaClass().getAnnotations());
        return description;
    }

    @Override
    public void run(RunNotifier notifier)
    {
        try
        {
            injectDataPoolParameter();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void injectDataPoolParameter() throws InstantiationException, IllegalAccessException
    {
        List<FrameworkField> dataPoolProvidedFields = testClass.getAnnotatedFields(DataPoolProvider.class);
        DataListPoolCache dataListPoolCache = DataListPoolCache.getInstance();

        Object testInstance = methodExecutionContext.getTestClassInstance();

        for (FrameworkField field : dataPoolProvidedFields)
        {
            DataPoolProvider dataPoolProviderAnnotation = field.getAnnotation(DataPoolProvider.class);
            Class<? extends DataListPool<?>> pool = dataPoolProviderAnnotation.pool();

            Object dataProvider = dataListPoolCache.getDataListProvider(pool);
            if (dataProvider == null)
            {
                pool.newInstance();
                dataProvider = dataListPoolCache.getDataListProvider(pool);
                if (dataProvider == null)
                {
                    throw new RuntimeException("Could not initialize data pool provider: " + pool.getCanonicalName());
                }
            }

            PoolEntry entry = dataPoolProviderAnnotation.entry();
            Object value;
            switch (entry)
            {
                case First:
                    // TODO: first, not random
                    value = ((DataListPool) dataProvider).getRandomEntry();
                    break;
                case Last:
                    // TODO: last, not random
                    value = ((DataListPool) dataProvider).getRandomEntry();
                    break;
                case Random:
                default:
                    value = ((DataListPool) dataProvider).getRandomEntry();
                    break;
            }

            if (value == null)
            {
                throw new RuntimeException("The data pool provider " + dataProvider.getClass().getCanonicalName() +
                                           " does not contain any entries");
            }

            field.getField().set(testInstance, value);
        }
    }

}
