package com.xceptance.neodymium.junit5.testclasses.browser.inheritance;

import com.xceptance.neodymium.common.browser.Browser;
import com.xceptance.neodymium.common.browser.RandomBrowsers;

@Browser("Chrome_1024x768")
@Browser("Chrome_1500x1000")
@Browser("FF_1024x768")
@Browser("FF_1500x1000")
@RandomBrowsers(2)
public abstract class RandomBrowsersParent
{

}
