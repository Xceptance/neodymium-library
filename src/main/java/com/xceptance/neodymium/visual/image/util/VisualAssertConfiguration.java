package com.xceptance.neodymium.visual.image.util;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
{
  "file:./config/visualassertion.properties"
})
public interface VisualAssertConfiguration extends Config
{

    @Key("neodymium.visualassertion.enabled")
    public boolean enabled();

    @Key("neodymium.visualassertion.ID")
    @DefaultValue("all")
    public String id();

    @Key("neodymium.visualassertion.resultDirectory")
    @DefaultValue("results/visualassertion")
    public String resultDirectory();

    @Key("neodymium.visualassertion.waitingTime")
    @DefaultValue("300")
    public int waitingTime();

    @Key("neodymium.visualassertion.mark.blocksize.x")
    @DefaultValue("10")
    public int markBlocksizeX();

    @Key("neodymium.visualassertion.mark.blocksize.y")
    @DefaultValue("10")
    public int markBlocksizeY();

    // "box" or "marker"
    @Key("neodymium.visualassertion.mark.type")
    @DefaultValue("box")
    public String markType();

    @Key("neodymium.visualassertion.fuzzy.blocksize.xy")
    @DefaultValue("10")
    public int fuzzyBlocksizeXY();

    @Key("neodymium.visualassertion.tolerance.colors")
    @DefaultValue("0.1")
    public double toleranceColors();

    @Key("neodymium.visualassertion.tolerance.pixels")
    @DefaultValue("0.2")
    public double tolerancePixels();

    @Key("neodymium.visualassertion.trainingsMode")
    @DefaultValue("false")
    public boolean trainingMode();

    @Key("neodymium.visualassertion.mask.close")
    @DefaultValue("false")
    public boolean maskClose();

    @Key("neodymium.visualassertion.mask.close.width")
    @DefaultValue("5")
    public int maskCloseWidth();

    @Key("neodymium.visualassertion.mask.close.height")
    @DefaultValue("5")
    public int maskCloseHeight();

    @Key("neodymium.visualassertion.onFailure.createDifferenceImage")
    @DefaultValue("true")
    public boolean createDifferenceImage();

    // "FUZZY" or "COLORFUZZY" or "EXACT"
    @Key("neodymium.visualassertion.algorithm")
    @DefaultValue("FUZZY")
    public String algorithm();

}
