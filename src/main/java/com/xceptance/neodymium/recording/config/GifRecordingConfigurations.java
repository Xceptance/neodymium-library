package com.xceptance.neodymium.recording.config;

import org.aeonbits.owner.Config.Sources;

@Sources(
    {
      "system:env", "system:properties", "file:config/dev-gif-recording.properties", "file:config/gif-recording.properties"
    })
public interface GifRecordingConfigurations extends RecordingConfigurations
{
    @Key("gif.loop")
    @DefaultValue("false")
    public boolean loop();

    @Override
    @DefaultValue("gif")
    public String format();
}
