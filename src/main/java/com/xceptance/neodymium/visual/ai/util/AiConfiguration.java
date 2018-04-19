package com.xceptance.neodymium.visual.ai.util;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
{
  "file:./config/ai.properties"
})
public interface AiConfiguration extends Config
{

    @Key("neodymium.ai.enabled")
    @DefaultValue("true")
    public boolean enabled();

    @Key("neodymium.ai.ID")
    @DefaultValue("all")
    public String id();

    @Key("neodymium.ai.TESTCASE_BOUND")
    @DefaultValue("true")
    public boolean testCaseBound();

    @Key("neodymium.ai.TESTCASE_NAME")
    @DefaultValue("unbound")
    public String testCaseName();

    @Key("neodymium.ai.TRAINING")
    @DefaultValue("true")
    public boolean trainingMode();

    @Key("neodymium.ai.WAITINGTIME")
    @DefaultValue("1000")
    public int waitingTime();

    @Key("neodymium.ai.IMAGE_HEIGHT")
    @DefaultValue("800")
    public int imageHeight();

    @Key("neodymium.ai.IMAGE_WIDTH")
    @DefaultValue("600")
    public int imageWidth();

    @Key("neodymium.ai.FORMAT")
    @DefaultValue("png")
    public String imageFormat();

    @Key("neodymium.ai.USE_ORIGINAL_SIZE")
    @DefaultValue("true")
    public boolean useOriginalSize();

    @Key("neodymium.ai.USE_COLOR_FOR_COMPARISON")
    @DefaultValue("false")
    public boolean useColorForComparison();

    @Key("neodymium.ai.LEARNING_RATE")
    @DefaultValue("0.2")
    public double learningRate();

    @Key("neodymium.ai.INTENDED_PERCENTAGE_MATCH")
    @DefaultValue("0.80")
    public double percentageMatch();

    @Key("neodymium.ai.PERCENTAGE_DIFFERENCE")
    @DefaultValue("0.1")
    public double percentageDifference();

}
