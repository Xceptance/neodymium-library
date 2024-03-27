package com.xceptance.neodymium.recording.config;

import org.aeonbits.owner.Mutable;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

/**
 * Configuration interface, which contains on the values needed for both gif and video recording. Needed to enable
 * abstract handling of the subclasses. Contains the key and default values for gif as this format is default for
 * recording
 * 
 * @author Xceptance Software Technologies
 */
@LoadPolicy(LoadType.MERGE)
public abstract interface RecordingConfigurations extends Mutable
{
    public boolean enableFilming();

    public boolean filmAutomaticaly();

    public int oneImagePerMilliseconds();

    public String tempFolderToStoreRecording();

    public boolean deleteRecordingsAfterAddingToAllureReport();

    public boolean appendAllRecordingsToReport();

    public double imageQuality();

    public double imageScaleFactor();

    public String format();
}
