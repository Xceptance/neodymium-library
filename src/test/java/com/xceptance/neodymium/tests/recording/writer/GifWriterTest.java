package com.xceptance.neodymium.tests.recording.writer;

import com.xceptance.neodymium.recording.FilmTestExecution;
import com.xceptance.neodymium.recording.writers.GifSequenceWriter;

public class GifWriterTest extends AbstractWriterTest
{
    public GifWriterTest()
    {
        super(FilmTestExecution.getContextGif(),GifSequenceWriter.class);
    }
}
