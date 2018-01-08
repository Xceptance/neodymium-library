# Neodymium
A library to extend and improve the JUnit testing framework.

## What is Neodymium?
Neodymium is the glue between today's state of the art testing technologies.
It's a sophisticated approach to develop fast and meaningful tests. We take [**JUnit4**](https://github.com/junit-team/junit4) as testing framework so you basically
just write acceptance/unit tests. Since Neodymium aims for web tests we added [**Selenide**](https://github.com/codeborne/selenide), a comprehensive and smart framework for
HTML tests that is based on [**Selenium**](https://github.com/SeleniumHQ/selenium) which in turn allows you to control web
browsers. Then we added our [**multi browser support**](https://github.com/Xceptance/multi-browser-suite) which enables you to
run you tests in all different browser configurations either local or in the cloud (e.g. BrowserStack, Sauce Labs). Also we have a well tested and straight forward [**Test data**](https://github.com/Xceptance/neodymium-library/wiki/Test-data-provider) approach. Just write down your data sets and Neodymium will take care of it. Finally we take all the test output and put them in a nifty [**Allure**](https://github.com/allure-framework/allure2) report.
Sounds good? Take a closer look at [**Neodymium-Example**](https://github.com/Xceptance/neodymium-example) which is an test project for our [**Posters**](https://github.com/Xceptance/neodymium-library/wiki/Posters-demo-store) demo shop. If you want to straight start over with your own site then consider using our [**template**](https://github.com/Xceptance/neodymium-template).


## Getting started
Add Neodymium-Library to you project
```xml
<repository>
    <id>xc-nexus</id>
    <url>https://lab.xceptance.de/nexus/content/groups/public</url>
</repository>
...
<dependency>
    <groupId>com.xceptance</groupId>
    <artifactId>neodymium-library</artifactId>
    <version>1.0.0</version>
</dependency>
```
Add the `@RunWith` annotation to your test class or its superclass. This enables Neodymium for test execution.
```java
@RunWith(NeodymiumRunner.class)
public class MyTests
{
    @Test 
    public void testMethod()
    {
        // test code
    }
}
```

## Features
### Multi browser
Enables you to run you Selenide tests in many different browsers.
You also can use cloud-based browser like Sauce Labs and BrowserStack or you can create your own browser cloud with [**Grid2**](https://github.com/SeleniumHQ/selenium/wiki/Grid2). See the wiki page [**Multi-browser-support**](https://github.com/Xceptance/neodymium-library/wiki/Multi-browser-support)

### Test data
Our [**test data provider**](https://github.com/Xceptance/neodymium-library/wiki/Test-data-provider) enables you to define your [**test data**](https://github.com/Xceptance/neodymium-library/wiki/Test-data-provider#package-test-data) and data sets alongside your test cases. While test data or package test data are used to configure common settings (e.g. web site URL, language, currency and so on) for many test cases in that package or sub package, data sets are specific for one test case that cause the test case to be executed for every data set that is defined (e.g. search terms: do the same test over and over again for every defined search term). Neodymium will look up test data for each test case and inject that data into your test context.

### Cucumber multi browser support
We added multi browser support to Cucumber test cases by 
A Java BDD (Behaviour Driven Development) implementation 


### Allure
A tool that creates reports from test executions. See [**Allure**](https://github.com/allure-framework/allure2) 

A test automation template based on best practice libraries and added missing functionalities to aid test automation done by Xceptance.