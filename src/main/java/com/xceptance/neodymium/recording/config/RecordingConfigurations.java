package com.xceptance.neodymium.recording.config;

import org.aeonbits.owner.Mutable;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

/**
 * Configuration interface, which contains the variables needed for both gif and video recording.
 * 
 * @author Xceptance Software Technologies
 */
@LoadPolicy(LoadType.MERGE)
public abstract interface RecordingConfigurations extends Mutable
{
    public boolean enableFilming();

    public boolean filmAutomatically();

    public int oneImagePerMilliseconds();

    public String tempFolderToStoreRecording();

    public boolean deleteRecordingsAfterAddingToAllureReport();

    public boolean appendAllRecordingsToAllureReport();

    public double imageQuality();

    public double imageScaleFactor();

    public String format();
}
