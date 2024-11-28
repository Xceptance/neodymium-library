package com.xceptance.neodymium.junit5.tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.xceptance.neodymium.junit4.testclasses.popupblocker.PopupBlockerTestclass;
import com.xceptance.neodymium.junit5.tests.utils.NeodymiumTestExecutionSummary;

public class PopupBlockerTest extends AbstractNeodymiumTest
{
    @Test
    public void testPopupBlocker()
    {
        Map<String, String> properties = new HashMap<>();

        properties.put("neodymium.popup.custom", "#myWindow");

        addPropertiesForTest("temp-PopupBlockerTest-neodymium.properties", properties);

        NeodymiumTestExecutionSummary summary = run(PopupBlockerTestclass.class);
        checkPass(summary, 3, 0);
    }
}
