package com.xceptance.neodymium.testclasses.datautils;

import java.text.MessageFormat;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xceptance.neodymium.NeodymiumRunner;
import com.xceptance.neodymium.util.Context;
import com.xceptance.neodymium.util.DataUtils;

@RunWith(NeodymiumRunner.class)
public class DataUtilsTests
{
    private static final String NIL = "not in list";

    @Test
    public void testAsString() throws Exception
    {
        Map<String, String> data = Context.get().data;
        data.clear();
        data.put("nullValue", null);
        data.put("empty", "");
        data.put("value", "value");

        // expect IllegalArgumentException
        expectIAE(() -> {
            DataUtils.asString(null);
        });
        expectIAE(() -> {
            DataUtils.asString("nullValue");
        });
        expectIAE(() -> {
            DataUtils.asString("");
        });
        expectIAE(() -> {
            DataUtils.asString(NIL);
        });

        Assert.assertEquals("", DataUtils.asString("empty"));
        Assert.assertEquals("value", DataUtils.asString("value"));

        Assert.assertEquals(null, DataUtils.asString(null, null));
        Assert.assertEquals(null, DataUtils.asString("", null));
        Assert.assertEquals(null, DataUtils.asString("nullValue", null));
        Assert.assertEquals(null, DataUtils.asString(NIL, null));

    }

    @Test
    public void testAsInt() throws Exception
    {
        Map<String, String> data = Context.get().data;
        data.clear();
        data.put("nullValue", null);
        data.put("empty", "");
        data.put("positiveValue", "3");
        data.put("negativeValue", "-3");
        data.put("zeroValue", "0");

        // expect IllegalArgumentException
        expectIAE(() -> {
            DataUtils.asInt(null);
        });
        expectIAE(() -> {
            DataUtils.asInt("nullValue");
        });
        expectIAE(() -> {
            DataUtils.asInt("");
        });
        expectIAE(() -> {
            DataUtils.asInt(NIL);
        });

        expectNFE(() -> {
            DataUtils.asInt("empty");
        });

        Assert.assertEquals(3, DataUtils.asInt("positiveValue"));
        Assert.assertEquals(-3, DataUtils.asInt("negativeValue"));
        Assert.assertEquals(0, DataUtils.asInt("zeroValue"));

        Assert.assertEquals(3, DataUtils.asInt(null, 3));
        Assert.assertEquals(3, DataUtils.asInt("", 3));
        Assert.assertEquals(3, DataUtils.asInt("nullValue", 3));
        Assert.assertEquals(3, DataUtils.asInt(NIL, 3));
    }

    @Test
    public void testAsLong() throws Exception
    {
        Map<String, String> data = Context.get().data;
        data.clear();
        data.put("nullValue", null);
        data.put("empty", "");
        data.put("positiveValue", "3");
        data.put("negativeValue", "-3");
        data.put("zeroValue", "0");

        // expect IllegalArgumentException
        expectIAE(() -> {
            DataUtils.asLong(null);
        });
        expectIAE(() -> {
            DataUtils.asLong("nullValue");
        });
        expectIAE(() -> {
            DataUtils.asLong("");
        });
        expectIAE(() -> {
            DataUtils.asLong(NIL);
        });

        expectNFE(() -> {
            DataUtils.asLong("empty");
        });

        Assert.assertEquals(3, DataUtils.asLong("positiveValue"));
        Assert.assertEquals(-3, DataUtils.asLong("negativeValue"));
        Assert.assertEquals(0, DataUtils.asLong("zeroValue"));

        Assert.assertEquals(3, DataUtils.asLong(null, 3));
        Assert.assertEquals(3, DataUtils.asLong("", 3));
        Assert.assertEquals(3, DataUtils.asLong("nullValue", 3));
        Assert.assertEquals(3, DataUtils.asLong(NIL, 3));
    }

    @Test
    public void testAsFloat() throws Exception
    {
        Map<String, String> data = Context.get().data;
        data.clear();
        data.put("nullValue", null);
        data.put("empty", "");
        data.put("positiveValue", "3.3f");
        data.put("negativeValue", "-3.3");
        data.put("zeroValue", ".0");

        // expect IllegalArgumentException
        expectIAE(() -> {
            DataUtils.asFloat(null);
        });
        expectIAE(() -> {
            DataUtils.asFloat("nullValue");
        });
        expectIAE(() -> {
            DataUtils.asFloat("");
        });
        expectIAE(() -> {
            DataUtils.asFloat(NIL);
        });

        expectNFE(() -> {
            DataUtils.asFloat("empty");
        });

        Assert.assertEquals(3.3, DataUtils.asFloat("positiveValue"), 0.000001);
        Assert.assertEquals(-3.3, DataUtils.asFloat("negativeValue"), 0.000001);
        Assert.assertEquals(0, DataUtils.asFloat("zeroValue"), 0.000001);

        Assert.assertEquals(3, DataUtils.asFloat(null, 3), 0.000001);
        Assert.assertEquals(3, DataUtils.asFloat("", 3), 0.000001);
        Assert.assertEquals(3, DataUtils.asFloat("nullValue", 3), 0.000001);
        Assert.assertEquals(3, DataUtils.asFloat(NIL, 3), 0.000001);
    }

    @Test
    public void testAsDouble() throws Exception
    {
        Map<String, String> data = Context.get().data;
        data.clear();
        data.put("nullValue", null);
        data.put("empty", "");
        data.put("positiveValue", "3.3d");
        data.put("negativeValue", "-3.3");
        data.put("zeroValue", ".0");

        // expect IllegalArgumentException
        expectIAE(() -> {
            DataUtils.asDouble(null);
        });
        expectIAE(() -> {
            DataUtils.asDouble("nullValue");
        });
        expectIAE(() -> {
            DataUtils.asDouble("");
        });
        expectIAE(() -> {
            DataUtils.asDouble(NIL);
        });

        expectNFE(() -> {
            DataUtils.asDouble("empty");
        });

        Assert.assertEquals(3.3, DataUtils.asDouble("positiveValue"), 0.000001);
        Assert.assertEquals(-3.3, DataUtils.asDouble("negativeValue"), 0.000001);
        Assert.assertEquals(0, DataUtils.asDouble("zeroValue"), 0.000001);

        Assert.assertEquals(3, DataUtils.asDouble(null, 3), 0.000001);
        Assert.assertEquals(3, DataUtils.asDouble("", 3), 0.000001);
        Assert.assertEquals(3, DataUtils.asDouble("nullValue", 3), 0.000001);
        Assert.assertEquals(3, DataUtils.asDouble(NIL, 3), 0.000001);
    }

    @Test
    public void testAsBoolean() throws Exception
    {
        Map<String, String> data = Context.get().data;
        data.clear();
        data.put("nullValue", null);
        data.put("empty", "");
        data.put("positiveValue", "true");
        data.put("negativeValue", "FaLsE");

        // expect IllegalArgumentException
        expectIAE(() -> {
            DataUtils.asBool(null);
        });
        expectIAE(() -> {
            DataUtils.asBool("nullValue");
        });
        expectIAE(() -> {
            DataUtils.asBool("");
        });
        expectIAE(() -> {
            DataUtils.asBool(NIL);
        });

        Assert.assertEquals(false, DataUtils.asBool("empty"));
        Assert.assertEquals(true, DataUtils.asBool("positiveValue"));
        Assert.assertEquals(false, DataUtils.asBool("negativeValue"));

        Assert.assertEquals(true, DataUtils.asBool(null, true));
        Assert.assertEquals(true, DataUtils.asBool("", true));
        Assert.assertEquals(true, DataUtils.asBool("nullValue", true));
        Assert.assertEquals(true, DataUtils.asBool(NIL, true));
    }

    private void expectIAE(Runnable function)
    {
        expectException(function, IllegalArgumentException.class);
    }

    private void expectNFE(Runnable function)
    {
        expectException(function, NumberFormatException.class);
    }

    private void expectException(Runnable function, Class<? extends Throwable> expectedException)
    {
        Throwable caughtException = null;

        try
        {
            function.run();
        }
        catch (Throwable e)
        {
            caughtException = e;
        }

        String caughtExeptionName = "no exception!";
        if (caughtException != null)
            caughtExeptionName = caughtException.getClass().toString();

        if (caughtException == null || caughtException.getClass() != expectedException)
        {
            Assert.fail(MessageFormat.format("Expected exception {0} but caught {1}", expectedException.toString(), caughtExeptionName));
        }
    }

}
