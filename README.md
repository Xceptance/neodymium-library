# Neodymium
A library to extend and improve the JUnit testing framework.

## What is Neodymium?
Neodymium is the glue between today's state of the art testing technologies.
It's a sophisticated approach to develop fast and meaningful tests. We take [**JUnit4**](https://github.com/junit-team/junit4) as testing framework so you basically
just write acceptance/unit tests. Since Neodymium aims for web tests we added [**Selenide**](https://github.com/codeborne/selenide), a comprehensive and smart framework for
HTML tests that is based on [**Selenium**](https://github.com/SeleniumHQ/selenium) which in turn allows you to control web
browsers. Then we added our [**multi browser support**](https://github.com/Xceptance/multi-browser-suite) which enables you to
run you tests in all different browser configurations either local or in the cloud (e.g. BrowserStack, Sauce Labs). Also we have a well tested and straight
forward **Test data** approach. Just write down your data sets and Neodymium will
take care of it. Finally we take all the test output and put them in a nifty [**Allure**](https://github.com/allure-framework/allure2) report.
Sounds good? Take a closer look at [**Neodymium-Example**](https://github.com/Xceptance/neodymium-example).


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
    }
}
```

## Features
### Multi-Browser
Enables you to run you Selenide tests in many different browsers.

You also can use cloud-based browser like Sauce Labs and BrowserStack or you can create your own browser cloud.
 
Run your Selenide tests in any browser. Just define  
### Test data
- 

### Cucumber multi browser support
We added multi browser support to Cucumber test cases by 
A Java BDD (Behaviour Driven Development) implementation 

A test automation template based on best practice libraries and added missing functionalities to aid test automation done by Xceptance.


### Allure
Allure reports are 