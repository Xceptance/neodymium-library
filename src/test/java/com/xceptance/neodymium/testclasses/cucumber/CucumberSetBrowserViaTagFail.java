package com.xceptance.neodymium.testclasses.cucumber;

import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumCucumberRunner;

import cucumber.api.CucumberOptions;

@RunWith(NeodymiumCucumberRunner.class)
@CucumberOptions(features = "src/test/resources/com/xceptance/neodymium/testclasses/cucumber/CucumberSetBrowserViaTagFail.feature",
                 glue = "com/xceptance/neodymium/testclasses/cucumber", plugin = "null_summary")
public class CucumberSetBrowserViaTagFail
{
}
