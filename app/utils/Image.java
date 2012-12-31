package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import org.imgscalr.*;

import play.Logger;

import javax.imageio.ImageIO;

public class Image {

	public static boolean resizeAndSaveImage(File sourceImage, 
			File targetImage, int targetImageSize, String targetImageType) {

		try {
			BufferedImage originalImage = ImageIO.read(sourceImage);
			int height = originalImage.getHeight();
			int width = originalImage.getWidth();

			// Resize the image only if it is more than given image size
			if (height > targetImageSize || width > targetImageSize) {
				BufferedImage newImage = AsyncScalr.resize(originalImage,
						Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC,
						targetImageSize, Scalr.OP_ANTIALIAS).get();

				ImageIO.write(newImage, targetImageType, targetImage);
				newImage.flush();
				newImage = null;
			}
			// If the image size is less, then maintain the scale of the
			// destination image
			else {
				ImageIO.write(originalImage, targetImageType, targetImage);
			}

			originalImage.flush();
			return true;

		} catch (Exception exception) {
			Logger.error("Error while resizing and saving image " + sourceImage.getName() + " to " + targetImage.getName(), exception);
			return false;
		}

	}
	
	
	/**
	 * Returns the web url where the photo can be accessed
	 */
	public static String getPhoto(String filename) {
		return "http://" + Util.getStringProperty("aws.s3.bucket.uploaded.photos") 
				+ "." + Util.getStringProperty("aws.s3.endpoint") 
				+ "/" + filename; 
	}

}
