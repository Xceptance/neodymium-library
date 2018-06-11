package com.xceptance.neodymium.testclasses.context.cucumbercontextclear;

import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumCucumberRunner;

import cucumber.api.CucumberOptions;

@RunWith(NeodymiumCucumberRunner.class)
@CucumberOptions(features = "src/test/resources/com/xceptance/neodymium/testclasses/context/cucumbercontextclear", glue = "com/xceptance/neodymium/testclasses/context/cucumbercontextclear")
public class CucumberContextGetsCleared
{
}
