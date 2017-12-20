package com.xceptance.neodymium.visual.ai.util;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
{
  "file:./config/ai.properties"
})
public interface AiConfiguration extends Config
{

    @Key("com.xceptance.xlt.ai.enabled")
    public boolean enabled();

    @Key("com.xceptance.xlt.ai.ID")
    @DefaultValue("all")
    public String id();

    @Key("com.xceptance.xlt.ai.resultDirectory")
    @DefaultValue("results/ai")
    public String resultDirectory();

    @Key("com.xceptance.xlt.ai.TESTCASE_BOUND")
    @DefaultValue("true")
    public boolean testCaseBound();

    @Key("com.xceptance.xlt.ai.TESTCASE_NAME")
    @DefaultValue("unbound")
    public String testCaseName();

    @Key("com.xceptance.xlt.ai.TRAINING")
    @DefaultValue("true")
    public boolean trainingMode();

    @Key("com.xceptance.xlt.ai.WAITINGTIME")
    @DefaultValue("1000")
    public int waitingTime();

    @Key("com.xceptance.xlt.ai.IMAGE_HEIGHT")
    @DefaultValue("800")
    public int imageHeight();

    @Key("com.xceptance.xlt.ai.IMAGE_WIDTH")
    @DefaultValue("600")
    public int imageWidth();

    @Key("com.xceptance.xlt.ai.FORMAT")
    @DefaultValue("png")
    public String imageFormat();

    @Key("com.xceptance.xlt.ai.USE_ORIGINAL_SIZE")
    @DefaultValue("true")
    public boolean useOriginalSize();

    @Key("com.xceptance.xlt.ai.USE_COLOR_FOR_COMPARISON")
    @DefaultValue("false")
    public boolean useColorForComparison();

    @Key("com.xceptance.xlt.ai.LEARNING_RATE")
    @DefaultValue("0.2")
    public double learningRate();

    @Key("com.xceptance.xlt.ai.INTENDED_PERCENTAGE_MATCH")
    @DefaultValue("0.80")
    public double percentageMatch();

    @Key("com.xceptance.xlt.ai.PERCENTAGE_DIFFERENCE")
    @DefaultValue("0.1")
    public double percentageDifference();

}
