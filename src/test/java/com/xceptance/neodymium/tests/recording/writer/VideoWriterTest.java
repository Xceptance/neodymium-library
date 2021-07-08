package com.xceptance.neodymium.tests.recording.writer;

import com.xceptance.neodymium.recording.FilmTestExecution;
import com.xceptance.neodymium.recording.writers.VideoWriter;

public class VideoWriterTest extends AbstractWriterTest
{
    public VideoWriterTest()
    {
        super(FilmTestExecution.getContextVideo(), VideoWriter.class);
    }
}
