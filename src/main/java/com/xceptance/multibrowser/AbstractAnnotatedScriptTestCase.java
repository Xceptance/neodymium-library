package com.xceptance.multibrowser;

import org.junit.After;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.xceptance.multibrowser.annotation.TestTargets;
import com.xceptance.multibrowser.runner.AnnotationRunner;
import com.xceptance.xlt.api.engine.scripting.AbstractScriptTestCase;

/**
 * This is a wrapper-class designed to run XLT-testcases. To use this class you simply inherit from this and add
 * {@link TestTargets} annotation that contains a list of {@link TestTarget} annotations. Within this annotations you
 * can specify in what browser environment your testcase should be executed. You are able to specify the browser,
 * browserversion, operating system and the scope (remote at SauceLabs or local). Furthermore you can define a own for
 * every scenario to distinguish your testcases from each other.
 * 
 * @author m.kaufmann
 */
@RunWith(AnnotationRunner.class)
public abstract class AbstractAnnotatedScriptTestCase extends AbstractScriptTestCase
{
    /**
     * Quits the {@link WebDriver} instance, but only if it was created implicitly. If set explicitly, the caller is
     * responsible to quit the driver instance properly.
     */
    @After
    public final void quitWebDriver()
    {
        WebDriver webDriver = getWebDriver();
        if (webDriver != null)
        {
            webDriver.quit();
        }
    }
}
