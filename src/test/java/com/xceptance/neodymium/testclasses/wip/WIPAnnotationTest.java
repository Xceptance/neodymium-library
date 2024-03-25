package com.xceptance.neodymium.testclasses.wip;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.WIP;

@RunWith(NeodymiumRunner.class)
public class WIPAnnotationTest

{
    @WIP
    @Test
    public void first()
    {
    }

    @Test
    public void second()
    {
    }
}
