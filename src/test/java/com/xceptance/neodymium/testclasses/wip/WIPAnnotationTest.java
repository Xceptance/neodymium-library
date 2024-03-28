package com.xceptance.neodymium.testclasses.wip;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.WorkInProgress;

@RunWith(NeodymiumRunner.class)
public class WIPAnnotationTest

{
    @WorkInProgress
    @Test
    public void first()
    {
    }

    @Test
    public void second()
    {
    }
}
