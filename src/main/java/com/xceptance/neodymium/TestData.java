package com.xceptance.neodymium;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;

import org.junit.runners.JUnit4;

/**
 * This annotation is used to mark a variable as target for test data injection in {@link JUnit4} test cases. The
 * annotation will be processed by {@link NeodymiumRunner}. Therefore the test case has to be annotated correctly, see
 * {@link NeodymiumRunner}. It is only one {@code @TestData} variable supported (first found). The test data variable
 * must be an public {@code public Map<String, String>} and may be static. Also this variable may be defined in a super
 * class.
 * <p>
 * For futher information about test data see <a href=
 * "https://github.com/Xceptance/neodymium-library/wiki/Test-data-provider">https://github.com/Xceptance/neodymium-library/wiki/Test-data-provider</a>
 * <p>
 * <b>Example</b>
 * 
 * <pre>
 * &#64;TestData
 * public Map&ltString, String&gt myTestData;
 * 
 * &#64;Test
 * public void testMethod() {
 *     String firstname = myTestData.get("firstname");  // get entry "firstname" from injected test data
 * }
 * </pre>
 * 
 * The variable may be initialized. {@link NeodymiumRunner} will inject a {@link HashMap} before the test method is
 * called.
 * 
 * @author m.kaufmann
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface TestData
{
    public String value() default "ID";
}
