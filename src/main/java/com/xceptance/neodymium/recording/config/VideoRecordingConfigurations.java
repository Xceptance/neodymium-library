package com.xceptance.neodymium.recording.config;

import org.aeonbits.owner.Config.Sources;

/**
 * Configuration interface, which contains the values needed for video recording. 
 * Contains the key and default values for video.
 * 
 * @author Xceptance Software Technologies
 */
@Sources(
{
  "${recording.temporaryConfigFile}", "system:env", "system:properties", "file:config/dev-video-recording.properties", "file:config/dev-neodymium.properties", "file:config/video-recording.properties", "file:config/neodyium.properties"
})
public interface VideoRecordingConfigurations extends RecordingConfigurations
{
    @Key("video.enableFilming")
    @DefaultValue("false")
    public boolean enableFilming();

    @Key("video.filmAutomatically")
    @DefaultValue("true")
    public boolean filmAutomatically();

    @Key("video.oneImagePerMilliseconds")
    @DefaultValue("100")
    public int oneImagePerMilliseconds();

    @Key("video.tempFolderToStoreRecording")
    @DefaultValue("target/videos/")
    public String tempFolderToStoreRecording();

    @Key("video.deleteRecordingsAfterAddingToAllureReport")
    @DefaultValue("true")
    public boolean deleteRecordingsAfterAddingToAllureReport();

    @Key("video.appendAllRecordingsToReport")
    @DefaultValue("false")
    public boolean appendAllRecordingsToReport();

    @Key("video.imageQuality")
    @DefaultValue("1.0")
    public double imageQuality();

    @Key("video.imageScaleFactor")
    @DefaultValue("1.0")
    public double imageScaleFactor();
    
    @Key("video.format")
    @DefaultValue("mp4")
    public String format();

    @Key("video.ffmpegBinaryPath")
    @DefaultValue("ffmpeg")
    public String ffmpegBinaryPath();

    @Key("video.ffmpegLogFile")
    @DefaultValue("target/ffmpeg_output_msg.txt")
    public String ffmpegLogFile();
}
