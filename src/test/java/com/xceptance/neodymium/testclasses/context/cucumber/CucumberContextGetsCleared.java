package com.xceptance.neodymium.testclasses.context.cucumber;

import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumCucumberRunner;

import cucumber.api.CucumberOptions;

@RunWith(NeodymiumCucumberRunner.class)
@CucumberOptions(features = "src/test/java/com/xceptance/neodymium/testclasses/context/cucumber",
                 glue = "com/xceptance/neodymium/testclasses/context/cucumber")
public class CucumberContextGetsCleared
{
}
