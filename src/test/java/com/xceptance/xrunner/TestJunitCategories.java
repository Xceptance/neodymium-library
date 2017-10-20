package com.xceptance.xrunner;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xceptance.neodymium.NeodymiumRunner;

@RunWith(NeodymiumRunner.class)
// @RunWith(Categories.class)
// @Category(Double.class)
// @RunWith(Parameter ized.class)
public class TestJunitCategories
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestJunitCategories.class);

    // @BeforeClass
    // public static void failingBeforeClass() throws Exception
    // {
    // LOGGER.debug("@BeforeClass");
    // // throw new RuntimeException("Opps, we failed in @BeforeClass");
    // }

    // @Parameter(0)
    // String para1;
    //
    // // @Parameters
    // public static List<Object[]> getData()
    // {
    // List<Object[]> data = new LinkedList<>();
    // data.add(new Object[]
    // {
    // "1"
    // });
    //
    // return data;
    // }

    public TestJunitCategories()
    {
        LOGGER.debug(this.getClass().getName() + " - constructor");
        System.out.println(this.getClass().getName() + " - constructor");
    }

    @Test
    @Category(Number.class)
    public void test0()
    {
        LOGGER.debug(this.getClass().getName() + " - test0");
        System.out.println(this.getClass().getName() + " - test0");
    }

    @Test
    @Category(Long.class)
    public void test1()
    {
        LOGGER.debug(this.getClass().getName() + " - test1");
        System.out.println(this.getClass().getName() + " - test1");
    }

    @Test
    @Category(Double.class)
    public void test2()
    {
        LOGGER.debug(this.getClass().getName() + " - test2");
        System.out.println(this.getClass().getName() + " - test2");
    }
}
