package com.xceptance.neodymium.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Assert;

/**
 * This class generates images which can be uploaded.
 * 
 * @author Xceptance Software Technologies
 *
 */
public class ImageGenerator
{
	private static void testImage(BufferedImage image, int indexColor)
	{
	    int forrestGreen = new Color(74, 103, 64).getRGB();
	    int oceanBlue = new Color(33, 64, 101).getRGB();
	    int bordeauxRed = new Color(128, 0, 50).getRGB();
	    int[] colors = {bordeauxRed, forrestGreen, oceanBlue};
	    
	    int width = image.getWidth();
	    int height= image.getHeight();
	        
	    // Create image with defined color
	    for(int i = 0; i < width; i++)
	    {
	        for(int j = 0; j < height; j++)
	        {
	            image.setRGB(i, j, colors[indexColor-1]);
	        }
	    }
	}
	
    public static File generateImage(int indexColor)
    {
    	int width = 1920;
    	int height = 1080;
    	
    	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);	
    	testImage(image, indexColor);    	
		File newImageFile = null;
		
		try
    	{
			long timestamp = System.currentTimeMillis();
			newImageFile = File.createTempFile("Neodymium-Writer-Test-Image-" + Long.toString(timestamp), ".jpg");
			newImageFile.createNewFile();
			newImageFile.deleteOnExit();
			
			ImageIO.write(image, "jpg", newImageFile);
    	}
		catch(Exception e)
		{
			Assert.fail("Generating a new image failed");
		}
		
		return newImageFile;
    }
}
