package com.xceptance.neodymium.recording.config;

import org.aeonbits.owner.Config.Sources;

@Sources(
{
  "system:env", "system:properties", "file:config/dev-video-recording.properties", "file:config/video-recording.properties"
})
public interface VideoRecordingConfigurations extends RecordingConfigurations
{
    @Key("video.enableFilming")
    @DefaultValue("false")
    public boolean enableFilming();

    @Key("video.filmAutomaticaly")
    @DefaultValue("true")
    public boolean filmAutomaticaly();

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

    @Key("video.ffmpegBinaryPath")
    @DefaultValue("ffmpeg")
    public String ffmpegBinaryPath();

    @Key("video.ffmpegLogFile")
    @DefaultValue("target/ffmpeg_output_msg.txt")
    public String ffmpegLogFile();

    @Override
    @DefaultValue("mp4")
    public String format();

    @Override
    @DefaultValue("false")
    public boolean loop();
}
