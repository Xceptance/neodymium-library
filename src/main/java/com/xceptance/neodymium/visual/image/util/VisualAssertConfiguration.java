package com.xceptance.neodymium.visual.image.util;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources(
{
  "file:./config/visualassertion.properties"
})
public interface VisualAssertConfiguration extends Config
{

    @Key("com.xceptance.xlt.visualassertion.enabled")
    public boolean enabled();

    @Key("com.xceptance.xlt.visualassertion.ID")
    @DefaultValue("all")
    public String id();

    @Key("com.xceptance.xlt.visualassertion.resultDirectory")
    @DefaultValue("results/visualassertion")
    public String resultDirectory();

    @Key("com.xceptance.xlt.visualassertion.waitingTime")
    @DefaultValue("300")
    public int waitingTime();

    @Key("com.xceptance.xlt.visualassertion.mark.blocksize.x")
    @DefaultValue("10")
    public int markBlocksizeX();

    @Key("com.xceptance.xlt.visualassertion.mark.blocksize.y")
    @DefaultValue("10")
    public int markBlocksizeY();

    // "box" or "marker"
    @Key("com.xceptance.xlt.visualassertion.mark.type")
    @DefaultValue("box")
    public String markType();

    @Key("com.xceptance.xlt.visualassertion.fuzzy.blocksize.xy")
    @DefaultValue("10")
    public int fuzzyBlocksizeXY();

    @Key("com.xceptance.xlt.visualassertion.tolerance.colors")
    @DefaultValue("0.1")
    public double toleranceColors();

    @Key("com.xceptance.xlt.visualassertion.tolerance.pixels")
    @DefaultValue("0.2")
    public double tolerancePixels();

    @Key("com.xceptance.xlt.visualassertion.trainingsMode")
    @DefaultValue("false")
    public boolean trainingMode();

    @Key("com.xceptance.xlt.visualassertion.mask.close")
    @DefaultValue("false")
    public boolean maskClose();

    @Key("com.xceptance.xlt.visualassertion.mask.close.width")
    @DefaultValue("5")
    public int maskCloseWidth();

    @Key("com.xceptance.xlt.visualassertion.mask.close.height")
    @DefaultValue("5")
    public int maskCloseHeight();

    @Key("com.xceptance.xlt.visualassertion.onFailure.createDifferenceImage")
    @DefaultValue("true")
    public boolean createDifferenceImage();

    // "FUZZY" or "COLORFUZZY" or "EXACT"
    @Key("com.xceptance.xlt.visualassertion.algorithm")
    @DefaultValue("FUZZY")
    public String algorithm();

}
