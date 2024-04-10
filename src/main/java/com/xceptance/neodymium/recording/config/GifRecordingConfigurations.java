package com.xceptance.neodymium.recording.config;

import org.aeonbits.owner.Config.Sources;

/**
 * Configuration interface, which contains the values needed for gif recording. 
 * Contains the key and default values for gif.
 * 
 * @author Xceptance Software Technologies
 */
@Sources(
{
  "${recording.temporaryConfigFile}", "system:env", "system:properties", "file:config/dev-gif-recording.properties", "file:config/dev-neodymium.properties", "file:config/gif-recording.properties", "file:config/neodyium.properties"
})
public interface GifRecordingConfigurations extends RecordingConfigurations
{
    @Key("gif.enableFilming")
    @DefaultValue("false")
    public boolean enableFilming();

    @Key("gif.filmAutomatically")
    @DefaultValue("true")
    public boolean filmAutomatically();

    @Key("gif.oneImagePerMilliseconds")
    @DefaultValue("500")
    public int oneImagePerMilliseconds();

    @Key("gif.tempFolderToStoreRecording")
    @DefaultValue("target/gifs/")
    public String tempFolderToStoreRecording();

    @Key("gif.deleteRecordingsAfterAddingToAllureReport")
    @DefaultValue("true")
    public boolean deleteRecordingsAfterAddingToAllureReport();

    @Key("gif.appendAllRecordingsToAllureReport")
    @DefaultValue("true")
    public boolean appendAllRecordingsToAllureReport();

    @Key("gif.imageQuality")
    @DefaultValue("0.2")
    public double imageQuality();

    @Key("gif.imageScaleFactor")
    @DefaultValue("1.0")
    public double imageScaleFactor();
    
    @Key("gif.format")
    @DefaultValue("gif")
    public String format();
    
    @Key("gif.loop")
    @DefaultValue("false")
    public boolean loop();
}
